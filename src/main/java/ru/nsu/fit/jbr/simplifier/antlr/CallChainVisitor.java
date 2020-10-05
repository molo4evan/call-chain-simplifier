// Generated from C:/Users/JetBrains/Projects/call-chain-simplifier/src/main/resources\CallChain.g4 by ANTLR 4.8
package ru.nsu.fit.jbr.simplifier.antlr;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link CallChainParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface CallChainVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link CallChainParser#minusExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMinusExpr(CallChainParser.MinusExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link CallChainParser#constExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstExpr(CallChainParser.ConstExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link CallChainParser#binExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBinExpr(CallChainParser.BinExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link CallChainParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpr(CallChainParser.ExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link CallChainParser#mapCall}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMapCall(CallChainParser.MapCallContext ctx);
	/**
	 * Visit a parse tree produced by {@link CallChainParser#filterCall}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFilterCall(CallChainParser.FilterCallContext ctx);
	/**
	 * Visit a parse tree produced by {@link CallChainParser#call}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCall(CallChainParser.CallContext ctx);
	/**
	 * Visit a parse tree produced by {@link CallChainParser#callChain}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCallChain(CallChainParser.CallChainContext ctx);
}