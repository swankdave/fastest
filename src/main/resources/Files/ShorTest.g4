grammar ShorTest;     // The name of the grammar is ShorTest

start
    : ws* ((rule|comment|setup) (eol|post_test_reset|pre_test_reset|EOF))+;

rule
    : WS* (test_name WS+)? constructor? WS* predicate WS* (rule_token WS* result)? WS* error_text?;

rule_token
    : RULETOKEN;

comment
    : COMMENTTOKEN anything_but_newline;

test_name
    : identifier;
constructor
    : ballanced_parenthesis_statement;
predicate
    : ballanced_parenthesis_statement anything_but_newline_or_ruletoken*;
result
    : parameter
    | ballanced_parenthesis_statement
    | set_method_statement
    | exception_method_statement;
error_text
    : ERRORTOKEN WS* anything_but_newline*;

set_method_statement
    : ballanced_parenthesis_statement set_keyword;

exception_method_statement
    : ballanced_parenthesis_statement EXCEPTION_KEYWORD;

ballanced_statement
    : ballanced_parenthesis_statement
    | ballanced_bracket_statement
    | ballanced_squigly_statement;

ballanced_squigly_statement
    : LC ws? parameterList ws? RC;

ballanced_bracket_statement
    : LB ws? parameterList ws? RB ;

ballanced_parenthesis_statement
    : LP ws? parameterList ws? RP;


parameterList
    : statement (COMMA statement)*
    | ;

statement:
    ws* parameter (ws* parameter)* ws*;

parameter
    : identifier
    | string
    | set_method_statement
    | ballanced_statement;

eol
    : NEWLINE WS*;
post_test_reset
    : eol NEWLINE WS*;
pre_test_reset
    : post_test_reset (NEWLINE WS*)+ ;

set_keyword
    : SET_KEYWORD ;
identifier
    : (CHAR_SEQ)+;
string
    : QUOTE (CHAR_SEQ|LB|RB|LP|RP|LC|RC|COMMA|WS|ERRORTOKEN)* QUOTE;
ws
    : (WS|NEWLINE)+;

anything_but_newline
    : (rule_token|anything_but_newline_or_ruletoken) ;

setup
    : anything_but_newline_or_ruletoken+;

anything_but_newline_or_ruletoken
    : anything_but_newline_or_ruletoken_or_space (anything_but_newline_or_ruletoken_or_space|WS)*;

anything_but_newline_or_ruletoken_or_space
    : (CHAR_SEQ|LB|RB|LP|RP|LC|RC|COMMA|QUOTE|ERRORTOKEN|FAKERULETOKEN|EXCEPTION_KEYWORD|SET_KEYWORD|'=') ;


RULETOKEN
    : '!'?'=>';

COMMENTTOKEN
    : '///' {_input.LA(-4) != '/' && _input.LA(1) != '/'}? ;

FAKERULETOKEN
    : '\'' RULETOKEN '\'';

SET_KEYWORD
    : 'SET' [A-Z]?;

EXCEPTION_KEYWORD
    : 'EX';

CHAR_SEQ
    : ~('('|')'|'['|']'|'{'|'}'|'"'|','|'\n'|' '|'#'|'='|'/')+;
LP
    : '(';
RP
    : ')';
LB
    : '[';
RB
    : ']';
LC
    : '{';
RC
    : '}';
QUOTE
    : '"';
COMMA
    : ',';
NEWLINE
    : '\n';
WS
    : ' ';

ERRORTOKEN
    : '//'
    | '#';