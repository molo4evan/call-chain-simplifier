package ru.nsu.fit.jbr.simplifier.ast

/**
 * Visitor that provides a copy of passed AST. Can be extended to produce modified AST.
 */
interface CopyVisitor: Visitor<AstNode> {
    override fun visit(node: CallChain): CallChain {
        val calls = mutableListOf<Call>()
        for (call in node.calls) {
            calls.add(call.accept(this) as Call)
        }
        return CallChain(calls)
    }

    override fun visit(node: FilterCall): Call {
        return FilterCall(node.expr.accept(this) as LogicExpr)
    }

    override fun visit(node: BinLogicExpr): AstNode {
        return BinLogicExpr(
            node.left.accept(this) as LogicExpr,
            node.type,
            node.right.accept(this) as LogicExpr
        )
    }

    override fun visit(node: CmpExpr): AstNode {
        return CmpExpr(
            node.left.accept(this) as ArithmExpr,
            node.type,
            node.right.accept(this) as ArithmExpr
        )
    }

    override fun visit(node: MapCall): Call {
        return MapCall(node.expr.accept(this) as ArithmExpr)
    }

    override fun visit(node: BinArithmExpr): AstNode {
        return BinArithmExpr(
            node.left.accept(this) as ArithmExpr,
            node.type,
            node.right.accept(this) as ArithmExpr
        )
    }

    override fun visit(node: Element): ArithmExpr {
        return node
    }

    override fun visit(node: MinusExpr): ArithmExpr {
        return MinusExpr(node.number.accept(this) as NumberExpr)
    }

    override fun visit(node: NumberExpr): NumberExpr {
        return node.copy()
    }
}