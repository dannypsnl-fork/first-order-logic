package org.kmu.fol
import org.antlr.v4.runtime.{CharStreams, CommonTokenStream}
import org.kmu.fol.parser.{FolLexer, FolParser}

class BuildVisitorSuite extends munit.FunSuite:
  def parseString(s: String): FolParser =
    val stream = CharStreams.fromString(s)
    val lex = FolLexer(stream)
    val tokens = CommonTokenStream(lex)
    FolParser(tokens)

  test("constant") {
    val parser = parseString("X()")
    assertEquals(BuildVisitor.visit(parser.logic()), Constant("X"))
  }
  test("and terms") {
    val parser = parseString("a and b")
    assertEquals(
      BuildVisitor.visit(parser.logic()),
      And(Variable("a"), Variable("b"))
    )
  }
