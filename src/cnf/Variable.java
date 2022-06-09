package cnf;

import java.util.Objects;

public class Variable implements CNF {
    public String name;

    public Variable(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "?" + name;
    }

    @Override
    public boolean equals(Object obj) {
        var v = (Variable)obj;
        return this.name.equals(v.name);
    }

    @Override
    public int hashCode() {
        return this.name.hashCode();
    }
}
