package trond.java.jaxb.mapping;

/**
 * @author trond.
 */
public class TestShutdownHook {

    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread() {

            @Override
            public void run() {
                System.out.println("Hook");
            }
        });
        if (1 == 1)
            throw new RuntimeException("fdskl");
        System.exit(0);
    }

}
