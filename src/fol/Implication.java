package fol;

import java.util.concurrent.ConcurrentLinkedDeque;

public class Implication implements FOL {
    private final FOL left;
    private final FOL right;

    public Implication(FOL left, FOL right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public String toString() {
               return left.toString() + " => " + right.toString();
    }
}
