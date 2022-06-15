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
  test("and") {
    val parser = parseString("a and b")
    assertEquals(
      BuildVisitor.visit(parser.logic()),
      And(Variable("a"), Variable("b"))
    )
  }
  test("or") {
    val parser = parseString("A or B or C")
    assertEquals(
      BuildVisitor.visit(parser.logic()),
      Or(Or(Variable("A"), Variable("B")), Variable("C"))
    )
  }
  test("not") {
    val parser = parseString("not not A")
    assertEquals(
      BuildVisitor.visit(parser.logic()),
      Not(Not(Variable("A")))
    )
  }
  test("implication") {
    val parser = parseString("A => B")
    assertEquals(
      BuildVisitor.visit(parser.logic()),
      Implication(
        Variable("A"),
        Variable("B")
      )
    )
  }
  test("forall exists mixing") {
    val parser = parseString("exists x . D(x) => forall y . D(y)")
    assertEquals(
      BuildVisitor.visit(parser.logic()),
      Exists(
        Seq("x"),
        Implication(
          Predicate("D", Variable("x")),
          Forall(Seq("y"), Predicate("D", Variable("y")))
        )
      )
    )
  }
  test("forall exists mixing") {
    val parser = parseString("exists x . D(x) => (forall y . D(y))")
    assertEquals(
      BuildVisitor.visit(parser.logic()),
      Exists(
        Seq("x"),
        Implication(
          Predicate("D", Variable("x")),
          Forall(Seq("y"), Predicate("D", Variable("y")))
        )
      )
    )
  }
