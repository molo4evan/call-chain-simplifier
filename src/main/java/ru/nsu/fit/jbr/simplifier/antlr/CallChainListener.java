// Generated from C:/Users/JetBrains/Projects/call-chain-simplifier/src/main/resources\CallChain.g4 by ANTLR 4.8
package ru.nsu.fit.jbr.simplifier.antlr;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link CallChainParser}.
 */
public interface CallChainListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link CallChainParser#minusExpr}.
	 * @param ctx the parse tree
	 */
	void enterMinusExpr(CallChainParser.MinusExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link CallChainParser#minusExpr}.
	 * @param ctx the parse tree
	 */
	void exitMinusExpr(CallChainParser.MinusExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link CallChainParser#constExpr}.
	 * @param ctx the parse tree
	 */
	void enterConstExpr(CallChainParser.ConstExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link CallChainParser#constExpr}.
	 * @param ctx the parse tree
	 */
	void exitConstExpr(CallChainParser.ConstExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link CallChainParser#binExpr}.
	 * @param ctx the parse tree
	 */
	void enterBinExpr(CallChainParser.BinExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link CallChainParser#binExpr}.
	 * @param ctx the parse tree
	 */
	void exitBinExpr(CallChainParser.BinExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link CallChainParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterExpr(CallChainParser.ExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link CallChainParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitExpr(CallChainParser.ExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link CallChainParser#mapCall}.
	 * @param ctx the parse tree
	 */
	void enterMapCall(CallChainParser.MapCallContext ctx);
	/**
	 * Exit a parse tree produced by {@link CallChainParser#mapCall}.
	 * @param ctx the parse tree
	 */
	void exitMapCall(CallChainParser.MapCallContext ctx);
	/**
	 * Enter a parse tree produced by {@link CallChainParser#filterCall}.
	 * @param ctx the parse tree
	 */
	void enterFilterCall(CallChainParser.FilterCallContext ctx);
	/**
	 * Exit a parse tree produced by {@link CallChainParser#filterCall}.
	 * @param ctx the parse tree
	 */
	void exitFilterCall(CallChainParser.FilterCallContext ctx);
	/**
	 * Enter a parse tree produced by {@link CallChainParser#call}.
	 * @param ctx the parse tree
	 */
	void enterCall(CallChainParser.CallContext ctx);
	/**
	 * Exit a parse tree produced by {@link CallChainParser#call}.
	 * @param ctx the parse tree
	 */
	void exitCall(CallChainParser.CallContext ctx);
	/**
	 * Enter a parse tree produced by {@link CallChainParser#callChain}.
	 * @param ctx the parse tree
	 */
	void enterCallChain(CallChainParser.CallChainContext ctx);
	/**
	 * Exit a parse tree produced by {@link CallChainParser#callChain}.
	 * @param ctx the parse tree
	 */
	void exitCallChain(CallChainParser.CallChainContext ctx);
}