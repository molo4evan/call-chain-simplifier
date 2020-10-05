package ru.nsu.fit.jbr.simplifier.transformation

import ru.nsu.fit.jbr.simplifier.ast.*

/**
 * Processes call chain right-to-left and builds filter
 * that is superposition of all filters with intermediate maps applied.
 */
class FilterCreator: UnitVisitor {

    /**
     * Filter to be constructed
     */
    var filter: FilterCall? = null
        private set

    override fun visit(node: CallChain) {
        for (call in node.calls.reversed()) {
            call.accept(this)
        }
    }

    /**
     * Main filter = main filter && current filter
     */
    override fun visit(node: FilterCall) {
        filter = if (filter == null) {
            node.copy()
        } else {
            FilterCall(BinLogicExpr(node.expr.clone(), LogicType.AND, filter!!.expr.clone()))
        }
    }

    /**
     * Injects map expression to filter elements
     */
    override fun visit(node: MapCall) {
        if (filter != null) {
            filter = filter!!.accept(ArithmExprElementInjector(node.expr)) as FilterCall
        }
    }
}