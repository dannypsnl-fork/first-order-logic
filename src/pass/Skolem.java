package pass;

import fol.*;

public class Skolem {
    public FOL pass(FOL expr) {
        return switch (expr) {
            case Or or -> new Or(
                    pass(or.left),
                    pass(or.right)
            );
            case And and -> new And(
                    pass(and.left),
                    pass(and.right)
            );
            case Not not -> new Not(
                    pass(not.expr)
            );
            case Forall fol -> new Forall(
                    fol.vars,
                    pass(fol.body)
            );
            case Exists exists -> ;
            default -> throw new IllegalStateException("Unexpected value: " + expr);
        };
    }
}