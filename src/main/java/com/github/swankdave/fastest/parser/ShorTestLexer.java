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
		T__0=1, ERRORTOKEN=2, FAKERULETOKEN=3, RULETOKEN=4, GEN_SET_KEYWORD=5, 
		SET_KEYWORD=6, EXCEPTION_KEYWORD=7, CHAR_SEQ=8, COMMA=9, LP=10, RP=11, 
		LB=12, RB=13, QUOTE=14, NEWLINE=15, WS=16;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"T__0", "ERRORTOKEN", "FAKERULETOKEN", "RULETOKEN", "GEN_SET_KEYWORD", 
			"SET_KEYWORD", "EXCEPTION_KEYWORD", "CHAR_SEQ", "COMMA", "LP", "RP", 
			"LB", "RB", "QUOTE", "NEWLINE", "WS"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'='", null, null, "'=>'", null, null, "'EX'", null, "','", "'('", 
			"')'", "'['", "']'", "'\"'", "'\\n'", "' '"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, "ERRORTOKEN", "FAKERULETOKEN", "RULETOKEN", "GEN_SET_KEYWORD", 
			"SET_KEYWORD", "EXCEPTION_KEYWORD", "CHAR_SEQ", "COMMA", "LP", "RP", 
			"LB", "RB", "QUOTE", "NEWLINE", "WS"
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

	public static final String _serializedATN =
		"\u0004\u0000\u0010Y\u0006\uffff\uffff\u0002\u0000\u0007\u0000\u0002\u0001"+
		"\u0007\u0001\u0002\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004"+
		"\u0007\u0004\u0002\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007"+
		"\u0007\u0007\u0002\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b"+
		"\u0007\u000b\u0002\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e\u0002"+
		"\u000f\u0007\u000f\u0001\u0000\u0001\u0000\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0003\u0001\'\b\u0001\u0001\u0002\u0001\u0002\u0001\u0002\u0001"+
		"\u0002\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0004\u0001\u0004\u0001"+
		"\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001"+
		"\u0004\u0003\u00049\b\u0004\u0001\u0005\u0001\u0005\u0001\u0005\u0001"+
		"\u0005\u0001\u0005\u0003\u0005@\b\u0005\u0001\u0006\u0001\u0006\u0001"+
		"\u0006\u0001\u0007\u0004\u0007F\b\u0007\u000b\u0007\f\u0007G\u0001\b\u0001"+
		"\b\u0001\t\u0001\t\u0001\n\u0001\n\u0001\u000b\u0001\u000b\u0001\f\u0001"+
		"\f\u0001\r\u0001\r\u0001\u000e\u0001\u000e\u0001\u000f\u0001\u000f\u0000"+
		"\u0000\u0010\u0001\u0001\u0003\u0002\u0005\u0003\u0007\u0004\t\u0005\u000b"+
		"\u0006\r\u0007\u000f\b\u0011\t\u0013\n\u0015\u000b\u0017\f\u0019\r\u001b"+
		"\u000e\u001d\u000f\u001f\u0010\u0001\u0000\u0002\u0001\u0000AZ\t\u0000"+
		"\n\n  \"#(),,//==[[]]\\\u0000\u0001\u0001\u0000\u0000\u0000\u0000\u0003"+
		"\u0001\u0000\u0000\u0000\u0000\u0005\u0001\u0000\u0000\u0000\u0000\u0007"+
		"\u0001\u0000\u0000\u0000\u0000\t\u0001\u0000\u0000\u0000\u0000\u000b\u0001"+
		"\u0000\u0000\u0000\u0000\r\u0001\u0000\u0000\u0000\u0000\u000f\u0001\u0000"+
		"\u0000\u0000\u0000\u0011\u0001\u0000\u0000\u0000\u0000\u0013\u0001\u0000"+
		"\u0000\u0000\u0000\u0015\u0001\u0000\u0000\u0000\u0000\u0017\u0001\u0000"+
		"\u0000\u0000\u0000\u0019\u0001\u0000\u0000\u0000\u0000\u001b\u0001\u0000"+
		"\u0000\u0000\u0000\u001d\u0001\u0000\u0000\u0000\u0000\u001f\u0001\u0000"+
		"\u0000\u0000\u0001!\u0001\u0000\u0000\u0000\u0003&\u0001\u0000\u0000\u0000"+
		"\u0005(\u0001\u0000\u0000\u0000\u0007,\u0001\u0000\u0000\u0000\t/\u0001"+
		"\u0000\u0000\u0000\u000b:\u0001\u0000\u0000\u0000\rA\u0001\u0000\u0000"+
		"\u0000\u000fE\u0001\u0000\u0000\u0000\u0011I\u0001\u0000\u0000\u0000\u0013"+
		"K\u0001\u0000\u0000\u0000\u0015M\u0001\u0000\u0000\u0000\u0017O\u0001"+
		"\u0000\u0000\u0000\u0019Q\u0001\u0000\u0000\u0000\u001bS\u0001\u0000\u0000"+
		"\u0000\u001dU\u0001\u0000\u0000\u0000\u001fW\u0001\u0000\u0000\u0000!"+
		"\"\u0005=\u0000\u0000\"\u0002\u0001\u0000\u0000\u0000#$\u0005/\u0000\u0000"+
		"$\'\u0005/\u0000\u0000%\'\u0005#\u0000\u0000&#\u0001\u0000\u0000\u0000"+
		"&%\u0001\u0000\u0000\u0000\'\u0004\u0001\u0000\u0000\u0000()\u0005\'\u0000"+
		"\u0000)*\u0003\u0007\u0003\u0000*+\u0005\'\u0000\u0000+\u0006\u0001\u0000"+
		"\u0000\u0000,-\u0005=\u0000\u0000-.\u0005>\u0000\u0000.\b\u0001\u0000"+
		"\u0000\u0000/0\u0005G\u0000\u000001\u0005E\u0000\u000012\u0005N\u0000"+
		"\u000023\u0005_\u0000\u000034\u0005S\u0000\u000045\u0005E\u0000\u0000"+
		"56\u0005T\u0000\u000068\u0001\u0000\u0000\u000079\u0007\u0000\u0000\u0000"+
		"87\u0001\u0000\u0000\u000089\u0001\u0000\u0000\u00009\n\u0001\u0000\u0000"+
		"\u0000:;\u0005S\u0000\u0000;<\u0005E\u0000\u0000<=\u0005T\u0000\u0000"+
		"=?\u0001\u0000\u0000\u0000>@\u0007\u0000\u0000\u0000?>\u0001\u0000\u0000"+
		"\u0000?@\u0001\u0000\u0000\u0000@\f\u0001\u0000\u0000\u0000AB\u0005E\u0000"+
		"\u0000BC\u0005X\u0000\u0000C\u000e\u0001\u0000\u0000\u0000DF\b\u0001\u0000"+
		"\u0000ED\u0001\u0000\u0000\u0000FG\u0001\u0000\u0000\u0000GE\u0001\u0000"+
		"\u0000\u0000GH\u0001\u0000\u0000\u0000H\u0010\u0001\u0000\u0000\u0000"+
		"IJ\u0005,\u0000\u0000J\u0012\u0001\u0000\u0000\u0000KL\u0005(\u0000\u0000"+
		"L\u0014\u0001\u0000\u0000\u0000MN\u0005)\u0000\u0000N\u0016\u0001\u0000"+
		"\u0000\u0000OP\u0005[\u0000\u0000P\u0018\u0001\u0000\u0000\u0000QR\u0005"+
		"]\u0000\u0000R\u001a\u0001\u0000\u0000\u0000ST\u0005\"\u0000\u0000T\u001c"+
		"\u0001\u0000\u0000\u0000UV\u0005\n\u0000\u0000V\u001e\u0001\u0000\u0000"+
		"\u0000WX\u0005 \u0000\u0000X \u0001\u0000\u0000\u0000\u0005\u0000&8?G"+
		"\u0000";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}