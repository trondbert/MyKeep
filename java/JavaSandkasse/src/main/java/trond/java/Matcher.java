package trond.java;

/**
 * @author trond.
 */
public interface Matcher {

    static Matcher eq(final Object o) {
        return o::equals;
    }

    static Matcher notNull() {
        return (o) -> o != null;
    }

    boolean matches(Object oGiven);

}
