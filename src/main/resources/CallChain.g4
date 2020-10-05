grammar CallChain;

NUMBER : [0-9]+;
ELEMENT: 'element';
OPERATION: '+' | '-' | '*' | '>' | '<' | '=' | '&' | '|';

minusExpr: '-'NUMBER;
constExpr : NUMBER | minusExpr;
binExpr : '(' expr OPERATION expr ')';
expr: ELEMENT | constExpr | binExpr;
mapCall : 'map{' expr '}';
filterCall : 'filter{' expr '}';
call : mapCall | filterCall;
callChain : call ('%>%' call)*;
