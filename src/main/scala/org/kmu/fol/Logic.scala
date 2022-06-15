package org.kmu.fol

trait Logic {}
trait Term extends Logic {}
case class Variable(name: String) extends Term
case class Constant(name: String) extends Term
case class Predicate(name: String, terms: Term*) extends Term
case class Not(statement: Logic) extends Logic
case class Or(subs: Logic*) extends Logic
case class And(subs: Logic*) extends Logic
case class Implication(left: Logic, right: Logic) extends Logic
case class Forall(vars: Seq[String], body: Logic) extends Logic
case class Exists(vars: Seq[String], body: Logic) extends Logic
