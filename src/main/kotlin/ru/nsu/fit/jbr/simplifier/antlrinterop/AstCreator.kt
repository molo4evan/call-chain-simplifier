package ru.nsu.fit.jbr.simplifier.antlrinterop

import org.antlr.v4.runtime.misc.ParseCancellationException
import ru.nsu.fit.jbr.simplifier.antlr.CallChainBaseVisitor
import ru.nsu.fit.jbr.simplifier.antlr.CallChainParser
import ru.nsu.fit.jbr.simplifier.ast.*

/**
 * Creates inner AST implementation bt ANTLR4 parse tree
 */
class AstCreator: CallChainBaseVisitor<AstNode>() {

    override fun visitCallChain(ctx: CallChainParser.CallChainContext?): CallChain {
        if (ctx == null) throw ParseCancellationException("Call chain context is not defined")

        val calls = ctx.call()
        if (calls == null || calls.isEmpty()) throw ParseCancellationException("Call chain context is incorrect")

        return CallChain(calls.map{ visitCall(it) as Call }.toMutableList())
    }

    override fun visitFilterCall(ctx: CallChainParser.FilterCallContext?): FilterCall {
        if (ctx == null) throw ParseCancellationException("Filter call context is not defined")
        if (ctx.expr() == null) throw ParseCancellationException("Filter call context is incorrect")

        val expr = visitExpr(ctx.expr())
        if (expr !is LogicExpr) throw IncorrectTypeCancellationException("Incorrect type for filter expression")
        return FilterCall(expr)
    }

    override fun visitMapCall(ctx: CallChainParser.MapCallContext?): MapCall {
        if (ctx == null) throw ParseCancellationException("Map call context is not defined")
        if (ctx.expr() == null) throw ParseCancellationException("Map call context is incorrect")

        val expr = visitExpr(ctx.expr())
        if (expr !is ArithmExpr) throw IncorrectTypeCancellationException("Incorrect type for map expression")
        return MapCall(expr)
    }

    override fun visitExpr(ctx: CallChainParser.ExprContext?): AstNode {
        return when {
            ctx == null -> throw ParseCancellationException("Expression context is not defined")
            ctx.ELEMENT() != null -> Element
            ctx.constExpr() != null -> visitConstExpr(ctx.constExpr())
            ctx.binExpr() != null -> visitBinExpr(ctx.binExpr())
            else -> throw ParseCancellationException("Expression context is incorrect")
        }
    }

    override fun visitBinExpr(ctx: CallChainParser.BinExprContext?): AstNode {
        if (ctx == null) throw ParseCancellationException("Binary expression context is not defined")
        val op = ctx.OPERATION().text
        val leftCtx = ctx.expr(0)
        val rightCtx = ctx.expr(1)
        if (op == null || leftCtx == null || rightCtx == null) {
            throw ParseCancellationException("Binary expression context is incorrect")
        }
        val left = visitExpr(leftCtx)
        val right = visitExpr(rightCtx)
        when(op) {
            "+", "-", "*" -> {
                val type = ArithmType.byOp(op)
                    ?: throw ParseCancellationException("Binary expression operation is invalid: $op")
                if (left !is ArithmExpr || right !is ArithmExpr) {
                    throw IncorrectTypeCancellationException("Incorrect type for arithmetic binary expression child")
                }
                return BinArithmExpr(left, type, right)
            }
            ">", "<", "=" -> {
                val type = CmpType.byOp(op)
                    ?: throw ParseCancellationException("Binary expression operation is invalid: $op")
                if (left !is ArithmExpr || right !is ArithmExpr) {
                    throw IncorrectTypeCancellationException("Incorrect type for compare expression child")
                }
                return CmpExpr(left, type, right)
            }
            "&", "|" -> {
                val type = LogicType.byOp(op)
                    ?: throw ParseCancellationException("Binary expression operation is invalid: $op")
                if (left !is LogicExpr || right !is LogicExpr) {
                    throw IncorrectTypeCancellationException("Incorrect type for logic binary expression child")
                }
                return BinLogicExpr(left, type, right)
            }
            else -> throw ParseCancellationException("Binary expression operation is invalid: $op")
        }
    }

    override fun visitConstExpr(ctx: CallChainParser.ConstExprContext?): ConstExpr {
        if (ctx == null) throw ParseCancellationException("Constant expression context is not defined")

        return when {
            ctx.NUMBER()?.text != null -> NumberExpr(ctx.NUMBER().text)
            ctx.minusExpr() != null -> visitMinusExpr(ctx.minusExpr())
            else -> throw ParseCancellationException("Constant expression context is incorrect")
        }
    }

    override fun visitMinusExpr(ctx: CallChainParser.MinusExprContext?): MinusExpr {
        if (ctx == null) throw ParseCancellationException("Unary minus expression context is not defined")
        if (ctx.NUMBER().text == null) throw ParseCancellationException("Unary minus expression context is incorrect")
        return MinusExpr(NumberExpr(ctx.NUMBER().text))
    }


}