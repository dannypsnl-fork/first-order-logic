import fol.*;
import fol.parser.FolBaseVisitor;
import fol.parser.FolLexer;
import fol.parser.FolParser;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import pass.Pass;
import pass.RemoveForall;
import pass.RemoveImplication;
import pass.Skolemization;

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
    public FOL visitTerm(FolParser.TermContext ctx) {
        return new Term();
    }

    public List<String> buildVars(FolParser.VarsContext ctx) {
        return ctx.VAR().stream().map(ParseTree::getText).collect(toList());
    }
}

public class Main {
    public static void main(String[] args) {
        var input = "@x #y Pred1(x) => Pred2(x, y)";
        var lexer = new FolLexer(new ANTLRInputStream(input));
        var parser = new FolParser(new CommonTokenStream(lexer));

        var builder = new FOLBuildVisitor();

        var pass1 = new RemoveImplication();
        var pass2 = new Skolemization();
        var pass3 = new RemoveForall();
        Pass[] all_passes = {
                pass1,
                pass2,
                pass3,
        };
        FOL expression = builder.visit(parser.logic());
        for (var pass : all_passes) {
            expression = pass.pass(expression);
        }
        System.out.println(expression);
    }
}