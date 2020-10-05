package ru.nsu.fit.jbr.simplifier.transformation

import ru.nsu.fit.jbr.simplifier.ast.ArithmExpr
import ru.nsu.fit.jbr.simplifier.ast.MapCall
import ru.nsu.fit.jbr.simplifier.ast.UnitVisitor

/**
 * Processes call chain left-to-right and builds map
 * that is superposition of all maps.
 */
class MapCreator: UnitVisitor {

    var map: MapCall? = null
    private set

    /**
     * Main map = new map with injected main map expression.
     */
    override fun visit(node: MapCall) {
        map = if (map == null) {
            node.copy()
        } else {
            MapCall(node.expr.accept(ArithmExprElementInjector(map!!.expr)) as ArithmExpr)
        }
    }
}