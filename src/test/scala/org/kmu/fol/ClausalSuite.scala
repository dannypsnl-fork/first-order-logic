package org.kmu.fol

class ClausalSuite extends munit.FunSuite:
  test("distribute law") {
    assertEquals(
      BuildVisitor.toClausal(
        BuildVisitor.buildLogicFromString(
          "(A and B) or C"
        )
      ),
      And(
        Or(Variable("A"), Variable("C")),
        Or(Variable("B"), Variable("C"))
      )
    )
  }
  test("distribute law") {
    assertEquals(
      BuildVisitor.toClausal(
        BuildVisitor.buildLogicFromString(
          "C or (A and B)"
        )
      ),
      And(
        Or(Variable("C"), Variable("A")),
        Or(Variable("C"), Variable("B"))
      )
    )
  }
