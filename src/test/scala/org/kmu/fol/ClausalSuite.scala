package org.kmu.fol

class ClausalSuite extends munit.FunSuite:
  test("distribute law") {
    assertEquals(
      BuildVisitor.toClausal(
        BuildVisitor.buildLogicFromString(
          "(A and B) or C"
        )
      ),
      BuildVisitor.buildLogicFromString(
        "A or C and B or C"
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
      BuildVisitor.buildLogicFromString(
        "C or A and C or B"
      )
    )
  }
