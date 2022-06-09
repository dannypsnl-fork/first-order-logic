package cnf;

import java.util.List;

// fol.Or(A, fol.or(B, C)) -> cnf.Or(A, B, C)
// fol.And(A, fol.And(B, C)) -> cnf.And(A, B, C)
public class Or implements CNF  {
    public List<CNF> subexprs;
    public Or(CNF ... subexprs) {
        this.subexprs = List.of(subexprs);
    }

    @Override
    public String toString() {
        return String.join(" ∨ ", subexprs.stream().map(Object::toString).toList());
    }
}

