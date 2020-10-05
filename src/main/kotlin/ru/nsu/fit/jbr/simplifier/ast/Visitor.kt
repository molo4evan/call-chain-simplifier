package ru.nsu.fit.jbr.simplifier.ast

/**
 * Base visitor interface. Used to go over the AST.
 */
interface Visitor<T> {

    fun visit(node: CallChain): T
    fun visit(node: FilterCall): T
    fun visit(node: BinLogicExpr): T
    fun visit(node: CmpExpr): T
    fun visit(node: MapCall): T
    fun visit(node: BinArithmExpr): T
    fun visit(node: Element): T
    fun visit(node: MinusExpr): T
    fun visit(node: NumberExpr): T
}