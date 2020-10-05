// Generated from C:/Users/JetBrains/Projects/call-chain-simplifier/src/main/resources\CallChain.g4 by ANTLR 4.8
package ru.nsu.fit.jbr.simplifier.antlr;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.ATN;
import org.antlr.v4.runtime.atn.ATNDeserializer;
import org.antlr.v4.runtime.atn.ParserATNSimulator;
import org.antlr.v4.runtime.atn.PredictionContextCache;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.List;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class CallChainParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.8", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, NUMBER=8, ELEMENT=9, 
		OPERATION=10;
	public static final int
		RULE_minusExpr = 0, RULE_constExpr = 1, RULE_binExpr = 2, RULE_expr = 3, 
		RULE_mapCall = 4, RULE_filterCall = 5, RULE_call = 6, RULE_callChain = 7;
	private static String[] makeRuleNames() {
		return new String[] {
			"minusExpr", "constExpr", "binExpr", "expr", "mapCall", "filterCall", 
			"call", "callChain"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'-'", "'('", "')'", "'map{'", "'}'", "'filter{'", "'%>%'", null, 
			"'element'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, "NUMBER", "ELEMENT", 
			"OPERATION"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "CallChain.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public CallChainParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	public static class MinusExprContext extends ParserRuleContext {
		public TerminalNode NUMBER() { return getToken(CallChainParser.NUMBER, 0); }
		public MinusExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_minusExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CallChainListener ) ((CallChainListener)listener).enterMinusExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CallChainListener ) ((CallChainListener)listener).exitMinusExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CallChainVisitor ) return ((CallChainVisitor<? extends T>)visitor).visitMinusExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MinusExprContext minusExpr() throws RecognitionException {
		MinusExprContext _localctx = new MinusExprContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_minusExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(16);
			match(T__0);
			setState(17);
			match(NUMBER);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ConstExprContext extends ParserRuleContext {
		public TerminalNode NUMBER() { return getToken(CallChainParser.NUMBER, 0); }
		public MinusExprContext minusExpr() {
			return getRuleContext(MinusExprContext.class,0);
		}
		public ConstExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_constExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CallChainListener ) ((CallChainListener)listener).enterConstExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CallChainListener ) ((CallChainListener)listener).exitConstExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CallChainVisitor ) return ((CallChainVisitor<? extends T>)visitor).visitConstExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ConstExprContext constExpr() throws RecognitionException {
		ConstExprContext _localctx = new ConstExprContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_constExpr);
		try {
			setState(21);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case NUMBER:
				enterOuterAlt(_localctx, 1);
				{
				setState(19);
				match(NUMBER);
				}
				break;
			case T__0:
				enterOuterAlt(_localctx, 2);
				{
				setState(20);
				minusExpr();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class BinExprContext extends ParserRuleContext {
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode OPERATION() { return getToken(CallChainParser.OPERATION, 0); }
		public BinExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_binExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CallChainListener ) ((CallChainListener)listener).enterBinExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CallChainListener ) ((CallChainListener)listener).exitBinExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CallChainVisitor ) return ((CallChainVisitor<? extends T>)visitor).visitBinExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BinExprContext binExpr() throws RecognitionException {
		BinExprContext _localctx = new BinExprContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_binExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(23);
			match(T__1);
			setState(24);
			expr();
			setState(25);
			match(OPERATION);
			setState(26);
			expr();
			setState(27);
			match(T__2);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ExprContext extends ParserRuleContext {
		public TerminalNode ELEMENT() { return getToken(CallChainParser.ELEMENT, 0); }
		public ConstExprContext constExpr() {
			return getRuleContext(ConstExprContext.class,0);
		}
		public BinExprContext binExpr() {
			return getRuleContext(BinExprContext.class,0);
		}
		public ExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CallChainListener ) ((CallChainListener)listener).enterExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CallChainListener ) ((CallChainListener)listener).exitExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CallChainVisitor ) return ((CallChainVisitor<? extends T>)visitor).visitExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExprContext expr() throws RecognitionException {
		ExprContext _localctx = new ExprContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_expr);
		try {
			setState(32);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case ELEMENT:
				enterOuterAlt(_localctx, 1);
				{
				setState(29);
				match(ELEMENT);
				}
				break;
			case T__0:
			case NUMBER:
				enterOuterAlt(_localctx, 2);
				{
				setState(30);
				constExpr();
				}
				break;
			case T__1:
				enterOuterAlt(_localctx, 3);
				{
				setState(31);
				binExpr();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class MapCallContext extends ParserRuleContext {
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public MapCallContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_mapCall; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CallChainListener ) ((CallChainListener)listener).enterMapCall(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CallChainListener ) ((CallChainListener)listener).exitMapCall(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CallChainVisitor ) return ((CallChainVisitor<? extends T>)visitor).visitMapCall(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MapCallContext mapCall() throws RecognitionException {
		MapCallContext _localctx = new MapCallContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_mapCall);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(34);
			match(T__3);
			setState(35);
			expr();
			setState(36);
			match(T__4);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FilterCallContext extends ParserRuleContext {
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public FilterCallContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_filterCall; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CallChainListener ) ((CallChainListener)listener).enterFilterCall(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CallChainListener ) ((CallChainListener)listener).exitFilterCall(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CallChainVisitor ) return ((CallChainVisitor<? extends T>)visitor).visitFilterCall(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FilterCallContext filterCall() throws RecognitionException {
		FilterCallContext _localctx = new FilterCallContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_filterCall);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(38);
			match(T__5);
			setState(39);
			expr();
			setState(40);
			match(T__4);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class CallContext extends ParserRuleContext {
		public MapCallContext mapCall() {
			return getRuleContext(MapCallContext.class,0);
		}
		public FilterCallContext filterCall() {
			return getRuleContext(FilterCallContext.class,0);
		}
		public CallContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_call; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CallChainListener ) ((CallChainListener)listener).enterCall(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CallChainListener ) ((CallChainListener)listener).exitCall(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CallChainVisitor ) return ((CallChainVisitor<? extends T>)visitor).visitCall(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CallContext call() throws RecognitionException {
		CallContext _localctx = new CallContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_call);
		try {
			setState(44);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__3:
				enterOuterAlt(_localctx, 1);
				{
				setState(42);
				mapCall();
				}
				break;
			case T__5:
				enterOuterAlt(_localctx, 2);
				{
				setState(43);
				filterCall();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class CallChainContext extends ParserRuleContext {
		public List<CallContext> call() {
			return getRuleContexts(CallContext.class);
		}
		public CallContext call(int i) {
			return getRuleContext(CallContext.class,i);
		}
		public CallChainContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_callChain; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CallChainListener ) ((CallChainListener)listener).enterCallChain(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CallChainListener ) ((CallChainListener)listener).exitCallChain(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CallChainVisitor ) return ((CallChainVisitor<? extends T>)visitor).visitCallChain(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CallChainContext callChain() throws RecognitionException {
		CallChainContext _localctx = new CallChainContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_callChain);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(46);
			call();
			setState(51);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__6) {
				{
				{
				setState(47);
				match(T__6);
				setState(48);
				call();
				}
				}
				setState(53);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\f9\4\2\t\2\4\3\t"+
		"\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\3\2\3\2\3\2\3\3\3\3"+
		"\5\3\30\n\3\3\4\3\4\3\4\3\4\3\4\3\4\3\5\3\5\3\5\5\5#\n\5\3\6\3\6\3\6\3"+
		"\6\3\7\3\7\3\7\3\7\3\b\3\b\5\b/\n\b\3\t\3\t\3\t\7\t\64\n\t\f\t\16\t\67"+
		"\13\t\3\t\2\2\n\2\4\6\b\n\f\16\20\2\2\2\65\2\22\3\2\2\2\4\27\3\2\2\2\6"+
		"\31\3\2\2\2\b\"\3\2\2\2\n$\3\2\2\2\f(\3\2\2\2\16.\3\2\2\2\20\60\3\2\2"+
		"\2\22\23\7\3\2\2\23\24\7\n\2\2\24\3\3\2\2\2\25\30\7\n\2\2\26\30\5\2\2"+
		"\2\27\25\3\2\2\2\27\26\3\2\2\2\30\5\3\2\2\2\31\32\7\4\2\2\32\33\5\b\5"+
		"\2\33\34\7\f\2\2\34\35\5\b\5\2\35\36\7\5\2\2\36\7\3\2\2\2\37#\7\13\2\2"+
		" #\5\4\3\2!#\5\6\4\2\"\37\3\2\2\2\" \3\2\2\2\"!\3\2\2\2#\t\3\2\2\2$%\7"+
		"\6\2\2%&\5\b\5\2&\'\7\7\2\2\'\13\3\2\2\2()\7\b\2\2)*\5\b\5\2*+\7\7\2\2"+
		"+\r\3\2\2\2,/\5\n\6\2-/\5\f\7\2.,\3\2\2\2.-\3\2\2\2/\17\3\2\2\2\60\65"+
		"\5\16\b\2\61\62\7\t\2\2\62\64\5\16\b\2\63\61\3\2\2\2\64\67\3\2\2\2\65"+
		"\63\3\2\2\2\65\66\3\2\2\2\66\21\3\2\2\2\67\65\3\2\2\2\6\27\".\65";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}