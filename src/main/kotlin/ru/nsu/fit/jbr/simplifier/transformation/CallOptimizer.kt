package ru.nsu.fit.jbr.simplifier.transformation

import ru.nsu.fit.jbr.simplifier.ast.*
import ru.nsu.fit.jbr.simplifier.ast.ArithmType.*
import ru.nsu.fit.jbr.simplifier.ast.CmpType.*
import ru.nsu.fit.jbr.simplifier.ast.LogicType.AND
import ru.nsu.fit.jbr.simplifier.ast.LogicType.OR
import ru.nsu.fit.jbr.simplifier.generation.CallChainEvaluator
import kotlin.math.ceil
import kotlin.math.roundToInt
import kotlin.math.sqrt

/**
 * Produces simplified call chain (simplifies logic operations, compare operation, arithmetic operations).
 * This is the most complex class in that project.
 * Comments can contain OP - arithmetic operation, LOG_OP - logic operation, or CMP_OP - compare operation.
 * -> means expression transformation, e means element, c or const means numerical constant.
 */
class CallOptimizer : CopyVisitor {

    companion object {
        /**
         * True constant
         */
        fun trueCmp() = CmpExpr(NumberExpr("1"), EQ, NumberExpr("1"))

        /**
         * False constant
         */
        fun falseCmp() = CmpExpr(NumberExpr("1"), EQ, NumberExpr("0"))
    }

    /**
     * Evaluator for getting value of constant expressions.
     * It will work incorrectly with expression containing elements.
     */
    private val constEvaluator = CallChainEvaluator(0)

    /**
     * Replaces chain with default 'filtering all' expression if first filter is always false.
     */
    override fun visit(node: CallChain): CallChain {

        val calls = node.calls.map { it.accept(this) as Call }.toMutableList()

        if (calls.isEmpty()) return node

        val firstCall = calls[0]
        return if (firstCall is FilterCall
            && firstCall.expr is CmpExpr
            && (firstCall.expr as CmpExpr).isAlwaysFalse()
        ) {
            filteringAll()
        } else {
            CallChain(calls)
        }
    }

    /**
     * Default chain which filters all elements.
     */
    private fun filteringAll() = CallChain(mutableListOf(FilterCall(falseCmp()), MapCall(Element)))

    override fun visit(node: BinLogicExpr): LogicExpr {
        node.left = node.left.accept(this) as LogicExpr
        node.right = node.right.accept(this) as LogicExpr

        val left = node.left
        val right = node.right
        val type = node.type
        return if (left is CmpExpr && right is CmpExpr) {
            simplifyCmp(left, type, right) ?: node
        } else {
            node
        }
    }

    /**
     * Tries to simplify compare expressions.
     */
    private fun simplifyCmp(left: CmpExpr, type: LogicType, right: CmpExpr): LogicExpr? {
        return processAsymptCmp(left, type, right)
            ?: processSimpleCmp(left, type, right)
    }

    /**
     * Checks for constant compare operations and simplifies it.
     */
    private fun processAsymptCmp(left: CmpExpr, type: LogicType, right: CmpExpr): LogicExpr? {
        val falseCmp = falseCmp()
        val trueCmp = trueCmp()
        return when {
            left.isAlwaysFalse() && type == OR ||
                    left.isAlwaysTrue() && type == AND -> right

            right.isAlwaysFalse() && type == OR ||
                    right.isAlwaysTrue() && type == AND -> left

            (left.isAlwaysFalse() || right.isAlwaysFalse()) && type == AND -> falseCmp

            (left.isAlwaysTrue() || right.isAlwaysTrue()) && type == OR -> trueCmp
            else -> {
                null
            }
        }
    }

    /**
     * Processes compare operations without inner arithmetic operations.
     */
    private fun processSimpleCmp(left: CmpExpr, type: LogicType, right: CmpExpr): LogicExpr? {
        if (left.left is BinArithmExpr
            || left.right is BinArithmExpr
            || right.left is BinArithmExpr
            || right.right is BinArithmExpr
        ) return null else {
            val stdLeft = getStandardElConst(left) ?: return null
            val stdRight = getStandardElConst(right) ?: return null
            val leftVal = stdLeft.right.accept(constEvaluator)
            val rightVal = stdRight.right.accept(constEvaluator)
            return processElConstElConstCmps(leftVal, stdLeft.type, type, rightVal, stdRight.type)
                ?: processElConstElConstCmps(rightVal, stdRight.type, type, leftVal, stdLeft.type)
        }
    }

