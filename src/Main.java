import fol.*;
import fol.parser.FolBaseVisitor;
import fol.parser.FolLexer;
import fol.parser.FolParser;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;

import java.util.Collections;

class FOLBuildVisitor extends FolBaseVisitor<FOL> {
    @Override
    public FOL visitLogic(FolParser.LogicContext ctx) {
        if (ctx.quantifier() == null) {
            // only one term
            return visitTerm(ctx.term(0));
        } else {
            var qs = ctx.quantifier();
            Collections.reverse(qs);
            FOL cumulative = new Implication(visitTerm(ctx.term(0)),visitTerm(ctx.term(1)));
            for(var q : qs) {
                var vars = q.VAR();
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
}
public class Main {
    public static void main(String[] args) {
        var input = "@x Pred1(x) => Pred2(x)";
        var lexer =  new FolLexer(new ANTLRInputStream(input));
        var parser = new FolParser(new CommonTokenStream(lexer));

        var builder = new FOLBuildVisitor();
        System.out.println(builder.visit(parser.logic()));
    }
}