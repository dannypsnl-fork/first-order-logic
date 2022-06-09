package cnf;

import java.util.List;

public class And implements CNF {
    public List<CNF> subexprs;
    public And(CNF ... subexprs) {
        this.subexprs = List.of(subexprs);
    }

    @Override
    public String toString() {
        return String.join(" ∧ ", subexprs.stream().map(Object::toString).toList());
    }
}

