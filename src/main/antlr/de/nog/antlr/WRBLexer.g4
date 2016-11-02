/**
 * Define a grammar called Math
 */
lexer grammar WRBLexer;
options{
	language = Java;

}
ID : ([a-z]|[A-Z])+DIGIT* ;  



//INTEGER : [+-]?DIGIT+ ; // internetz
//INTEGER : ('-')?(DIGIT)+;
INTEGER : (DIGIT)+;
//INTEGER : (DIGIT)+;
FLOAT : INTEGER('.')(DIGIT)+; //Staffa

ADD : '+';
SUB : '-';
MUL :'*';
DIV :'/';
POW : '^' | '**';
ASSIGN : '=';
TERMINATOR : ';';
BRACKETOPEN : '(';
BRACKETCLOSE : ')';
COMMA : ',';
WHITESPACE : [ \t]+ -> skip ;//whitespaces
//DOTOPERATOR : ('*'|'/');
//DASHOPERATOR: ('-'|'+');
VARIABLE : ([a-z]|[A-Z])+;
fragment DIGIT : [0-9];


//CONSTANT : INTEGER|FLOAT;

//ATOM : CONSTANT|VARIABLE;
//TERM : ATOM [DOTOPERATOR ATOM];
