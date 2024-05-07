// Generated from /Users/aiukpw0/IdeaProjects/fastest/src/main/resources/Files/ShorTest.g4 by ANTLR 4.13.1
package com.github.swankdave.fastest.parser;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue", "this-escape"})
public class ShorTestLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.13.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, RULETOKEN=2, COMMENTTOKEN=3, FAKERULETOKEN=4, SET_KEYWORD=5, EXCEPTION_KEYWORD=6, 
		CHAR_SEQ=7, LP=8, RP=9, LB=10, RB=11, LC=12, RC=13, QUOTE=14, COMMA=15, 
		NEWLINE=16, WS=17, ERRORTOKEN=18;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"T__0", "RULETOKEN", "COMMENTTOKEN", "FAKERULETOKEN", "SET_KEYWORD", 
			"EXCEPTION_KEYWORD", "CHAR_SEQ", "LP", "RP", "LB", "RB", "LC", "RC", 
			"QUOTE", "COMMA", "NEWLINE", "WS", "ERRORTOKEN"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'='", null, "'///'", null, null, "'EX'", null, "'('", "')'", "'['", 
			"']'", "'{'", "'}'", "'\"'", "','", "'\\n'", "' '"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, "RULETOKEN", "COMMENTTOKEN", "FAKERULETOKEN", "SET_KEYWORD", 
			"EXCEPTION_KEYWORD", "CHAR_SEQ", "LP", "RP", "LB", "RB", "LC", "RC", 
			"QUOTE", "COMMA", "NEWLINE", "WS", "ERRORTOKEN"
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


	public ShorTestLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "ShorTest.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	@Override
	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 2:
			return COMMENTTOKEN_sempred((RuleContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean COMMENTTOKEN_sempred(RuleContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return _input.LA(-4) != '/' && _input.LA(1) != '/';
		}
		return true;
	}

	public static final String _serializedATN =
		"\u0004\u0000\u0012_\u0006\uffff\uffff\u0002\u0000\u0007\u0000\u0002\u0001"+
		"\u0007\u0001\u0002\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004"+
		"\u0007\u0004\u0002\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007"+
		"\u0007\u0007\u0002\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b"+
		"\u0007\u000b\u0002\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e\u0002"+
		"\u000f\u0007\u000f\u0002\u0010\u0007\u0010\u0002\u0011\u0007\u0011\u0001"+
		"\u0000\u0001\u0000\u0001\u0001\u0003\u0001)\b\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001"+
		"\u0002\u0001\u0002\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001"+
		"\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0003\u0004=\b"+
		"\u0004\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0006\u0004\u0006C\b"+
		"\u0006\u000b\u0006\f\u0006D\u0001\u0007\u0001\u0007\u0001\b\u0001\b\u0001"+
		"\t\u0001\t\u0001\n\u0001\n\u0001\u000b\u0001\u000b\u0001\f\u0001\f\u0001"+
		"\r\u0001\r\u0001\u000e\u0001\u000e\u0001\u000f\u0001\u000f\u0001\u0010"+
		"\u0001\u0010\u0001\u0011\u0001\u0011\u0001\u0011\u0003\u0011^\b\u0011"+
		"\u0000\u0000\u0012\u0001\u0001\u0003\u0002\u0005\u0003\u0007\u0004\t\u0005"+
		"\u000b\u0006\r\u0007\u000f\b\u0011\t\u0013\n\u0015\u000b\u0017\f\u0019"+
		"\r\u001b\u000e\u001d\u000f\u001f\u0010!\u0011#\u0012\u0001\u0000\u0002"+
		"\u0001\u0000AZ\u000b\u0000\n\n  \"#(),,//==[[]]{{}}b\u0000\u0001\u0001"+
		"\u0000\u0000\u0000\u0000\u0003\u0001\u0000\u0000\u0000\u0000\u0005\u0001"+
		"\u0000\u0000\u0000\u0000\u0007\u0001\u0000\u0000\u0000\u0000\t\u0001\u0000"+
		"\u0000\u0000\u0000\u000b\u0001\u0000\u0000\u0000\u0000\r\u0001\u0000\u0000"+
		"\u0000\u0000\u000f\u0001\u0000\u0000\u0000\u0000\u0011\u0001\u0000\u0000"+
		"\u0000\u0000\u0013\u0001\u0000\u0000\u0000\u0000\u0015\u0001\u0000\u0000"+
		"\u0000\u0000\u0017\u0001\u0000\u0000\u0000\u0000\u0019\u0001\u0000\u0000"+
		"\u0000\u0000\u001b\u0001\u0000\u0000\u0000\u0000\u001d\u0001\u0000\u0000"+
		"\u0000\u0000\u001f\u0001\u0000\u0000\u0000\u0000!\u0001\u0000\u0000\u0000"+
		"\u0000#\u0001\u0000\u0000\u0000\u0001%\u0001\u0000\u0000\u0000\u0003("+
		"\u0001\u0000\u0000\u0000\u0005-\u0001\u0000\u0000\u0000\u00073\u0001\u0000"+
		"\u0000\u0000\t7\u0001\u0000\u0000\u0000\u000b>\u0001\u0000\u0000\u0000"+
		"\rB\u0001\u0000\u0000\u0000\u000fF\u0001\u0000\u0000\u0000\u0011H\u0001"+
		"\u0000\u0000\u0000\u0013J\u0001\u0000\u0000\u0000\u0015L\u0001\u0000\u0000"+
		"\u0000\u0017N\u0001\u0000\u0000\u0000\u0019P\u0001\u0000\u0000\u0000\u001b"+
		"R\u0001\u0000\u0000\u0000\u001dT\u0001\u0000\u0000\u0000\u001fV\u0001"+
		"\u0000\u0000\u0000!X\u0001\u0000\u0000\u0000#]\u0001\u0000\u0000\u0000"+
		"%&\u0005=\u0000\u0000&\u0002\u0001\u0000\u0000\u0000\')\u0005!\u0000\u0000"+
		"(\'\u0001\u0000\u0000\u0000()\u0001\u0000\u0000\u0000)*\u0001\u0000\u0000"+
		"\u0000*+\u0005=\u0000\u0000+,\u0005>\u0000\u0000,\u0004\u0001\u0000\u0000"+
		"\u0000-.\u0005/\u0000\u0000./\u0005/\u0000\u0000/0\u0005/\u0000\u0000"+
		"01\u0001\u0000\u0000\u000012\u0004\u0002\u0000\u00002\u0006\u0001\u0000"+
		"\u0000\u000034\u0005\'\u0000\u000045\u0003\u0003\u0001\u000056\u0005\'"+
		"\u0000\u00006\b\u0001\u0000\u0000\u000078\u0005S\u0000\u000089\u0005E"+
		"\u0000\u00009:\u0005T\u0000\u0000:<\u0001\u0000\u0000\u0000;=\u0007\u0000"+
		"\u0000\u0000<;\u0001\u0000\u0000\u0000<=\u0001\u0000\u0000\u0000=\n\u0001"+
		"\u0000\u0000\u0000>?\u0005E\u0000\u0000?@\u0005X\u0000\u0000@\f\u0001"+
		"\u0000\u0000\u0000AC\b\u0001\u0000\u0000BA\u0001\u0000\u0000\u0000CD\u0001"+
		"\u0000\u0000\u0000DB\u0001\u0000\u0000\u0000DE\u0001\u0000\u0000\u0000"+
		"E\u000e\u0001\u0000\u0000\u0000FG\u0005(\u0000\u0000G\u0010\u0001\u0000"+
		"\u0000\u0000HI\u0005)\u0000\u0000I\u0012\u0001\u0000\u0000\u0000JK\u0005"+
		"[\u0000\u0000K\u0014\u0001\u0000\u0000\u0000LM\u0005]\u0000\u0000M\u0016"+
		"\u0001\u0000\u0000\u0000NO\u0005{\u0000\u0000O\u0018\u0001\u0000\u0000"+
		"\u0000PQ\u0005}\u0000\u0000Q\u001a\u0001\u0000\u0000\u0000RS\u0005\"\u0000"+
		"\u0000S\u001c\u0001\u0000\u0000\u0000TU\u0005,\u0000\u0000U\u001e\u0001"+
		"\u0000\u0000\u0000VW\u0005\n\u0000\u0000W \u0001\u0000\u0000\u0000XY\u0005"+
		" \u0000\u0000Y\"\u0001\u0000\u0000\u0000Z[\u0005/\u0000\u0000[^\u0005"+
		"/\u0000\u0000\\^\u0005#\u0000\u0000]Z\u0001\u0000\u0000\u0000]\\\u0001"+
		"\u0000\u0000\u0000^$\u0001\u0000\u0000\u0000\u0005\u0000(<D]\u0000";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}