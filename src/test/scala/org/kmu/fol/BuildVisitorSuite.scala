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
  test("predicate") {
    val parser = parseString("D(x)")
    assertEquals(
      BuildVisitor.visit(parser.logic()),
      Predicate("D", Variable("x"))
    )
  }
  test("and terms") {
    val parser = parseString("a and b")
    assertEquals(
      BuildVisitor.visit(parser.logic()),
      And(Variable("a"), Variable("b"))
    )
  }
  test("or terms") {
    val parser = parseString("A or B or C")
    assertEquals(
      BuildVisitor.visit(parser.logic()),
      Or(Or(Variable("A"), Variable("B")), Variable("C"))
    )
  }
  test("not terms") {
    val parser = parseString("not not A")
    assertEquals(
      BuildVisitor.visit(parser.logic()),
      Not(Not(Variable("A")))
    )
  }
