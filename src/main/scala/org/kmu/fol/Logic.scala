package org.kmu.fol

trait Logic:
  def is_clausal(): Boolean =
    this match
      case Top() => true
      case And(s*) =>
        for elem <- s do if !elem.is_clause() then return false
        true
      case _ => is_clause()
  def is_clause(): Boolean =
    this match
      case Bottom() => true
      case Or(s*) =>
        for elem <- s do if !elem.is_literal() then return false
        true
      case _ => is_literal()
  def is_literal(): Boolean =
    this match
      case Variable(_) | Constant(_) | Predicate(_) | Not(_) => true
      case _                                                 => false
trait Term extends Logic {}
case class Variable(name: String) extends Term
case class Constant(name: String) extends Term
case class Predicate(name: String, terms: Term*) extends Term
case class Top() extends Logic
case class Bottom() extends Logic
case class Not(statement: Logic) extends Logic
case class Or(subs: Logic*) extends Logic
case class And(subs: Logic*) extends Logic
case class Implication(left: Logic, right: Logic) extends Logic
case class Forall(vars: Seq[String], body: Logic) extends Logic
case class Exists(vars: Seq[String], body: Logic) extends Logic
