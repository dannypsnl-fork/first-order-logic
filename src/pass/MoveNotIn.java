package pass;

import cnf.Not;
import cnf.Variable;
import fol.*;
public class MoveNotIn implements Pass {
    @Override
    public FOL pass(FOL expr) {
        return switch (expr) {
            case Not not -> switch (not.expr) {
                        case Forall forall -> new Exists(forall.vars, pass(new Not(pass(forall.body))));
                        case Exists exists -> new Forall(exists.vars, pass(new Not(pass(exists.body))));
                        case Not inner_not -> pass(inner_not.expr);
                        default -> new Not(pass(not.expr));
                    };
            case Or or -> new Or(pass(or.left), pass(or.right));
            case And and -> new And(pass(and.left), pass(and.right));
            case Forall forall -> new Forall(forall.vars, pass(forall.body));
            case Exists exists -> new Exists(exists.vars, pass(exists.body));
            case Variable variable -> variable;
            default -> expr;
        };
    }
}