    /**
     * Tries to simplify (e leftType leftVal) type (e rightType rightVal).
     * Only for leftVal >= rightVal, for leftVal < rightVal use with left = right and right = left.
     */
    private fun processElConstElConstCmps(
        leftVal: Int,
        leftType: CmpType,
        type: LogicType,
        rightVal: Int,
        rightType: CmpType
    ): LogicExpr? {
        val falseCmp = falseCmp()
        val leftNum = NumberExpr(leftVal.toString())
        val rightNum = NumberExpr(rightVal.toString())
        return when {
            leftVal == rightVal -> {

                if (leftType != rightType) when (type) {
                    // (e CMP_OP1 c) && (e CMP_OP2 c) -> false
                    AND -> falseCmp
                    // (e CMP_OP1 c) || (e CMP_OP2 c) -> not optimized
                    OR -> null
                }
                // (e CMP_OP c) LOG_OP (e CMP_OP c) -> (e CMP_OP c)
                else CmpExpr(Element, leftType, leftNum)
            }
            leftVal > rightVal -> {
                when (leftType) {
                    LESS -> when (rightType) {
                        LESS -> when (type) {
                            // (e < C) & (e < c) -> (e < c)
                            AND -> CmpExpr(Element, rightType, rightNum)
                            // (e < C) | (e < c) -> (e < C)
                            OR -> CmpExpr(Element, leftType, leftNum)
                        }
                        EQ -> when (type) {
                            // (e < C) & (e = c) -> (e = c)
                            AND -> CmpExpr(Element, rightType, rightNum)
                            // (e < C) | (e = c) -> not optimized
                            OR -> null
                        }
                        // (e < C) LOG_OP (e > c) -> not optimized
                        MORE -> null
                    }
                    EQ -> when (rightType) {
                        LESS, EQ -> when (type) {
                            // (e = C) & (e <= c) -> false
                            AND -> falseCmp
                            // (e = C) | (e <= c) -> not optimized
                            OR -> null
                        }
                        // (e = C) LOG_OP (e > c) -> not optimized
                        MORE -> null
                    }
                    MORE -> when (rightType) {
                        // (e > C): for (e < c) and (e = c) - same as in cae of (e = C)
                        LESS, EQ -> when (type) {
                            AND -> falseCmp
                            OR -> null
                        }
                        MORE -> when (type) {
                            // (e > C) && (e > c) -> (e > C)
                            AND -> CmpExpr(Element, leftType, leftNum)
                            // (e > C) || (e > c) -> (e > c)
                            OR -> CmpExpr(Element, rightType, rightNum)
                        }
                    }
                }
            }
            else -> {
                null
            }
        }
    }

    /**
     * Standardizes compare expression with element and const to (e CMP_OP c)
     */
    private fun getStandardElConst(node: CmpExpr): CmpExpr? {
        return when {
            node.left is Element && node.right is ConstExpr -> node
            node.left is ConstExpr && node.right is Element -> CmpExpr(node.right, node.type.reversed(), node.left)
            else -> null
        }
    }

    /**
     * Checks if comparison is always false.
     */
    private fun CmpExpr.isAlwaysFalse(): Boolean {
        if (left !is NumberExpr || right !is NumberExpr) return false
        val leftVal = left.accept(constEvaluator)
        val rightVal = right.accept(constEvaluator)

        return when (type) {
            EQ -> leftVal != rightVal
            MORE -> leftVal <= rightVal
            LESS -> leftVal >= rightVal
        }
    }

    /**
     * Checks if comparison is always true.
     */
    private fun CmpExpr.isAlwaysTrue(): Boolean {
        if (left !is NumberExpr || right !is NumberExpr) return false
        val leftVal = left.accept(constEvaluator)
        val rightVal = right.accept(constEvaluator)

        return when (type) {
            EQ -> leftVal == rightVal
            MORE -> leftVal > rightVal
            LESS -> leftVal < rightVal
        }
    }

    override fun visit(node: CmpExpr): LogicExpr {
        node.left = node.left.accept(this) as ArithmExpr
        node.right = node.right.accept(this) as ArithmExpr

        return processConsts(node)
            ?: processElemens(node)
            ?: processSingleConst(node.left, node.type, node.right)
            ?: node
    }

    /**
     * const1 CMP const2
     */
    private fun processConsts(node: CmpExpr): LogicExpr? {
        return if (node.left !is ConstExpr || node.right !is ConstExpr) null else {
            if (node.accept(constEvaluator) != 0) {
                trueCmp()
            } else {
                falseCmp()
            }
        }
    }

