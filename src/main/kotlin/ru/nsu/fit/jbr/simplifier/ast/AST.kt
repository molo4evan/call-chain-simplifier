package ru.nsu.fit.jbr.simplifier.ast

/**
 * Base interface of all nodes of internal AST implementation
 */
interface AstNode {
    fun <T> accept(visitor: Visitor<T>): T

    fun clone(): AstNode
}

data class CallChain(val calls: MutableList<Call>): AstNode {
    override fun <T> accept(visitor: Visitor<T>): T {
        return visitor.visit(this)
    }

    override fun clone(): CallChain {
        return copy()
    }
}

interface Call: AstNode

data class FilterCall(var expr: LogicExpr): Call {
    override fun <T> accept(visitor: Visitor<T>): T {
        return visitor.visit(this)
    }

    override fun clone(): FilterCall {
        return copy()
    }
}

sealed class LogicExpr: AstNode {
    abstract override fun clone(): LogicExpr
}

enum class LogicType(val op: String) {
    AND("&"), OR("|");

    companion object {
        private val ops = values().map { it.op to it }.toMap()

        fun byOp(op: String?): LogicType? = ops[op]
    }
}

data class BinLogicExpr(var left: LogicExpr, var type: LogicType, var right: LogicExpr): LogicExpr() {
    override fun <T> accept(visitor: Visitor<T>): T = visitor.visit(this)

    override fun clone(): BinLogicExpr = copy()
}

enum class CmpType(val op: String) {
    MORE(">"), LESS("<"), EQ("=");

    companion object {
        private val ops = values().map { it.op to it }.toMap()

        fun byOp(op: String?): CmpType? = ops[op]
    }

    fun reversed(): CmpType = when (this) {
        MORE -> LESS
        LESS -> MORE
        EQ -> EQ
    }
}

data class CmpExpr(var left: ArithmExpr, var type: CmpType, var right: ArithmExpr): LogicExpr() {
    override fun <T> accept(visitor: Visitor<T>): T = visitor.visit(this)

    override fun clone(): CmpExpr = copy()
}

data class MapCall(var expr: ArithmExpr): Call {
    override fun <T> accept(visitor: Visitor<T>): T = visitor.visit(this)

    override fun clone(): MapCall = copy()
}

sealed class ArithmExpr: AstNode {
    abstract override fun clone(): ArithmExpr
}

enum class ArithmType(val op: String) {
    PLUS("+"), MINUS("-"), MULT("*");

    companion object {
        private val ops = values().map { it.op to it }.toMap()

        fun byOp(op: String?): ArithmType? = ops[op]
    }
}

data class BinArithmExpr(var left: ArithmExpr, var type: ArithmType, var right: ArithmExpr): ArithmExpr() {
    override fun <T> accept(visitor: Visitor<T>): T = visitor.visit(this)

    override fun clone(): BinArithmExpr = copy()
}

object Element: ArithmExpr() {
    override fun <T> accept(visitor: Visitor<T>): T = visitor.visit(this)

    override fun clone(): Element = this
}

sealed class ConstExpr: ArithmExpr()

data class MinusExpr(var number: NumberExpr): ConstExpr() {
    override fun <T> accept(visitor: Visitor<T>): T = visitor.visit(this)

    override fun clone(): MinusExpr = copy()
}

data class NumberExpr(var value: String): ConstExpr() {
    override fun <T> accept(visitor: Visitor<T>): T = visitor.visit(this)

    override fun clone(): NumberExpr = copy()
}
