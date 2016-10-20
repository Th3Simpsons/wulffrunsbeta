/**
 * Define a grammar called Math
 */
lexer grammar WRBLexer;
options{
	language = Java;

}
ID : [a-z]+ ;  


//INTEGER : [+-]?DIGIT+ ; // internetz
INTEGER : ('-')?(DIGIT)+;
FLOAT : INTEGER('.')(DIGIT)+; //Staffa

ADD : '+';
SUB : '-';
MUL :'*';
DIV :'/';
POW : '^';
ASSIGN : '=';
TERMINATOR : ';';
BRACKETOPEN : '(';
BRACKETCLOSE : ')';
//DOTOPERATOR : ('*'|'/');
//DASHOPERATOR: ('-'|'+');
VARIABLE : ([a-z]|[A-Z])+;
WHITESPACE : [ \t]+ -> skip ;//whitespaces

fragment DIGIT : [0-9];


//CONSTANT : INTEGER|FLOAT;

//ATOM : CONSTANT|VARIABLE;
//TERM : ATOM [DOTOPERATOR ATOM];
