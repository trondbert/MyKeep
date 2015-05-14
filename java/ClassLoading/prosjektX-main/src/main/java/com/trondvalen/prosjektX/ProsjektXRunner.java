package com.trondvalen.prosjektX;

import java.util.Arrays;
import java.util.List;
import java.util.ServiceLoader;

public class ProsjektXRunner {

    private static ServiceLoader<ElementReverser> serviceLoader = ServiceLoader.load(ElementReverser.class);
    private ElementReverser reverser;

    public static void main(String[] args) {
        new ProsjektXRunner().run();
    }

    private void run() {
        System.out.println("Implementors");
        ElementReverser reverser = getReverser();
        if (reverser == null) {
            System.out.println("Couldn't find an implementor. Exiting");
            return;
        }
        List<String> list = Arrays.asList("ball", "agnesisenga", "effekt", "frakt");
        System.out.println("Result: " + reverser.work(list));
    }

    ElementReverser getReverser() {
        if (this.reverser != null) {
            return this.reverser;
        }
        System.out.printf("");

        for (ElementReverser implementor : serviceLoader) {
            if (implementor != null) {
                this.reverser = implementor;
                System.out.println(implementor.getClass().getCanonicalName());
            }
        }
        return this.reverser;
    }
}
