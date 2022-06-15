package org.kmu.fol
import org.antlr.v4.runtime.{CharStreams, CommonTokenStream}
import org.kmu.fol.parser.{FolLexer, FolParser}

class BuildVisitorSuite extends munit.FunSuite:
  test("constant") {
    assertEquals(BuildVisitor.buildLogicFromString("X()"), Constant("X"))
  }
  test("predicate") {
    assertEquals(
      BuildVisitor.buildLogicFromString("D(x)"),
      Predicate("D", Variable("x"))
    )
  }
  test("and") {
    assertEquals(
      BuildVisitor.buildLogicFromString("a and b"),
      And(Variable("a"), Variable("b"))
    )
  }
  test("or") {
    assertEquals(
      BuildVisitor.buildLogicFromString("A or B or C"),
      Or(Or(Variable("A"), Variable("B")), Variable("C"))
    )
  }
  test("not") {
    assertEquals(
      BuildVisitor.buildLogicFromString("not not A"),
      Not(Not(Variable("A")))
    )
  }
  test("implication") {
    assertEquals(
      BuildVisitor.buildLogicFromString("A => B"),
      Implication(
        Variable("A"),
        Variable("B")
      )
    )
  }
  test("forall exists mixing") {
    assertEquals(
      BuildVisitor.buildLogicFromString("exists x . D(x) => forall y . D(y)"),
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
    assertEquals(
      BuildVisitor.buildLogicFromString("exists x . D(x) => (forall y . D(y))"),
      Exists(
        Seq("x"),
        Implication(
          Predicate("D", Variable("x")),
          Forall(Seq("y"), Predicate("D", Variable("y")))
        )
      )
    )
  }
