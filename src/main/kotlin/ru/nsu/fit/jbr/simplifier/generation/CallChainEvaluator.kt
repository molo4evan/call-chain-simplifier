package ru.nsu.fit.jbr.simplifier.generation

import ru.nsu.fit.jbr.simplifier.ast.*

/**
 * Evaluates call chain for specific element.
 * Result of visit should be ignored, result of evaluation stored.
 * For arithmetic nodes returns value of evaluated node.
 * For logic nodes returns 1 if node is true and 0 if node is false.
 */
class CallChainEvaluator(
    /**
     * Initial element value.
     */
    private val initialElement: Int
) : Visitor<Int> {

    private var lastCallResult: Int = initialElement

    /**
     * Contains result of evaluating specific call chain.
     * Null means that element didn't passed some filter.
     */
    var result: Int? = initialElement

    override fun visit(node: CallChain): Int {
        result = initialElement
        lastCallResult = initialElement
        for (call in node.calls) {
            val callResult = call.accept(this)
            if (result == null) return 0
            if (call is MapCall) {
                result = callResult
                lastCallResult = callResult
            }
        }
        return 1
    }

    override fun visit(node: FilterCall): Int {
        val check = node.expr.accept(this)
        if (check == 0) {
            result = null
        }
        return check
    }

    override fun visit(node: BinLogicExpr): Int {
        val left = node.left.accept(this)
        if (left == 0 && node.type == LogicType.AND) {
            return 0
        }
        return node.right.accept(this)
    }

    override fun visit(node: CmpExpr): Int {
        val left = node.left.accept(this)
        val right = node.right.accept(this)
        val cmp = when (node.type) {
            CmpType.MORE -> left > right
            CmpType.LESS -> left < right
            CmpType.EQ -> left == right
        }
        return if (cmp) 1 else 0
    }

    override fun visit(node: MapCall): Int {
        return node.expr.accept(this)
    }

    override fun visit(node: BinArithmExpr): Int {
        val left = node.left.accept(this)
        val right = node.right.accept(this)
        return when (node.type) {
            ArithmType.PLUS -> left + right
            ArithmType.MINUS -> left - right
            ArithmType.MULT -> left * right
        }
    }

    override fun visit(node: Element): Int {
        return lastCallResult
    }

    override fun visit(node: MinusExpr): Int {
        return -node.number.accept(this)
    }

    override fun visit(node: NumberExpr): Int {
        return node.value.toInt()
    }
}