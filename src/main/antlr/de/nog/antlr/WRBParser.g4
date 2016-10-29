/**
 * Define a grammar called Math
 */
parser grammar WRBParser;
options{
	language = Java;
	tokenVocab = WRBLexer;	
}
start: statement? (ende statement)* ende?;

//##############
statement :( expression | assign | functiondefinition)  ;
expression : addition;

//##############
addition : multi (( operator +=(ADD|SUB)) multi)*;
multi : pow ((operator +=(MUL|DIV)) pow)*;
pow : constant ( POW constant)*;
constant: INTEGER|FLOAT|(BRACKETOPEN expression BRACKETCLOSE)|function|ID;
ende : TERMINATOR ;

//##############
functiondefinition : ID BRACKETOPEN ID (COMMA ID)* BRACKETCLOSE ASSIGN expression;
function : ID BRACKETOPEN expression (COMMA expression)* BRACKETCLOSE;
//##############
assign : ID ASSIGN expression;

