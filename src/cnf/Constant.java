package cnf;

public class Constant implements CNF {
    String c;

    public Constant(String c) {
        this.c = c;
    }

    @Override
    public String toString() {
        return c;
    }
}
