/**
 * Define a grammar called Math
 */
parser grammar WRBParser;
options{
	language = Java;
	tokenVocab = WRBLexer;	
}
//start: statement? (ende statement)* ende?;
start: statement? (TERMINATOR statement)* TERMINATOR?;

//##############
statement :( expression | assign | functiondefinition)  ;
expression :  multi (( operator +=(ADD|SUB)) multi)*;

//##############
functiondefinition : ID BRACKETOPEN ID (COMMA ID)* BRACKETCLOSE ASSIGN expression;
function : ID BRACKETOPEN expression (COMMA expression)* BRACKETCLOSE;

//##############
multi : pow ((operator +=(MUL|DIV)) pow)*;
pow : constant (POW constant)*;
constant: (sign +=(ADD|SUB))?(INTEGER|FLOAT| (BRACKETOPEN expression BRACKETCLOSE)| function|ID);

//##############
assign : ID ASSIGN expression;

