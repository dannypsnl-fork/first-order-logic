package org.kmu.fol
import org.antlr.v4.runtime.{CharStreams, CommonTokenStream}
import org.kmu.fol.parser.{FolBaseVisitor, FolLexer, FolParser}

import scala.jdk.CollectionConverters.*

object BuildVisitor extends FolBaseVisitor[Logic]:
  def parseString(s: String): FolParser =
    val stream = CharStreams.fromString(s)
    val lex = FolLexer(stream)
    val tokens = CommonTokenStream(lex)
    FolParser(tokens)

  def buildLogicFromString(s: String): Logic =
    val parser = parseString(s)
    visit(parser.logic())

  def clausalStep(e: Logic): Logic =
    e match
      case And(Forall(vars, a), b) =>
        Forall(vars, And(clausalStep(a), clausalStep(b)))
      case And(b, Forall(vars, a)) =>
        Forall(vars, And(clausalStep(a), clausalStep(b)))
      case Or(Forall(vars, a), b) =>
        Forall(vars, Or(clausalStep(a), clausalStep(b)))
      case Or(b, Forall(vars, a)) =>
        Forall(vars, Or(clausalStep(a), clausalStep(b)))
      case Implication(Forall(vars, a), b) =>
        Exists(vars, Implication(clausalStep(a), clausalStep(b)))
      case Implication(b, Forall(vars, a)) =>
        Forall(vars, Implication(clausalStep(b), clausalStep(a)))
      case And(Exists(vars, a), b) =>
        Exists(vars, And(clausalStep(a), clausalStep(b)))
      case And(b, Exists(vars, a)) =>
        Exists(vars, And(clausalStep(a), clausalStep(b)))
      case Or(Exists(vars, a), b) =>
        Exists(vars, Or(clausalStep(a), clausalStep(b)))
      case Or(b, Exists(vars, a)) =>
        Exists(vars, Or(clausalStep(a), clausalStep(b)))
      case Implication(Exists(vars, a), b) =>
        Forall(vars, Implication(clausalStep(a), clausalStep(b)))
      case Implication(b, Exists(vars, a)) =>
        Exists(vars, Implication(clausalStep(b), clausalStep(a)))
      case Not(Forall(vars, a)) => Exists(vars, Not(clausalStep(a)))
      case Not(Exists(vars, a)) => Forall(vars, Not(clausalStep(a)))
      case Forall(_, Top())     => Top()
      case Exists(_, Bottom())  => Bottom()
      case Implication(l, r)    => Or(Not(clausalStep(l)), clausalStep(r))
      case Or(a, And(b, c)) =>
        And(
          Or(clausalStep(a), clausalStep(b)),
          Or(clausalStep(a), clausalStep(c))
        )
      case Or(And(b, c), a) =>
        And(
          Or(clausalStep(b), clausalStep(a)),
          Or(clausalStep(c), clausalStep(a))
        )
      case Not(And(s*))   => Or(s.map(x => Not(clausalStep(x)))*)
      case Not(Or(s*))    => And(s.map(x => Not(clausalStep(x)))*)
      case Not(Top())     => Bottom()
      case Not(Bottom())  => Top()
      case Or(Top(), _s*) => Top()
      case Not(Not(e))    => e
      case _              => e
  def toClausal(old: Logic): Logic =
    val tmp = clausalStep(old)
    if tmp.equals(old)
    then clausalStep(tmp)
    else tmp

  override def visitTrue(ctx: FolParser.TrueContext): Logic = Top()
  override def visitFalse(ctx: FolParser.FalseContext): Logic = Bottom()
  override def visitVariable(ctx: FolParser.VariableContext): Logic =
    Variable(ctx.VAR().getText)
  override def visitPredicate(ctx: FolParser.PredicateContext): Logic =
    if ctx.term().size() == 0
    then Constant(ctx.VAR().getText)
    else
      Predicate(
        ctx.VAR().getText,
        ctx.term().asScala.toSeq.map(x => visit(x).asInstanceOf[Term])*
      )
  override def visitWrap(ctx: FolParser.WrapContext): Logic = visit(ctx.logic())
  override def visitNot(ctx: FolParser.NotContext): Logic =
    Not(visit(ctx.logic()))
  override def visitOr(ctx: FolParser.OrContext): Logic =
    Or(ctx.logic().asScala.toSeq.map(visit)*)
  override def visitAnd(ctx: FolParser.AndContext): Logic =
    And(ctx.logic().asScala.toSeq.map(visit)*)
  override def visitImplication(ctx: FolParser.ImplicationContext): Logic =
    Implication(visit(ctx.logic(0)), visit(ctx.logic(1)))
  override def visitForall(ctx: FolParser.ForallContext): Logic =
    Forall(ctx.VAR().asScala.toSeq.map(x => x.getText), visit(ctx.logic()))
  override def visitExists(ctx: FolParser.ExistsContext): Logic =
    Exists(ctx.VAR().asScala.toSeq.map(x => x.getText), visit(ctx.logic()))
