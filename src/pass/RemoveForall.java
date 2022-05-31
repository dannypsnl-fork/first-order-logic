package pass;

import fol.FOL;
import fol.Forall;

public class RemoveForall implements Pass {
    @Override
    public FOL pass(FOL expr) {
        return switch (expr) {
            case Forall forall -> forall.body;
            default -> expr.cata(pass);
        };
    }
}
