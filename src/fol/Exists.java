package fol;

import java.util.List;

public class Exists implements FOL {
    public final List<String> vars;
    public final FOL body;

    public Exists(List<String> vars, FOL body) {
        this.vars = vars;
        this.body = body;
    }

    @Override
    public String toString() {
        return "∃" + vars.toString() + "." + body.toString();
    }
}
            