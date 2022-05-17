grammar Fol;
expr : logic ;

logic :
  CONST '(' VAR (',' VAR)* ')'
  | logic '=>' logic
  | quantifier logic
  ;
quantifier : (FORALL|EXISTS) VAR+;

FORALL : '@' ;
EXISTS : '#' ;
VAR : [a-z]+ ;
CONST : [A-Z][a-zA-Z0-9]+ ;
WS : [ \t\r\n]+ -> skip ;
