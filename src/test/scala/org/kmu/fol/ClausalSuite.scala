package org.kmu.fol

class ClausalSuite extends munit.FunSuite:
  test("distribute law") {
    assertEquals(
      BuildVisitor.toClausal(
        BuildVisitor.buildLogicFromString(
          "A and (B or C)"
        )
      ),
      And(
        Or(Variable("A"), Variable("B")),
        Or(Variable("A"), Variable("C"))
      )
    )
  }
