# reference implementation

This is a reference implementation for Java program written in Racket and use some powerful tools like [**nanopass**](https://github.com/nanopass/nanopass-framework-racket).

### convert First Order Logic to Conjunctive Normal Form

Quantifiers: $\forall$ and $\exists$

1. remove implication, which means

$P \Rightarrow Q$ goes to $\lnot P \lor Q$

2. move **not** into quantifier scope

First: $\lnot \forall x. E$ goes to $\exists x. \lnot E$

Second: $\lnot \exists x. E$ goes to $\forall x. \lnot E$

and we would like to simplify $\lnot$ by swaping it into the innest expression

3. skolem: Here we are going to remove $\exists$ expression, by adding **Skolem** function on to those variables hold by $\exists$. The form like $\exists x. P(x)$ goes to $P(Skolem1(x))$

4. remove quantifier $\forall$ since all rest variables is hold by $\forall$. Thus, $\forall x. E$ goes to $E$

5. Finally, we redistribute $\land$. The form $(P \land Q) \lor R$ goes to $(P \lor R) \land (Q \lor R)$

### Resolution

Reference: http://logic.stanford.edu/intrologic/notes/chapter_05.html

TODO