    /**
     * e CMP e
     */
    private fun processElemens(node: CmpExpr): LogicExpr? =
        if (node.left !is Element || node.right !is Element) null else {
            when (node.type) {
                MORE -> falseCmp()
                LESS -> falseCmp()
                EQ -> trueCmp()
            }
        }

    /**
     * expr CMP const | const CMP expr
     */
    private fun processSingleConst(left: ArithmExpr, type: CmpType, right: ArithmExpr): LogicExpr? =
        if (left !is ConstExpr && right !is ConstExpr) null else {
            processBinAndConst(left, type, right)
                ?: processBinAndConst(right, type.reversed(), left)?.accept(this) as? LogicExpr?
        }

    /**
     * expr CMP const
     */
    private fun processBinAndConst(left: ArithmExpr, type: CmpType, right: ArithmExpr): LogicExpr? =
        if (left !is BinArithmExpr || right !is ConstExpr) null else {
            val number = right.accept(constEvaluator)
            processSquareAndConst(left, type, number)
                ?: processSingleConstInBinAndConst(left, type, number)
        }

    /**
     * (e * e) CMP const
     */
    private fun processSquareAndConst(left: BinArithmExpr, type: CmpType, const: Int): LogicExpr? =
        if (left.left !is Element || left.right !is Element || left.type != MULT) null else {
            when {
                type == LESS && const <= 0 -> falseCmp()
                type == MORE && const <= 0 -> trueCmp()
                type == EQ && const < 0 -> falseCmp()
                else -> {
                    val sqrt = sqrt(const.toDouble())
                    if (sqrt != ceil(sqrt)) null
                    else {
                        val sqrtNum = sqrt.roundToInt().toString()
                        when (type) {
                            MORE -> BinLogicExpr(
                                CmpExpr(Element, LESS, NumberExpr(sqrtNum)),
                                AND,
                                CmpExpr(Element, MORE, MinusExpr(NumberExpr(sqrtNum)))
                            )
                            LESS -> BinLogicExpr(
                                CmpExpr(Element, MORE, NumberExpr(sqrtNum)),
                                OR,
                                CmpExpr(Element, LESS, MinusExpr(NumberExpr(sqrtNum)))
                            )
                            EQ -> CmpExpr(Element, EQ, NumberExpr(sqrtNum))
                        }
                    }
                }
            }
        }

    /**
     * (expr OP const1) CMP const2 | (const1 OP expr) CMP const2
     */
    private fun processSingleConstInBinAndConst(left: BinArithmExpr, type: CmpType, const: Int): LogicExpr? =
        processArithmConstAndConst(left.left, left.type, left.right, type, const)
            ?: processArithmConstAndConst(left.right, left.type, left.left, type, const)

    /**
     * (expr OP const1) CMP const2
     */
    private fun processArithmConstAndConst(
        left: ArithmExpr,
        arithm: ArithmType,
        right: ArithmExpr,
        cmp: CmpType,
        const: Int
    ): LogicExpr? =
        if (right !is ConstExpr) null else {
            val valueToRemove = right.accept(constEvaluator)
            when (arithm) {
                PLUS -> CmpExpr(left.clone(), cmp, NumberExpr((const - valueToRemove).toString()))
                MINUS -> CmpExpr(left.clone(), cmp, NumberExpr((const + valueToRemove).toString()))
                MULT -> {
                    try {
                        val result = const / valueToRemove.toDouble()
                        if (result != ceil(result)) null else
                            CmpExpr(left.clone(), cmp, NumberExpr(result.roundToInt().toString()))
                    } catch (ex: Exception) {
                        null
                    }
                }
            }
        }

    override fun visit(node: BinArithmExpr): ArithmExpr {
        node.left = node.left.accept(this) as ArithmExpr
        node.right = node.right.accept(this) as ArithmExpr

        val left = node.left
        val right = node.right
        val type = node.type
        return processElements(node)
            ?: processConsts(node)
            ?: processSingleConst(left, type, right)
            ?: processSingleElem(left, type, right)
            ?: processBins(left, type, right)
            ?: node
    }

    /**
     * e OP e
     */
    private fun processElements(node: BinArithmExpr): ArithmExpr? =
        if (node.left !is Element || node.right !is Element) null else {
            when (node.type) {
                PLUS -> BinArithmExpr(Element, MULT, NumberExpr("2"))
                MINUS -> NumberExpr("0")
                MULT -> null
            }
        }

    /**
     * c1 OP c2
     */
    private fun processConsts(node: BinArithmExpr): ArithmExpr? {
        if (node.left !is ConstExpr || node.right !is ConstExpr) return null

        val res = node.accept(constEvaluator)
        return if (res > 0) {
            NumberExpr(res.toString())
        } else {
            MinusExpr(NumberExpr((-res).toString()))
        }
    }

