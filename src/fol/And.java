package fol;

public class And implements FOL {
    public FOL left;
    public FOL right;

    public And(FOL remove_implication, FOL remove_implication1) {
        this.left = remove_implication;
        this.right = remove_implication1;
    }
}
