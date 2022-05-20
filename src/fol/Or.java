package fol;

public class Or implements FOL {
    public FOL left;
    public FOL right;

    public Or(FOL left, FOL right) {
        this.left = left;
        this.right = right;
    }
}
