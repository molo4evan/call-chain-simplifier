package ru.nsu.fit.jbr.simplifier.generation

import ru.nsu.fit.jbr.simplifier.ast.*

/**
 * Generates call chain code by visiting AST.
 */
class ChainCodeVisitor: UnitVisitor {

    /**
     * Contains code text of call chain after visiting.
     */
    val stringBuilder = StringBuilder()

    override fun visit(node: CallChain) {
        if (node.calls.isEmpty()) {
            return
        }

        for (i in 0 until node.calls.size - 1) {
            node.calls[i].accept(this)
            stringBuilder.append("%>%")
        }
        node.calls.last().accept(this)
    }

    override fun visit(node: FilterCall) {
        stringBuilder.append("filter{")
        node.expr.accept(this)
        stringBuilder.append("}")
    }

    override fun visit(node: BinLogicExpr) {
        stringBuilder.append("(")
        node.left.accept(this)
        stringBuilder.append(node.type.op)
        node.right.accept(this)
        stringBuilder.append(")")
    }

    override fun visit(node: CmpExpr) {
        stringBuilder.append("(")
        node.left.accept(this)
        stringBuilder.append(node.type.op)
        node.right.accept(this)
        stringBuilder.append(")")
    }

    override fun visit(node: MapCall) {
        stringBuilder.append("map{")
        node.expr.accept(this)
        stringBuilder.append("}")
    }

    override fun visit(node: BinArithmExpr) {
        stringBuilder.append("(")
        node.left.accept(this)
        stringBuilder.append(node.type.op)
        node.right.accept(this)
        stringBuilder.append(")")
    }

    override fun visit(node: Element) {
        stringBuilder.append("element")
    }

    override fun visit(node: MinusExpr) {
        stringBuilder.append("-")
        node.number.accept(this)
    }

    override fun visit(node: NumberExpr) {
        stringBuilder.append(node.value)
    }
}