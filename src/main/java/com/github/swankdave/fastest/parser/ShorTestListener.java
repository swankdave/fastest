// Generated from /Users/aiukpw0/IdeaProjects/fastest/src/main/resources/Files/ShorTest.g4 by ANTLR 4.13.1
package com.github.swankdave.fastest.parser;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link ShorTestParser}.
 */
public interface ShorTestListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link ShorTestParser#start}.
	 * @param ctx the parse tree
	 */
	void enterStart(ShorTestParser.StartContext ctx);
	/**
	 * Exit a parse tree produced by {@link ShorTestParser#start}.
	 * @param ctx the parse tree
	 */
	void exitStart(ShorTestParser.StartContext ctx);
	/**
	 * Enter a parse tree produced by {@link ShorTestParser#rule}.
	 * @param ctx the parse tree
	 */
	void enterRule(ShorTestParser.RuleContext ctx);
	/**
	 * Exit a parse tree produced by {@link ShorTestParser#rule}.
	 * @param ctx the parse tree
	 */
	void exitRule(ShorTestParser.RuleContext ctx);
	/**
	 * Enter a parse tree produced by {@link ShorTestParser#rule_token}.
	 * @param ctx the parse tree
	 */
	void enterRule_token(ShorTestParser.Rule_tokenContext ctx);
	/**
	 * Exit a parse tree produced by {@link ShorTestParser#rule_token}.
	 * @param ctx the parse tree
	 */
	void exitRule_token(ShorTestParser.Rule_tokenContext ctx);
	/**
	 * Enter a parse tree produced by {@link ShorTestParser#comment}.
	 * @param ctx the parse tree
	 */
	void enterComment(ShorTestParser.CommentContext ctx);
	/**
	 * Exit a parse tree produced by {@link ShorTestParser#comment}.
	 * @param ctx the parse tree
	 */
	void exitComment(ShorTestParser.CommentContext ctx);
	/**
	 * Enter a parse tree produced by {@link ShorTestParser#test_name}.
	 * @param ctx the parse tree
	 */
	void enterTest_name(ShorTestParser.Test_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link ShorTestParser#test_name}.
	 * @param ctx the parse tree
	 */
	void exitTest_name(ShorTestParser.Test_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link ShorTestParser#constructor}.
	 * @param ctx the parse tree
	 */
	void enterConstructor(ShorTestParser.ConstructorContext ctx);
	/**
	 * Exit a parse tree produced by {@link ShorTestParser#constructor}.
	 * @param ctx the parse tree
	 */
	void exitConstructor(ShorTestParser.ConstructorContext ctx);
	/**
	 * Enter a parse tree produced by {@link ShorTestParser#predicate}.
	 * @param ctx the parse tree
	 */
	void enterPredicate(ShorTestParser.PredicateContext ctx);
	/**
	 * Exit a parse tree produced by {@link ShorTestParser#predicate}.
	 * @param ctx the parse tree
	 */
	void exitPredicate(ShorTestParser.PredicateContext ctx);
	/**
	 * Enter a parse tree produced by {@link ShorTestParser#result}.
	 * @param ctx the parse tree
	 */
	void enterResult(ShorTestParser.ResultContext ctx);
	/**
	 * Exit a parse tree produced by {@link ShorTestParser#result}.
	 * @param ctx the parse tree
	 */
	void exitResult(ShorTestParser.ResultContext ctx);
	/**
	 * Enter a parse tree produced by {@link ShorTestParser#error_text}.
	 * @param ctx the parse tree
	 */
	void enterError_text(ShorTestParser.Error_textContext ctx);
	/**
	 * Exit a parse tree produced by {@link ShorTestParser#error_text}.
	 * @param ctx the parse tree
	 */
	void exitError_text(ShorTestParser.Error_textContext ctx);
	/**
	 * Enter a parse tree produced by {@link ShorTestParser#set_method_statement}.
	 * @param ctx the parse tree
	 */
	void enterSet_method_statement(ShorTestParser.Set_method_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ShorTestParser#set_method_statement}.
	 * @param ctx the parse tree
	 */
	void exitSet_method_statement(ShorTestParser.Set_method_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ShorTestParser#exception_method_statement}.
	 * @param ctx the parse tree
	 */
	void enterException_method_statement(ShorTestParser.Exception_method_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ShorTestParser#exception_method_statement}.
	 * @param ctx the parse tree
	 */
	void exitException_method_statement(ShorTestParser.Exception_method_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ShorTestParser#ballanced_statement}.
	 * @param ctx the parse tree
	 */
	void enterBallanced_statement(ShorTestParser.Ballanced_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ShorTestParser#ballanced_statement}.
	 * @param ctx the parse tree
	 */
	void exitBallanced_statement(ShorTestParser.Ballanced_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ShorTestParser#ballanced_squigly_statement}.
	 * @param ctx the parse tree
	 */
	void enterBallanced_squigly_statement(ShorTestParser.Ballanced_squigly_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ShorTestParser#ballanced_squigly_statement}.
	 * @param ctx the parse tree
	 */
	void exitBallanced_squigly_statement(ShorTestParser.Ballanced_squigly_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ShorTestParser#ballanced_bracket_statement}.
	 * @param ctx the parse tree
	 */
	void enterBallanced_bracket_statement(ShorTestParser.Ballanced_bracket_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ShorTestParser#ballanced_bracket_statement}.
	 * @param ctx the parse tree
	 */
	void exitBallanced_bracket_statement(ShorTestParser.Ballanced_bracket_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ShorTestParser#ballanced_parenthesis_statement}.
	 * @param ctx the parse tree
	 */
	void enterBallanced_parenthesis_statement(ShorTestParser.Ballanced_parenthesis_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ShorTestParser#ballanced_parenthesis_statement}.
	 * @param ctx the parse tree
	 */
	void exitBallanced_parenthesis_statement(ShorTestParser.Ballanced_parenthesis_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ShorTestParser#parameterList}.
	 * @param ctx the parse tree
	 */
	void enterParameterList(ShorTestParser.ParameterListContext ctx);
	/**
	 * Exit a parse tree produced by {@link ShorTestParser#parameterList}.
	 * @param ctx the parse tree
	 */
	void exitParameterList(ShorTestParser.ParameterListContext ctx);
	/**
	 * Enter a parse tree produced by {@link ShorTestParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatement(ShorTestParser.StatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ShorTestParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatement(ShorTestParser.StatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ShorTestParser#parameter}.
	 * @param ctx the parse tree
	 */
	void enterParameter(ShorTestParser.ParameterContext ctx);
	/**
	 * Exit a parse tree produced by {@link ShorTestParser#parameter}.
	 * @param ctx the parse tree
	 */
	void exitParameter(ShorTestParser.ParameterContext ctx);
	/**
	 * Enter a parse tree produced by {@link ShorTestParser#eol}.
	 * @param ctx the parse tree
	 */
	void enterEol(ShorTestParser.EolContext ctx);
	/**
	 * Exit a parse tree produced by {@link ShorTestParser#eol}.
	 * @param ctx the parse tree
	 */
	void exitEol(ShorTestParser.EolContext ctx);
	/**
	 * Enter a parse tree produced by {@link ShorTestParser#post_test_reset}.
	 * @param ctx the parse tree
	 */
	void enterPost_test_reset(ShorTestParser.Post_test_resetContext ctx);
	/**
	 * Exit a parse tree produced by {@link ShorTestParser#post_test_reset}.
	 * @param ctx the parse tree
	 */
	void exitPost_test_reset(ShorTestParser.Post_test_resetContext ctx);
	/**
	 * Enter a parse tree produced by {@link ShorTestParser#pre_test_reset}.
	 * @param ctx the parse tree
	 */
	void enterPre_test_reset(ShorTestParser.Pre_test_resetContext ctx);
	/**
	 * Exit a parse tree produced by {@link ShorTestParser#pre_test_reset}.
	 * @param ctx the parse tree
	 */
	void exitPre_test_reset(ShorTestParser.Pre_test_resetContext ctx);
	/**
	 * Enter a parse tree produced by {@link ShorTestParser#set_keyword}.
	 * @param ctx the parse tree
	 */
	void enterSet_keyword(ShorTestParser.Set_keywordContext ctx);
	/**
	 * Exit a parse tree produced by {@link ShorTestParser#set_keyword}.
	 * @param ctx the parse tree
	 */
	void exitSet_keyword(ShorTestParser.Set_keywordContext ctx);
	/**
	 * Enter a parse tree produced by {@link ShorTestParser#identifier}.
	 * @param ctx the parse tree
	 */
	void enterIdentifier(ShorTestParser.IdentifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link ShorTestParser#identifier}.
	 * @param ctx the parse tree
	 */
	void exitIdentifier(ShorTestParser.IdentifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link ShorTestParser#string}.
	 * @param ctx the parse tree
	 */
	void enterString(ShorTestParser.StringContext ctx);
	/**
	 * Exit a parse tree produced by {@link ShorTestParser#string}.
	 * @param ctx the parse tree
	 */
	void exitString(ShorTestParser.StringContext ctx);
	/**
	 * Enter a parse tree produced by {@link ShorTestParser#ws}.
	 * @param ctx the parse tree
	 */
	void enterWs(ShorTestParser.WsContext ctx);
	/**
	 * Exit a parse tree produced by {@link ShorTestParser#ws}.
	 * @param ctx the parse tree
	 */
	void exitWs(ShorTestParser.WsContext ctx);
	/**
	 * Enter a parse tree produced by {@link ShorTestParser#anything_but_newline}.
	 * @param ctx the parse tree
	 */
	void enterAnything_but_newline(ShorTestParser.Anything_but_newlineContext ctx);
	/**
	 * Exit a parse tree produced by {@link ShorTestParser#anything_but_newline}.
	 * @param ctx the parse tree
	 */
	void exitAnything_but_newline(ShorTestParser.Anything_but_newlineContext ctx);
	/**
	 * Enter a parse tree produced by {@link ShorTestParser#setup}.
	 * @param ctx the parse tree
	 */
	void enterSetup(ShorTestParser.SetupContext ctx);
	/**
	 * Exit a parse tree produced by {@link ShorTestParser#setup}.
	 * @param ctx the parse tree
	 */
	void exitSetup(ShorTestParser.SetupContext ctx);
	/**
	 * Enter a parse tree produced by {@link ShorTestParser#anything_but_newline_or_ruletoken}.
	 * @param ctx the parse tree
	 */
	void enterAnything_but_newline_or_ruletoken(ShorTestParser.Anything_but_newline_or_ruletokenContext ctx);
	/**
	 * Exit a parse tree produced by {@link ShorTestParser#anything_but_newline_or_ruletoken}.
	 * @param ctx the parse tree
	 */
	void exitAnything_but_newline_or_ruletoken(ShorTestParser.Anything_but_newline_or_ruletokenContext ctx);
	/**
	 * Enter a parse tree produced by {@link ShorTestParser#anything_but_newline_or_ruletoken_or_space}.
	 * @param ctx the parse tree
	 */
	void enterAnything_but_newline_or_ruletoken_or_space(ShorTestParser.Anything_but_newline_or_ruletoken_or_spaceContext ctx);
	/**
	 * Exit a parse tree produced by {@link ShorTestParser#anything_but_newline_or_ruletoken_or_space}.
	 * @param ctx the parse tree
	 */
	void exitAnything_but_newline_or_ruletoken_or_space(ShorTestParser.Anything_but_newline_or_ruletoken_or_spaceContext ctx);
}