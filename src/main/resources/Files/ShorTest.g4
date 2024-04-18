grammar ShorTest;     // The name of the grammar is ShorTest

start
    : NEWLINE* ((setup|rule) (test_reset|post_test_reset|setup_reset|EOF))+;

setup
    : anything_but_newline_or_ruletoken+;

rule
    : WS* test_name? WS* constructor? WS* predicate WS* RULETOKEN WS* result WS* error_text?;

test_name:
    identifier;
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
    : ERRORTOKEN anything_but_newline*;

set_method_statement
    : ballanced_parenthesis_statement set_keyword;
exception_method_statement
    : ballanced_parenthesis_statement EXCEPTION_KEYWORD;
method_statement
    : identifier ballanced_parenthesis_statement;

ballanced_parenthesis_statement :
    LP ws? parameterList ws? RP;


parameterList
    : parameter ws? (COMMA ws? parameter ws?)*
    | ;

parameter
    : identifier
    | string
    | set_method_statement
    | method_statement
    | ballanced_parenthesis_statement;

test_reset
    : NEWLINE;
post_test_reset
    : test_reset NEWLINE;
setup_reset
    : post_test_reset NEWLINE+;


set_keyword
    : SET_KEYWORD
    | GEN_SET_KEYWORD;

identifier
    : (CHAR_SEQ)+;
string
    : QUOTE (CHAR_SEQ|LB|RB|LP|RP|COMMA|ws)* QUOTE;
ws
    : (WS|NEWLINE)+;

anything_but_newline
    : (RULETOKEN|anything_but_newline_or_ruletoken) ;

anything_but_newline_or_ruletoken
    : (CHAR_SEQ|COMMA|LP|RP|LB|RB|QUOTE|WS|ERRORTOKEN|FAKERULETOKEN|GEN_SET_KEYWORD|EXCEPTION_KEYWORD|SET_KEYWORD|'='|ERRORTOKEN) ;

ERRORTOKEN
    : '//'
    | '#';
FAKERULETOKEN
    : '\'' RULETOKEN '\'';
RULETOKEN
    : '=>';
GEN_SET_KEYWORD
    : 'GEN_SET' [A-Z]?;
SET_KEYWORD
    : 'SET' [A-Z]?;
EXCEPTION_KEYWORD
    : 'EX';
CHAR_SEQ
    : ~('('|')'|'['|']'|'"'|'\n'|' '|'='|','|'/'|'#')+;

COMMA
    : ',';
LP
    : '(';
RP
    : ')';
LB
    : '[';
RB
    : ']';
QUOTE
    : '"';
NEWLINE
    : '\n';
WS
    : ' ';