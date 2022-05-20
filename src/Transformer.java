import fol.*;

public class Transformer {
    public FOL remove_implication(FOL expr) {
        return switch (expr) {
            case Implication impl -> new Or(
                    new Not(remove_implication(impl.left)), remove_implication(impl.right)
            );
            case Or or -> new Or(
                    remove_implication(or.left),
                    remove_implication(or.right)
            );
            case And and -> new And(
                    remove_implication(and.left),
                    remove_implication(and.right)
            );
            case Not not -> new Not(
                    remove_implication(not.expr)
            );
            case Forall fol -> new Forall(
                    fol.vars,
                    remove_implication(fol.body)
            );
            case Exists exists -> new Exists(
                    exists.vars,
                    remove_implication(exists.body)
            );
            default -> throw new IllegalStateException("Unexpected value: " + expr);
        };
    }
}
