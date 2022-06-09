import cnf.CNF;
import cnf.CNFPredicate;
import cnf.Constant;
import cnf.Variable;
import fol.*;
import fol.parser.FolBaseVisitor;
import fol.parser.FolLexer;
import fol.parser.FolParser;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import pass.Pass;

import java.util.*;
import java.util.stream.Collectors;

class FOLBuildVisitor extends FolBaseVisitor<FOL> {
    private List<Variable> buildVars(FolParser.VarsContext ctx) {
        return ctx.VAR().stream().map((v) -> new Variable(v.getText())).collect(Collectors.toList());
    }

    @Override
    public FOL visitLogic(FolParser.LogicContext ctx) {
        if (ctx.quantifier() == null) {
            // only one term
            return visit(ctx.term(0));
        } else {
            var qs = ctx.quantifier();
            Collections.reverse(qs);
            FOL cumulative = new Implication(visit(ctx.term(0)), visit(ctx.term(1)));
            for (var q : qs) {
                var vars = buildVars(q.vars());
                if (q.FORALL() != null) {
                    cumulative = new Forall(vars, cumulative);
                } else if (q.EXISTS() != null) {
                    cumulative = new Exists(vars, cumulative);
                }
            }
            return cumulative;
        }
    }

    @Override
    public FOL visitAnd(FolParser.AndContext ctx) {
        return new And(visit(ctx.term(0)), visit((ctx.term(1))));
    }

    @Override
    public FOL visitOr(FolParser.OrContext ctx) {
        return new Or(visit((ctx.term(0))), visit((ctx.term(1))));
    }

    @Override
    public FOL visitEq(FolParser.EqContext ctx) {
        return new Eq(visit((ctx.term(0))), visit((ctx.term(1))));
    }

    @Override
    public FOL visitNot(FolParser.NotContext ctx) {
        return new Not(visit((ctx.term())));
    }

    @Override
    public FOL visitWrap(FolParser.WrapContext ctx) {
        return visit(ctx.term());
    }

    @Override
    public FOL visitPredicate(FolParser.PredicateContext ctx) {
        return new CNFPredicate(
                ctx.CONST().getText(),
                ctx.expr().stream().map((v) -> (CNF)visit(v)).toList()
        );
    }

    @Override
    public FOL visitExpr(FolParser.ExprContext ctx) {
        if (ctx.VAR() != null) {
            return new Variable(ctx.getText());
        } else {
            return new Constant(ctx.getText());
        }
    }
}

public class Main {
    public static void main(String[] args) {
        var input = "@x #y Pred1(x) => Pred2(x, y)";
        var lexer = new FolLexer(new ANTLRInputStream(input));
        var parser = new FolParser(new CommonTokenStream(lexer));

        var builder = new FOLBuildVisitor();

        var expression = builder.visit(parser.logic());
        CNF result = Pass.all(expression);
        System.out.println(result);
    }
}
