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
		EOL=1, RULETOKEN=2, COMMENTTOKEN=3, FAKERULETOKEN=4, SET_KEYWORD=5, EXCEPTION_KEYWORD=6, 
		ESCAPEDQUOTE=7, ESCAPE=8, CHAR_SEQ=9, CHAR=10, NON_RESERVED_CHARACTER=11, 
		FS=12, LP=13, RP=14, LB=15, RB=16, LC=17, RC=18, EQ=19, QUOTE=20, COMMA=21, 
		NEWLINE=22, COLIN=23, STAR=24, WS=25;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"EOL", "RULETOKEN", "COMMENTTOKEN", "FAKERULETOKEN", "SET_KEYWORD", "EXCEPTION_KEYWORD", 
			"ESCAPEDQUOTE", "ESCAPE", "CHAR_SEQ", "CHAR", "NON_RESERVED_CHARACTER", 
			"FS", "LP", "RP", "LB", "RB", "LC", "RC", "EQ", "QUOTE", "COMMA", "NEWLINE", 
			"COLIN", "STAR", "WS"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, null, null, null, null, null, "'EX'", null, "'\\'", null, null, 
			null, "'/'", "'('", "')'", "'['", "']'", "'{'", "'}'", "'='", "'\"'", 
			null, "'\\n'", "':'", "'*'", "' '"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "EOL", "RULETOKEN", "COMMENTTOKEN", "FAKERULETOKEN", "SET_KEYWORD", 
			"EXCEPTION_KEYWORD", "ESCAPEDQUOTE", "ESCAPE", "CHAR_SEQ", "CHAR", "NON_RESERVED_CHARACTER", 
			"FS", "LP", "RP", "LB", "RB", "LC", "RC", "EQ", "QUOTE", "COMMA", "NEWLINE", 
			"COLIN", "STAR", "WS"
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
		"\u0004\u0000\u0019\u0092\u0006\uffff\uffff\u0002\u0000\u0007\u0000\u0002"+
		"\u0001\u0007\u0001\u0002\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002"+
		"\u0004\u0007\u0004\u0002\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002"+
		"\u0007\u0007\u0007\u0002\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002"+
		"\u000b\u0007\u000b\u0002\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e"+
		"\u0002\u000f\u0007\u000f\u0002\u0010\u0007\u0010\u0002\u0011\u0007\u0011"+
		"\u0002\u0012\u0007\u0012\u0002\u0013\u0007\u0013\u0002\u0014\u0007\u0014"+
		"\u0002\u0015\u0007\u0015\u0002\u0016\u0007\u0016\u0002\u0017\u0007\u0017"+
		"\u0002\u0018\u0007\u0018\u0001\u0000\u0005\u00005\b\u0000\n\u0000\f\u0000"+
		"8\t\u0000\u0001\u0000\u0001\u0000\u0005\u0000<\b\u0000\n\u0000\f\u0000"+
		"?\t\u0000\u0001\u0001\u0003\u0001B\b\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0003"+
		"\u0002L\b\u0002\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001"+
		"\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0003\u0004W\b"+
		"\u0004\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0006\u0001\u0006\u0001"+
		"\u0006\u0001\u0007\u0001\u0007\u0001\b\u0004\bb\b\b\u000b\b\f\bc\u0001"+
		"\t\u0001\t\u0003\th\b\t\u0001\n\u0001\n\u0001\u000b\u0001\u000b\u0001"+
		"\f\u0001\f\u0001\r\u0001\r\u0001\u000e\u0001\u000e\u0001\u000f\u0001\u000f"+
		"\u0001\u0010\u0001\u0010\u0001\u0011\u0001\u0011\u0001\u0012\u0001\u0012"+
		"\u0001\u0013\u0001\u0013\u0001\u0014\u0005\u0014\u007f\b\u0014\n\u0014"+
		"\f\u0014\u0082\t\u0014\u0001\u0014\u0001\u0014\u0005\u0014\u0086\b\u0014"+
		"\n\u0014\f\u0014\u0089\t\u0014\u0001\u0015\u0001\u0015\u0001\u0016\u0001"+
		"\u0016\u0001\u0017\u0001\u0017\u0001\u0018\u0001\u0018\u0000\u0000\u0019"+
		"\u0001\u0001\u0003\u0002\u0005\u0003\u0007\u0004\t\u0005\u000b\u0006\r"+
		"\u0007\u000f\b\u0011\t\u0013\n\u0015\u000b\u0017\f\u0019\r\u001b\u000e"+
		"\u001d\u000f\u001f\u0010!\u0011#\u0012%\u0013\'\u0014)\u0015+\u0016-\u0017"+
		"/\u00181\u0019\u0001\u0000\u0002\u0001\u0000AZ\u000b\u0000\n\n  \"#()"+
		",,//::==[]{{}}\u009a\u0000\u0001\u0001\u0000\u0000\u0000\u0000\u0003\u0001"+
		"\u0000\u0000\u0000\u0000\u0005\u0001\u0000\u0000\u0000\u0000\u0007\u0001"+
		"\u0000\u0000\u0000\u0000\t\u0001\u0000\u0000\u0000\u0000\u000b\u0001\u0000"+
		"\u0000\u0000\u0000\r\u0001\u0000\u0000\u0000\u0000\u000f\u0001\u0000\u0000"+
		"\u0000\u0000\u0011\u0001\u0000\u0000\u0000\u0000\u0013\u0001\u0000\u0000"+
		"\u0000\u0000\u0015\u0001\u0000\u0000\u0000\u0000\u0017\u0001\u0000\u0000"+
		"\u0000\u0000\u0019\u0001\u0000\u0000\u0000\u0000\u001b\u0001\u0000\u0000"+
		"\u0000\u0000\u001d\u0001\u0000\u0000\u0000\u0000\u001f\u0001\u0000\u0000"+
		"\u0000\u0000!\u0001\u0000\u0000\u0000\u0000#\u0001\u0000\u0000\u0000\u0000"+
		"%\u0001\u0000\u0000\u0000\u0000\'\u0001\u0000\u0000\u0000\u0000)\u0001"+
		"\u0000\u0000\u0000\u0000+\u0001\u0000\u0000\u0000\u0000-\u0001\u0000\u0000"+
		"\u0000\u0000/\u0001\u0000\u0000\u0000\u00001\u0001\u0000\u0000\u0000\u0001"+
		"6\u0001\u0000\u0000\u0000\u0003A\u0001\u0000\u0000\u0000\u0005K\u0001"+
		"\u0000\u0000\u0000\u0007M\u0001\u0000\u0000\u0000\tQ\u0001\u0000\u0000"+
		"\u0000\u000bX\u0001\u0000\u0000\u0000\r[\u0001\u0000\u0000\u0000\u000f"+
		"^\u0001\u0000\u0000\u0000\u0011a\u0001\u0000\u0000\u0000\u0013g\u0001"+
		"\u0000\u0000\u0000\u0015i\u0001\u0000\u0000\u0000\u0017k\u0001\u0000\u0000"+
		"\u0000\u0019m\u0001\u0000\u0000\u0000\u001bo\u0001\u0000\u0000\u0000\u001d"+
		"q\u0001\u0000\u0000\u0000\u001fs\u0001\u0000\u0000\u0000!u\u0001\u0000"+
		"\u0000\u0000#w\u0001\u0000\u0000\u0000%y\u0001\u0000\u0000\u0000\'{\u0001"+
		"\u0000\u0000\u0000)\u0080\u0001\u0000\u0000\u0000+\u008a\u0001\u0000\u0000"+
		"\u0000-\u008c\u0001\u0000\u0000\u0000/\u008e\u0001\u0000\u0000\u00001"+
		"\u0090\u0001\u0000\u0000\u000035\u00031\u0018\u000043\u0001\u0000\u0000"+
		"\u000058\u0001\u0000\u0000\u000064\u0001\u0000\u0000\u000067\u0001\u0000"+
		"\u0000\u000079\u0001\u0000\u0000\u000086\u0001\u0000\u0000\u00009=\u0003"+
		"+\u0015\u0000:<\u00031\u0018\u0000;:\u0001\u0000\u0000\u0000<?\u0001\u0000"+
		"\u0000\u0000=;\u0001\u0000\u0000\u0000=>\u0001\u0000\u0000\u0000>\u0002"+
		"\u0001\u0000\u0000\u0000?=\u0001\u0000\u0000\u0000@B\u0005!\u0000\u0000"+
		"A@\u0001\u0000\u0000\u0000AB\u0001\u0000\u0000\u0000BC\u0001\u0000\u0000"+
		"\u0000CD\u0005=\u0000\u0000DE\u0005>\u0000\u0000E\u0004\u0001\u0000\u0000"+
		"\u0000FG\u0003\u0017\u000b\u0000GH\u0003\u0017\u000b\u0000HI\u0003\u0017"+
		"\u000b\u0000IL\u0001\u0000\u0000\u0000JL\u0005#\u0000\u0000KF\u0001\u0000"+
		"\u0000\u0000KJ\u0001\u0000\u0000\u0000L\u0006\u0001\u0000\u0000\u0000"+
		"MN\u0005\'\u0000\u0000NO\u0003\u0003\u0001\u0000OP\u0005\'\u0000\u0000"+
		"P\b\u0001\u0000\u0000\u0000QR\u0005S\u0000\u0000RS\u0005E\u0000\u0000"+
		"ST\u0005T\u0000\u0000TV\u0001\u0000\u0000\u0000UW\u0007\u0000\u0000\u0000"+
		"VU\u0001\u0000\u0000\u0000VW\u0001\u0000\u0000\u0000W\n\u0001\u0000\u0000"+
		"\u0000XY\u0005E\u0000\u0000YZ\u0005X\u0000\u0000Z\f\u0001\u0000\u0000"+
		"\u0000[\\\u0003\u000f\u0007\u0000\\]\u0005\"\u0000\u0000]\u000e\u0001"+
		"\u0000\u0000\u0000^_\u0005\\\u0000\u0000_\u0010\u0001\u0000\u0000\u0000"+
		"`b\u0003\u0013\t\u0000a`\u0001\u0000\u0000\u0000bc\u0001\u0000\u0000\u0000"+
		"ca\u0001\u0000\u0000\u0000cd\u0001\u0000\u0000\u0000d\u0012\u0001\u0000"+
		"\u0000\u0000eh\u0003\r\u0006\u0000fh\u0003\u0015\n\u0000ge\u0001\u0000"+
		"\u0000\u0000gf\u0001\u0000\u0000\u0000h\u0014\u0001\u0000\u0000\u0000"+
		"ij\b\u0001\u0000\u0000j\u0016\u0001\u0000\u0000\u0000kl\u0005/\u0000\u0000"+
		"l\u0018\u0001\u0000\u0000\u0000mn\u0005(\u0000\u0000n\u001a\u0001\u0000"+
		"\u0000\u0000op\u0005)\u0000\u0000p\u001c\u0001\u0000\u0000\u0000qr\u0005"+
		"[\u0000\u0000r\u001e\u0001\u0000\u0000\u0000st\u0005]\u0000\u0000t \u0001"+
		"\u0000\u0000\u0000uv\u0005{\u0000\u0000v\"\u0001\u0000\u0000\u0000wx\u0005"+
		"}\u0000\u0000x$\u0001\u0000\u0000\u0000yz\u0005=\u0000\u0000z&\u0001\u0000"+
		"\u0000\u0000{|\u0005\"\u0000\u0000|(\u0001\u0000\u0000\u0000}\u007f\u0003"+
		"1\u0018\u0000~}\u0001\u0000\u0000\u0000\u007f\u0082\u0001\u0000\u0000"+
		"\u0000\u0080~\u0001\u0000\u0000\u0000\u0080\u0081\u0001\u0000\u0000\u0000"+
		"\u0081\u0083\u0001\u0000\u0000\u0000\u0082\u0080\u0001\u0000\u0000\u0000"+
		"\u0083\u0087\u0005,\u0000\u0000\u0084\u0086\u00031\u0018\u0000\u0085\u0084"+
		"\u0001\u0000\u0000\u0000\u0086\u0089\u0001\u0000\u0000\u0000\u0087\u0085"+
		"\u0001\u0000\u0000\u0000\u0087\u0088\u0001\u0000\u0000\u0000\u0088*\u0001"+
		"\u0000\u0000\u0000\u0089\u0087\u0001\u0000\u0000\u0000\u008a\u008b\u0005"+
		"\n\u0000\u0000\u008b,\u0001\u0000\u0000\u0000\u008c\u008d\u0005:\u0000"+
		"\u0000\u008d.\u0001\u0000\u0000\u0000\u008e\u008f\u0005*\u0000\u0000\u008f"+
		"0\u0001\u0000\u0000\u0000\u0090\u0091\u0005 \u0000\u0000\u00912\u0001"+
		"\u0000\u0000\u0000\n\u00006=AKVcg\u0080\u0087\u0000";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}