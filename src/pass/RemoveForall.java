package pass;
import cnf.Not;
import fol.*;

public class RemoveForall implements Pass {
    @Override
    public FOL pass(FOL expr) {
        return switch (expr) {
            case Forall forall -> forall.body;
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
            default -> expr;
        };
    }
}
