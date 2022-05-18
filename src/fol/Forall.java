package fol;

import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.List;

public class Forall implements FOL {
    private final FOL body;
    private final List<String> vars;

    public Forall(List<String> vars, FOL body) {
        this.vars = vars;
        this.body = body;
    }

    @Override
    public String toString() {
        return "∀" + vars.toString() + "." + body.toString();
    }
}