    /**
     * expr OP const | const OP expr
     */
    private fun processSingleConst(left: ArithmExpr, type: ArithmType, right: ArithmExpr): ArithmExpr? =
        if (left !is ConstExpr && right !is ConstExpr) null
        else processBinAndConst(left, type, right)
            ?: processBinAndConst(right, type, left)

    /**
     * expr OP const
     */
    private fun processBinAndConst(left: ArithmExpr, type: ArithmType, right: ArithmExpr): ArithmExpr? =
        if (left !is BinArithmExpr || right !is ConstExpr) null else {
            val res = right.accept(constEvaluator)
            when (type) {
                PLUS,
                MINUS -> {
                    processSimpleBinAndConst(left.left, left.type, left.right, type, res)
                        ?: processBinAndMinus(left, type, res)
                }
                MULT -> {
                    val optimizedLeft =
                        BinArithmExpr(left.left, type, NumberExpr(res.toString())).accept(this) as ArithmExpr
                    val optimizedRight =
                        BinArithmExpr(left.right, type, NumberExpr(res.toString())).accept(this) as ArithmExpr
                    BinArithmExpr(optimizedLeft, left.type, optimizedRight)
                }
            }
        }

    /**
     * expr OP -c
     */
    private fun processBinAndMinus(left: ArithmExpr, type: ArithmType, const: Int): ArithmExpr? =
        if (type == MULT || const >= 0) null else {
            val opposite = if (type == MINUS) PLUS else MINUS
            BinArithmExpr(left, opposite, NumberExpr(const.toString()))
        }

    /**
     * (e OP c1) OP c2 | (c1 OP e) OP c2
     */
    private fun processSimpleBinAndConst(
        left: ArithmExpr,
        inner: ArithmType,
        right: ArithmExpr,
        outer: ArithmType,
        const: Int
    ): ArithmExpr? = if (left is BinArithmExpr || right is BinArithmExpr || outer == MULT) null else {
        processElConstBinAndConst(left, inner, right, outer, const)
            ?: processElConstBinAndConst(right, inner, left, outer, const)
    }

    /**
     * (e OP c1) OP c2
     */
    private fun processElConstBinAndConst(
        elExpr: ArithmExpr,
        inner: ArithmType,
        iConst: ArithmExpr,
        outer: ArithmType,
        oConst: Int
    ): ArithmExpr? = if (elExpr !is Element || iConst !is ConstExpr) null else {
        val number = iConst.accept(constEvaluator)
        var acc = if (inner == PLUS) number else -number
        acc = if (outer == PLUS) acc + oConst else acc - oConst
        when {
            acc > 0 -> BinArithmExpr(Element, PLUS, NumberExpr(acc.toString()))
            acc < 0 -> BinArithmExpr(Element, MINUS, NumberExpr((-acc).toString()))
            else -> Element
        }
    }

    /**
     * e OP e
     */
    private fun processSingleElem(left: ArithmExpr, type: ArithmType, right: ArithmExpr): ArithmExpr? =
        if (left !is Element || right !is Element) null else {
            processBinAndElem(left, type, right) ?: processBinAndElem(right, type, left)
        }

    /**
     * expr OP e
     */
    private fun processBinAndElem(left: ArithmExpr, type: ArithmType, right: ArithmExpr): ArithmExpr? =
        if (left !is BinArithmExpr || right !is Element) null else {
            val optimizedLeft = BinArithmExpr(left.left, type, Element).accept(this) as ArithmExpr
            val optimizedRight = BinArithmExpr(left.right, type, Element).accept(this) as ArithmExpr
            BinArithmExpr(optimizedLeft, left.type, optimizedRight)
        }

    /**
     * expr OP expr
     */
    private fun processBins(left: ArithmExpr, type: ArithmType, right: ArithmExpr): ArithmExpr? =
        if (left !is BinArithmExpr || right !is BinArithmExpr) null else {
            if (type == MULT) {
                processBinsMult(left, right)
            } else null
        }

    /**
     * expr * expr
     */
    private fun processBinsMult(left: BinArithmExpr, right: BinArithmExpr): ArithmExpr? {
        return processElElAndElConst(left, right)
            ?: processElElAndElConst(right, left)
            ?: processElConstAndElConst(left, right)
    }

