package fol;

public class Or implements FOL {
    public FOL left;
    public FOL right;

    public Or(FOL visitTerm, FOL visitTerm1) {
        left = visitTerm;
        right = visitTerm1;
    }

    @Override
    public String toString() {
        return left.toString()+" ∨ "+ right.toString();
    }
}
