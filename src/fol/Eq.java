package fol;

import cnf.Not;

public class Eq implements FOL {
    public FOL left;
    public FOL right;

    public Eq(FOL visitTerm, FOL visitTerm1) {
        left = visitTerm;
        right = visitTerm1;
    }

    @Override
    public String toString() {
        return left.toString()+" ≡ "+ right.toString();
    }
}