    /**
     * (e OP e)*(e OP c)
     */
    private fun processElElAndElConst(left: BinArithmExpr, right: BinArithmExpr): ArithmExpr? {
        if (left.type == MULT && right.type == MULT) return null
        if (left.left !is Element || left.right !is Element) return null
        val common = getStandardElConst(right) ?: return null
        val value = common.right.accept(constEvaluator)

        return when {
            // (e - e)*(e OP const) -> 0
            left.type == MINUS -> NumberExpr("0")
            // (e * e)*(e +- const) -> e*e*e +- const*e*e
            left.type == MULT -> BinArithmExpr(
                BinArithmExpr(elem2(), MULT, Element),
                right.type,
                BinArithmExpr(elem2(), MULT, common.right)
            )
            // (e + e)*(e * const) -> 2*const*e*e
            right.type == MULT -> BinArithmExpr(
                NumberExpr((2 * value).toString()),
                MULT,
                elem2()
            )
            // (e + e)*(e +- const) -> 2*e*e +- 2*const*e
            else -> BinArithmExpr(
                BinArithmExpr(NumberExpr("2"), MULT, elem2()),
                right.type,
                BinArithmExpr(NumberExpr((value * 2).toString()), MULT, Element)
            )
        }
    }

    /**
     * (e OP c1)*(e OP c2)
     */
    private fun processElConstAndElConst(left: BinArithmExpr, right: BinArithmExpr): ArithmExpr? {
        val commonLeft = getStandardElConst(left) ?: return null
        val commonRight = getStandardElConst(right) ?: return null

        val leftVal = commonLeft.right.accept(constEvaluator)
        val rightVal = commonRight.right.accept(constEvaluator)

        return tryProcessElConstMultValues(leftVal, left.type, rightVal, right.type)
    }

    /**
     * (e leftType leftVal)*(e rightType rightVal)
     */
    private fun tryProcessElConstMultValues(
        leftVal: Int,
        leftType: ArithmType,
        rightVal: Int,
        rightType: ArithmType
    ): ArithmExpr? {
        return tryProcessDoubleMult(leftVal, leftType, rightVal, rightType)
            ?: tryProcessSingleMult(leftVal, leftType, rightVal, rightType)
            ?: tryProcessSingleMult(rightVal, rightType, leftVal, leftType)
            ?: tryProcessSumSub(rightVal, rightType, leftVal, leftType)
    }

    /**
     * (e * a)*(e * b) -> a*b*e*e
     */
    private fun tryProcessDoubleMult(
        leftVal: Int,
        leftType: ArithmType,
        rightVal: Int,
        rightType: ArithmType
    ): ArithmExpr? = if (leftType != MULT || rightType != MULT) null else {
        BinArithmExpr(
            NumberExpr((leftVal * rightVal).toString()),
            MULT,
            elem2()
        )
    }

    /**
     * (e * a)*(e +- b) -> a*e*e +- a*b*e
     */
    private fun tryProcessSingleMult(
        leftVal: Int,
        leftType: ArithmType,
        rightVal: Int,
        rightType: ArithmType
    ): ArithmExpr? = if (leftType != MULT || rightType == MULT) null else {
        BinArithmExpr(
            BinArithmExpr(NumberExpr(leftType.toString()), MULT, elem2()),
            rightType,
            BinArithmExpr(NumberExpr((leftVal * rightVal).toString()), MULT, Element)
        )
    }

    /**
     * (e +- const1)*(e +- const2) -> e*e +- const1*e +- const2*e + const1*const2
     */
    private fun tryProcessSumSub(
        leftVal: Int,
        leftType: ArithmType,
        rightVal: Int,
        rightType: ArithmType
    ): ArithmExpr? {
        return if (leftType == MULT || rightType == MULT) null else {
            val correctedLeft = if (leftType == MINUS) -leftVal else leftVal
            val correctedRight = if (rightType == MINUS) -rightVal else rightVal
            val b = correctedLeft + correctedRight
            val c = leftVal * rightVal

            BinArithmExpr(
                BinArithmExpr(
                    elem2(),
                    PLUS,
                    BinArithmExpr(NumberExpr(b.toString()), MULT, Element)
                ),
                PLUS,
                NumberExpr(c.toString())
            )
        }
    }

    /**
     * Standardizes arithmetic expression with element and const to (e OP c)
     */
    private fun getStandardElConst(node: BinArithmExpr): BinArithmExpr? {
        return when {
            node.left is Element && node.right is ConstExpr -> node
            node.left is ConstExpr && node.right is Element -> BinArithmExpr(node.right, node.type, node.left)
            else -> null
        }
    }

    /**
     * Gets element^2.
     */
    private fun elem2() = BinArithmExpr(Element, MULT, Element)
}