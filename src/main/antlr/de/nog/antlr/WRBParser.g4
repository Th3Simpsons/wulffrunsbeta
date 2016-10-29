/**
 * Define a grammar called Math
 */
parser grammar WRBParser;
options{
	language = Java;
	tokenVocab = WRBLexer;	
}
start: statement*;

//##############
statement :( expression | assign | functiondefinition) ende? ;
expression : addition;

//##############
addition : multi (( operator +=(ADD|SUB)) multi)*;
multi : pow ((operator +=(MUL|DIV)) pow)*;
pow : constant ( POW constant)*;
constant: INTEGER|FLOAT|(BRACKETOPEN expression BRACKETCLOSE)|ID|function;
ende : TERMINATOR ;

//##############
functiondefinition : ID BRACKETOPEN ID (COMMA ID)* BRACKETCLOSE ASSIGN expression;
function : ID BRACKETOPEN expression (COMMA expression)* BRACKETCLOSE;
//##############
assign : ID ASSIGN expression;

