package fol;

import java.util.List;

public class Predicate {
    public String name;
    public List<String> vars;

    public Predicate(String name, List<String> vars) {
        this.name = name;
        this.vars = vars;
    }
}
