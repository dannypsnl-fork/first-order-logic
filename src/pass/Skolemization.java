package pass;

import cnf.CNF;
import cnf.CNFPredicate;
import cnf.Skolem;
import cnf.Variable;
import fol.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

public class Skolemization {
    static int skolem_counter = 1;

    public FOL pass(FOL expr) {
        return switch (expr) {
            case Or or -> new Or(
                    pass(or.left),
                    pass(or.right)
            );
            case And and -> new And(
                    pass(and.left),
                    pass(and.right)
            );
            case Not not -> new Not(
                    pass(not.expr)
            );
            case Forall fol -> new Forall(
                    fol.vars,
                    pass(fol.body)
            );
            // Exists x,y . P(x) and P(y)
            // ~> P(Skolem1(x)) and P(Skolem2(y))
            case Exists exists -> f(exists.body, exists.vars);
            default -> throw new IllegalStateException("Unexpected value: " + expr);
        };
    }

    FOL f(FOL expr, List<String> vars) {
        Map<String, CNF> map = new HashMap<>();
        for (var v : vars) {
            map.put(v, new Skolem(skolem_counter, v));
            skolem_counter += 1;
        }
        return traverse(expr, map);
    }

    FOL traverse(FOL expr, Map<String, CNF> substMap) {
        return switch (expr) {
            case Or or -> new Or(
                    traverse(or.left, substMap),
                    traverse(or.right, substMap)
            );
            case And and -> new And(
                    traverse(and.left, substMap),
                    traverse(and.right, substMap)
            );
            case Not not -> new Not(
                    traverse(not.expr, substMap)
            );
            case Forall fol -> new Forall(
                    fol.vars,
                    traverse(fol.body, substMap)
            );
            case Predicate pred -> new CNFPredicate(
                    pred.name,
                    pred.vars.stream().map((v) -> {
                        if (substMap.containsKey(v)) {
                            return substMap.get(v);
                        } else {
                            return new Variable(v);
                        }
                    }).collect(toList())
            );
            default -> throw new IllegalStateException("Unexpected value: " + expr);
        };
    }
}
