package cnf;

import cnf.CNF;

public class Skolem implements CNF {
    public int unique_id;
    public String var;

    public Skolem(int unique_id, String v) {
        this.unique_id = unique_id;
        this.var = v;
    }
}
