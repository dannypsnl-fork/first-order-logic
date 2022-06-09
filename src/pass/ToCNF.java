package pass;

import cnf.CNF;
import fol.*;

import java.util.List;
import java.util.stream.Collectors;

public class ToCNF {
    public List<cnf.CNF> flat_or(CNF expr) {
        switch (expr) {
            case cnf.Or or -> {
                return or.subexprs
                        .stream().map(this::flat_or).toList()
                        .stream().flatMap(List::stream)
                        .collect(Collectors.toList());
            }
            case cnf.And and -> {
                return flat_and(and);
            }
            default -> {
                return List.of(pass(expr));
            }
        }
    }

    public List<cnf.CNF> flat_and(CNF expr) {
        switch (expr) {
            case cnf.And and -> {
                return and.subexprs
                        .stream().map(this::flat_and).toList()
                        .stream().flatMap(List::stream)
                        .collect(Collectors.toList());
            }
            case cnf.Or or -> {
                return flat_or(or);
            }
            default -> {
                return List.of(pass(expr));
            }
        }
    }

    public CNF pass(FOL expr) {
        if (expr instanceof Or or) {
            return new cnf.Or(flat_or(new cnf.Or(pass(or.left), pass(or.right))).toArray(new cnf.CNF[0]));
        } else if (expr instanceof And and) {
            return new cnf.And(flat_and(new cnf.And(pass(and.left), pass(and.right))).toArray(new cnf.CNF[0]));
        } else if (expr instanceof Not not) {
            return new cnf.Not(pass(not.expr));
        } else {
            return (CNF)expr;
        }
    }
}
