grammar Fol;
logic :
    term '&' logic
  | term '|' logic
  | term '=' logic
  | '~' logic
  |<assoc=right> term op='=>' logic
  | term
  ;
term :
    quantifier logic
  | CONST '(' VAR (',' VAR)* ')'
  | '(' logic ')'
  ;
quantifier : (FORALL|EXISTS) VAR+
  ;

FORALL : '@' ;
EXISTS : '#' ;
VAR : [a-z]+ ;
CONST : [A-Z][a-zA-Z0-9]+ ;
WS : [ \t\r\n]+ -> skip ;
