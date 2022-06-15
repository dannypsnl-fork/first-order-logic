package org.kmu.fol

trait Logic {}
case class Not(statement: Logic) extends Logic
case class Or(subs: Seq[Logic]) extends Logic
case class And(subs: Seq[Logic]) extends Logic
case class Implication() extends Logic
case class Forall() extends Logic
case class Exists() extends Logic
