/**
 * Define a grammar called Math
 */
parser grammar WRBParser;
options{
	language = Java;
	tokenVocab = WRBLexer;	
}



statement : expression | assign;
expression : addition;
addition : multi ( operator +=(ADD|SUB)) multi;
multi : pow ( operator +=(MUL|DIV)) pow;
pow: INTEGER;


//statement : assign | expression;
assign : ID ASSIGN expression;

//constant : INTEGER|FLOAT;
//atom : constant|VARIABLE;
//expression:  term ( operator +=(ADD|SUB) term )*;
//term : constant;


//term: dotop (operator +=(ADD|SUB) dotop )* ;
//dotop: power (operator +=(MUL|DIV) power)*;
//power: atom (POW atom)*;
