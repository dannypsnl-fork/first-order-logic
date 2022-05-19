import cnf.CNF;
import cnf.Not;
import cnf.Or;
import fol.Exists;
import fol.FOL;
import fol.Forall;
import fol.Implication;

public class Transformer {
    public CNF transform(FOL fol) {
        return switch (fol) {
            case Forall forall -> transform(forall.body);
            case Exists exists -> transform(exists.body);
            case Implication impl -> new Or(
                    new Not(transform(impl.left)),
                    transform(impl.right)
            );
            default -> throw new IllegalStateException("Unexpected value: " + fol);
        };
    }

}
