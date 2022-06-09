package cnf;

import fol.FOL;

import java.util.List;

public class CNFPredicate implements CNF {
    public String name;
    public List<CNF> vars;

    public CNFPredicate(String name, List<CNF> vars) {
        this.name = name;
        this.vars = vars;
    }

    @Override
    public String toString() {
        return name + "(" +String.join(", ", vars.stream().map(Object::toString).toList())+ ")";
    }
}
