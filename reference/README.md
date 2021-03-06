# reference implementation

This is a reference implementation for Java program written in Racket and use some powerful tools like [**nanopass**](https://github.com/nanopass/nanopass-framework-racket).

### convert First Order Logic to Conjunctive Normal Form

Reference: http://logic.stanford.edu/intrologic/notes/chapter_05.html

Quantifiers: $\forall$ and $\exists$

1. remove implication, which means

   $P \Rightarrow Q$ goes to $\lnot P \lor Q$

2. move **not** in, we would like to simplify $\lnot$ by swapping it into the innermost expression.

   1. $\lnot \forall x. E$ goes to $\exists x. \lnot E$
   2. $\lnot \exists x. E$ goes to $\forall x. \lnot E$
   3. $\lnot \lnot E$ goes to $E$

3. skolem: Here we are going to remove $\exists$ expression, by adding **Skolem** function on to those variables hold by $\exists$.

   $\exists x. P(x)$ goes to $P(Skolem1(x))$

4. remove quantifier $\forall$ since all rest variables is hold by $\forall$.

   $\forall x. E$ goes to $E$

5. Finally, we redistribute $\land$, the target is $\land$ is at outermost and no nested same form inside of any clause.
   1. $(P \land Q) \lor R$ goes to $(P \lor R) \land (Q \lor R)$
   2. $R \lor (P \land Q)$ goes to $(R \lor P) \land (R \lor Q)$
   3. $(P \lor Q) \lor R$ goes to $P \lor Q \lor R$
   4. $(P \land Q) \land R$ goes to $P \land Q \land R$

### Resolution

Now, we already have only form like `(and (or ...) ...)`, I mean $\land$ must in the outermost and $\lor$ in the innermost. So we convert $\lor$ to a set of it's body, and $\land$ as set of $\lor$ sets. It called our knowledge base(for short called `KB` in code), with it, we can write down general resolution:

#### example about CNF to set

$$
(P \lor R) \land (Q \lor R)
$$

has set form

$$
\{
    \{P, R\},
    \{Q, R\}
\}
$$

#### Code

```
function resolution(KB) {
    learned := set()
    loop {
        for (r1, r2) in combinations(set_to_list(KB), 2) {
            resolvent := resolve(r1, r2)
            if is_empty_set(resolvent) {
                return true
            } else {
                learned.add(resolvent)
            }
        }
        if is_subset(learned, KB) {
            return false
        } else {
            KB = set_union(KB, learned)
        }
    }
}
```

For propostional logic, resolve is more easier than in first order logic:

```
function resolve(r1, r2) {
    resolvent := set_union(r1, r2)
    for (c1, c2) in combinations(set_to_list(resolvent), 2) {
        if (not c1) = c2 {
            resolvent.remove(c1)
            resolvent.remove(c2)
        }
    }
    return resolvent
}
```

TODO: for first order logic, unification is required
