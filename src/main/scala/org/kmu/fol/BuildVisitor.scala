package org.kmu.fol
import org.kmu.fol.parser.{FolBaseVisitor, FolParser}
import scala.jdk.CollectionConverters.*

object BuildVisitor extends FolBaseVisitor[Logic]:
  override def visitWrap(ctx: FolParser.WrapContext): Logic = visit(ctx.logic())
  override def visitNot(ctx: FolParser.NotContext): Logic =
    Not(visit(ctx.logic()))
  override def visitOr(ctx: FolParser.OrContext): Logic =
    Or(ctx.logic().asScala.toSeq.map(visit))
  override def visitAnd(ctx: FolParser.AndContext): Logic =
    And(ctx.logic().asScala.toSeq.map(visit))
  override def visitImplication(ctx: FolParser.ImplicationContext): Logic =
    Implication()
  override def visitForall(ctx: FolParser.ForallContext): Logic =
    Forall()
  override def visitExists(ctx: FolParser.ExistsContext): Logic =
    Exists()
