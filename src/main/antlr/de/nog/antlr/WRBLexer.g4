/**
 * Define a grammar called Math
 */
lexer grammar WRBLexer;
options{
	language = Java;

}
WHITESPACE: [' ']+ -> skip ;//whitespaces
ID : ([a-z]|[A-Z])+DIGIT* ;  


//INTEGER : ('-')?(DIGIT)+;
INTEGER : (DIGIT)+;

FLOAT : INTEGER('.')(DIGIT)+; //Staffa

ADD : '+';
MUL :'*';
BRACKETOPEN : '(';
BRACKETCLOSE : ')';
SUB : '-';
DIV :'/';
POW : '^' | '**';
ASSIGN : '=';
TERMINATOR : ';';
COMMA : ',';
fragment DIGIT : [0-9];
