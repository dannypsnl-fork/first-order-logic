grammar Fol;
logic :
    quantifier+ (term '=>' term)
  | term
  ;
term :
    term '&' term
  | term '|' term
  | term '=' term
  | '~' '(' term ')'
  | '(' term ')'
  | CONST '(' (VAR|CONST) (',' (VAR|CONST))* ')'
  ;
quantifier : op=(FORALL|EXISTS) VAR+
  ;

FORALL : '@' ;
EXISTS : '#' ;
VAR : [a-z]+ ;
CONST : [A-Z][a-zA-Z0-9]* ;
WS : [ \t\r\n]+ -> skip ;
