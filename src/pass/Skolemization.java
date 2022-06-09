package pass;

import cnf.*;
import fol.*;
import fol.And;
import fol.Not;
import fol.Or;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Skolemization implements Pass {
    static int skolem_counter = 1;

    @Override
    public FOL pass(FOL expr) {
        return switch (expr) {
            // Exists x,y . P(x) and P(y)
            // ~> P(Skolem1(x)) and P(Skolem2(y))
            case Exists exists -> f(exists.body, exists.vars);
            case Or or -> new Or(pass(or.left), pass(or.right));
            case And and -> new And(pass(and.left), pass(and.right));
            case Not not -> new Not(pass(not.expr));
            case Forall fol -> new Forall(fol.vars, pass(fol.body));
            case Variable variable -> variable;
            default -> expr;
        };
    }

    FOL f(FOL expr, List<Variable> vars) {
        Map<CNF, CNF> map = new HashMap<>();
        for (var v : vars) {
            map.put(v, new Skolem(skolem_counter, v));
            skolem_counter += 1;
        }
        return traverse(expr, map);
    }

    FOL traverse(FOL expr, Map<CNF, CNF> substMap) {
        return switch (expr) {
            case Or or -> new Or(traverse(or.left, substMap), traverse(or.right, substMap));
            case And and -> new And(traverse(and.left, substMap), traverse(and.right, substMap));
            case Not not -> new Not(
                    traverse(not.expr, substMap)
            );
            case Forall fol -> new Forall(
                    fol.vars,
                    traverse(fol.body, substMap)
            );
            case CNFPredicate pred -> new CNFPredicate(
                    pred.name,
                    pred.vars.stream().map((v) -> substMap.getOrDefault(v, v)).toList()
            );
            case Variable variable -> variable;
            default -> throw new IllegalStateException("Unexpected value: " + expr);
        };
    }
}
