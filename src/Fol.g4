grammar Fol;
logic :
    FORALL vars logic # forall
  | EXISTS vars logic # exists
  | term '=>' term # implication
  | term # topTerm
  ;
term :
    term AND term # and
  | term OR term # or
  | term EQ term # eq
  | NOT '(' term ')' # not
  | '(' term ')' # wrap
  | CONST '(' expr (',' expr)* ')' # predicate
  ;
expr :
  VAR
  | CONST ;

vars :
    VAR+
  | VAR (',' VAR)* ','?
  ;

AND : '&';
OR : '|';
EQ : '=';
NOT : '~';
FORALL : '@' ;
EXISTS : '#' ;
VAR : [a-z]+ ;
CONST : [A-Z][a-zA-Z0-9]* ;
WS : [ \t\r\n]+ -> skip ;
