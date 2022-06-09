package fol;

import cnf.Variable;

import java.util.List;

public class Forall implements FOL {
    public final FOL body;
    public final List<Variable> vars;

    public Forall(List<Variable> vars, FOL body) {
        this.vars = vars;
        this.body = body;
    }

    @Override
    public String toString() {
        return "∀" + vars.toString() + "." + body.toString();
    }
}
