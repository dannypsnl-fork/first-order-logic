package pass;
import fol.*;

public class Redistribute implements Pass {
    @Override
    public FOL pass(FOL expr) {
         switch (expr) {
            case Or or -> {
                And and = null;
                FOL to_distribute = null;
                if (or.left instanceof And left_and) {
                    and = left_and;
                    to_distribute = or.right;
                } else if (or.right instanceof And right_and) {
                    and = right_and;
                    to_distribute = or.left;
                };
                if (and != null) {
                    return new And(new Or(pass(and.left), pass(to_distribute)), new Or(pass(and.right),pass(to_distribute)));
                } else {
                    return new Or(pass(or.left), pass(or.right));
                }
            }
            // catamorphism
            case And and -> {
                return new And(pass(and.left), pass(and.right));
            }
            case Not not -> {
                return new Not(pass(not.expr));
            }
            default -> throw new IllegalStateException("Unexpected value: " + expr);
        }
    }
}
