package pass;

import fol.FOL;
import fol.Forall;

public class VerifyRemoveForall implements Pass {
    @Override
    public FOL pass(FOL expr) {
        return switch (expr) {
            case Forall _forall -> throw new IllegalStateException("shouldn't have forall still");
            default -> expr;
        };
    }
}
