package pass;

import cnf.CNF;
import fol.FOL;

public interface Pass {
     FOL pass(FOL expr);

     static CNF all(FOL expr) {
          Pass[] all_passes = {
                  new RemoveImplication(),
                  new MoveNotIn(),
                  new Skolemization(),
                  new RemoveForall(),
                  new VerifyRemoveForall(),
                  new Redistribute(),
          };
          for (var pass : all_passes) {
               expr = pass.pass(expr);
          }
          return (new ToCNF().pass(expr));
     }
}
