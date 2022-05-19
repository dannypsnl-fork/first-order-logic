package fol;

import java.util.List;

public class Forall implements FOL {
    public final FOL body;
    public final List<String> vars;

    public Forall(List<String> vars, FOL body) {
        this.vars = vars;
        this.body = body;
    }

    @Override
    public String toString() {
        return "∀" + vars.toString() + "." + body.toString();
    }
}
