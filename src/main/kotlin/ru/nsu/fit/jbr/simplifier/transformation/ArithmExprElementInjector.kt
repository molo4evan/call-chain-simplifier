package ru.nsu.fit.jbr.simplifier.transformation

import ru.nsu.fit.jbr.simplifier.ast.*

/**
 * Replaces all 'element' entries in AST to specific arithmetic expression.
 */
class ArithmExprElementInjector(
    /**
     * Arithmetic expression to be injected.
     */
    private val injected: ArithmExpr
): CopyVisitor {

    override fun visit(node: BinArithmExpr): AstNode {
        return BinArithmExpr(injectArithm(node.left), node.type, injectArithm(node.right))
    }

    override fun visit(node: Element): ArithmExpr {
        return injectArithm(node)
    }

    override fun visit(node: MapCall): Call {
        return MapCall(injectArithm(node.expr))
    }

    private fun injectArithm(expr: ArithmExpr): ArithmExpr {
        return if (expr is Element) {
            injected.clone()
        } else {
            expr.accept(this) as ArithmExpr
        }
    }
}