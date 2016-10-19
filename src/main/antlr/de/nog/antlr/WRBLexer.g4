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
//FLOAT : [DIGIT]'.'[DIGIT]; //sossenbrink
FLOAT : INTEGER('.')(DIGIT)+; //Staffa

ADD : '+';
SUB : '-';
MUL :'*';
DIV :'/';
POW : '^';
ASSIGN : '=';
//DOTOPERATOR : ('*'|'/');
//DASHOPERATOR: ('-'|'+');
VARIABLE : ([a-z]|[A-Z])+;
WHITESPACE : [ \t]+ -> skip ;//whitespaces interessieren uns nicht

fragment DIGIT : [0-9];


//CONSTANT : INTEGER|FLOAT;

//ATOM : CONSTANT|VARIABLE;
//TERM : ATOM [DOTOPERATOR ATOM];
