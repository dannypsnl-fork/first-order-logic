package org.kmu.fol

trait Logic {}
trait Term extends Logic {}
case class Variable(name: String) extends Term
case class Constant(name: String) extends Term
case class Predicate(name: String, terms: Seq[Term]) extends Term
case class Not(statement: Logic) extends Logic
case class Or(subs: Seq[Logic]) extends Logic
case class And(subs: Seq[Logic]) extends Logic
case class Implication() extends Logic
case class Forall() extends Logic
case class Exists() extends Logic
