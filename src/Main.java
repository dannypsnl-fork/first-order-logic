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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

class FOLBuildVisitor extends FolBaseVisitor<FOL> {
    private List<Variable> buildVars(FolParser.VarsContext ctx) {
        return ctx.VAR().stream().map((v) -> new Variable(v.getText())).collect(Collectors.toList());
    }

    @Override
    public FOL visitTopQuantifier(FolParser.TopQuantifierContext ctx) {
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

    @Override
    public FOL visitTopTerm(FolParser.TopTermContext ctx) {
        return visit(ctx.term());
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
    static void handleFile(String path) {
        List<CNF> knowledge_base = new ArrayList<>();
        List<CNF> queries = new ArrayList<>();
        var counter = 0;
        try {
            var buf = new BufferedReader(new FileReader(path));
            String line = buf.readLine();
            while (line != null) {
                try {
                    Integer.parseInt(line);
                    counter += 1;
                } catch (NumberFormatException e) {
                    var logic = fromStatement(line);
                    if (counter == 2) {
                        queries.add(Pass.all(logic));
                    } else {
                        knowledge_base.add(Pass.all(logic));
                    }
                }
                line = buf.readLine();
            }
            buf.close();

            System.out.println("Queries:");
            for (CNF cnf : queries) {
                System.out.println(cnf);
            }

            System.out.println("Knowledge Base:");
            for (CNF cnf : knowledge_base) {
                System.out.println(cnf);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static FOL fromStatement(String statement) {
        var lexer = new FolLexer(new ANTLRInputStream(statement));
        var parser = new FolParser(new CommonTokenStream(lexer));
        var builder = new FOLBuildVisitor();
        return builder.visit(parser.logic());
    }

    public static void main(String[] args) {
        handleFile("./input/1/input.txt");
        handleFile("./input/2/input.txt");
    }
}
