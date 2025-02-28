grammar ShorTest;     // The name of the grammar is ShorTest

start
    : ws* ( WS* (parameter_set|truth_list|rule|comment|setup) WS* comment? WS* (rule_seperator|EOF))+ EOF;

comment
    : COMMENTTOKEN everything_until_newline ;

setup
    : everything_until_newline ;

//testlist based tests
parameter_set
    : (identifier ws* COLIN)? ws* LC ws? key_value_list ws? RC;

key_value_list
    : key_value_pair (COMMA key_value_pair)*;

key_value_pair
    : ws* identifier ws* COLIN ws* statement ws*;

truth_list
    : WS* LP ws* ((rule|comment|setup) rule_seperator WS*)* rule (WS* rule_seperator (rule|comment|setup))* RP;

//rule based tests
rule
    : WS* (test_name WS+)? constructor? predicate (WS* rule_token WS* result)? WS* (COMMENTTOKEN error_text)?;

test_name
    : identifier;

constructor
    : WS* LP WS* (parameterList|programming_contents) WS* RP WS*;

predicate
    : WS* LP WS* (parameterList|programming_contents) WS* RP programming_contents*;

rule_token
    : RULETOKEN;

result
    : exception_method_statement
    | set_method_statement
    | programming_contents;

error_text
    : everything_until_newline;

rule_seperator
    : (EOL|post_test_reset|pre_test_reset);

//keyword based Statements
exception_method_statement
    : LP programming_contents RP EXCEPTION_KEYWORD;

set_method_statement
    : LP ws? parameterList ws? RP set_keyword;

//structured parameters
parameterList
    : statement (COMMA statement)*
    | ;

statement
    : set_method_statement
    | enclosed_statement
    | string
    | identifier
    | programming_contents;

enclosed_statement
    : LC ws? statement* ws? RC
    | LB ws? statement* ws? RB
    | LP ws? statement* ws? RP ;

//programming contents
programming_contents
    : WS* programming_content (WS programming_content)*;

programming_content
    : (set_method_statement|programming_content_ballanced_statement|(CHAR_SEQ))+ ;

programming_content_ballanced_statement
    : LC ws? programming_contents_with_comma ws? RC
    | LB ws? programming_contents_with_comma ws? RB
    | LP ws? programming_contents_with_comma ws? RP
    | string;

programming_contents_with_comma
    : WS* (programming_content|WS|COMMA|COLIN|FS)* WS*;

// resets
EOL
    : WS* NEWLINE WS*;
post_test_reset
    : EOL EOL;
pre_test_reset
    : post_test_reset EOL+ ;




everything_until_newline
    : ~(EOL|RULETOKEN)+ ;

set_keyword
    : SET_KEYWORD ;
identifier
    : CHAR_SEQ;
string
    : QUOTE (ESCAPEDQUOTE|~('"'))* QUOTE;
ws
    : (WS|NEWLINE)+;



RULETOKEN
    : '!'?'=>';

COMMENTTOKEN
    : FS FS FS
    | '#';

FAKERULETOKEN
    : '\'' RULETOKEN '\'';

SET_KEYWORD
    : 'SET' [A-Z]? ;

EXCEPTION_KEYWORD
    : 'EX';

ESCAPEDQUOTE
    : ESCAPE '"';
ESCAPE
    : '\\';

CHAR_SEQ
    : CHAR+;
CHAR
    : (ESCAPEDQUOTE|NON_RESERVED_CHARACTER);
NON_RESERVED_CHARACTER
    : ~('('|')'|'['|']'|'{'|'}'|':'|'"'|','|'\n'|' '|'#'|'='|'\\'|'/');

FS
    : '/';
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
EQ
    : '=';


QUOTE
    : '"';
COMMA
    : WS* ',' WS*;
NEWLINE
    : '\n';

COLIN
    : ':';
STAR
    : '*';
WS
    : ' ';
