grammar Fol;
logic :
    CONST '(' expr (',' expr)* ')' # predicate
  | '(' logic ')' # wrap
  | NOT '(' logic ')' # not
  | logic OR logic # or
  | logic AND logic # and
  | logic EQ logic # eq
  | logic '=>' logic # implication
  | FORALL vars logic # forall
  | EXISTS vars logic # exists
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
