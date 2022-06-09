package cnf;

import fol.FOL;

public class Not implements CNF {
    public FOL expr;

    public Not(FOL expr) {
        this.expr = expr;
    }

    @Override
    public String toString() {
        return "¬" + expr.toString();
    }
}
