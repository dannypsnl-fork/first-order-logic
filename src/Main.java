import fol.*;
import fol.parser.FolBaseVisitor;
import fol.parser.FolLexer;
import fol.parser.FolParser;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import pass.*;

import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.toList;

class FOLBuildVisitor extends FolBaseVisitor<FOL> {
    @Override
    public FOL visitLogic(FolParser.LogicContext ctx) {
        if (ctx.quantifier() == null) {
            // only one term
            return visitTerm(ctx.term(0));
        } else {
            var qs = ctx.quantifier();
            Collections.reverse(qs);
            FOL cumulative = new Implication(visitTerm(ctx.term(0)), visitTerm(ctx.term(1)));
            for (var q : qs) {
                var vars = buildVars(q.vars());
                switch (q.op.getText()) {
                    case "@" -> cumulative = new Forall(vars, cumulative);
                    case "#" -> cumulative = new Exists(vars, cumulative);
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

        Pass[] all_passes = {
                new RemoveImplication(),
                new MoveNotIn(),
                new Skolemization(),
                new RemoveForall(),
                new Redistribute(),
        };
        FOL expression = builder.visit(parser.logic());
        for (var pass : all_passes) {
            expression = pass.pass(expression);
        }
        System.out.println(expression);
    }
}