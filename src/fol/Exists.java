package fol;

import cnf.Variable;

import java.util.List;

public class Exists implements FOL {
    public final List<Variable> vars;
    public final FOL body;

    public Exists(List<Variable> vars, FOL body) {
        this.vars = vars;
        this.body = body;
    }

    @Override
    public String toString() {
        return "∃" + vars.toString() + "." + body.toString();
    }
}
            