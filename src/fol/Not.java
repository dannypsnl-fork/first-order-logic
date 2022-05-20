package fol;

public class Not implements FOL {
    public FOL expr;

    public Not(FOL expr) {
        this.expr = expr;
    }
}
