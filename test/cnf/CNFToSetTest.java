package cnf;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import java.util.HashSet;
import java.util.List;

public class CNFToSetTest {
    public HashSet<CNF> cnf_to_set(CNF expr) {
        switch (expr) {
            case Or or -> {
                return new HashSet<>(or.subexprs);
            }
            case And and -> {
                return new HashSet<>(and.subexprs);
            }
            default -> throw new IllegalStateException("Unexpected value: " + expr);
        }
    }

    @Test
    public void x_or_y() {
        var r = cnf_to_set(new Or(new Variable("x"), new Variable("y")));
        assertEquals(r, new HashSet<>(List.of(new Variable("x"), new Variable("y"))));
    }

    @Test
    public void x_or_y_or_z() {
        var r = cnf_to_set(
                new Or(new Variable("x"),
                        new Variable("y"),
                        new Variable("z")));
        assertEquals(r, new HashSet<>(List.of(new Variable("x"),
                new Variable("y"),
                new Variable("z")
                )));
    }
}
