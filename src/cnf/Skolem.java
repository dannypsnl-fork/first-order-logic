package cnf;

public class Skolem implements CNF {
    public int unique_id;
    public Variable var;

    public Skolem(int unique_id, Variable v) {
        this.unique_id = unique_id;
        this.var = v;
    }

    @Override
    public String toString() {
        return "Skolem" + unique_id + "(" + var.toString() + ")";
    }
}
