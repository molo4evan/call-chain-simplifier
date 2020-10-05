package ru.nsu.fit.jbr.simplifier.ast

/**
 * Visitor that returns nothing.
 * Can be extended for doing some additional actions on visiting specific nodes.
 */
interface UnitVisitor: Visitor<Unit> {

    override fun visit(node: CallChain) {
        for (call in node.calls) {
            call.accept(this)
        }
    }

    override fun visit(node: FilterCall) {
        node.expr.accept(this)
    }

    override fun visit(node: BinLogicExpr) {
        node.left.accept(this)
        node.right.accept(this)
    }

    override fun visit(node: CmpExpr) {
        node.left.accept(this)
        node.right.accept(this)
    }

    override fun visit(node: MapCall) {
        node.expr.accept(this)
    }

    override fun visit(node: BinArithmExpr) {
        node.left.accept(this)
        node.right.accept(this)
    }

    override fun visit(node: Element) {}

    override fun visit(node: MinusExpr) {
        node.number.accept(this)
    }

    override fun visit(node: NumberExpr) {}
}