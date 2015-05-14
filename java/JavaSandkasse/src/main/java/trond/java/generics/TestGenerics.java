package trond.java.generics;

import java.util.ArrayList;
import java.util.List;

public class TestGenerics {

    public static void main(String ... args) {
        ObjectHolder oh = new ObjectHolder();

        System.out.println(oh);

        Object something = oh.doSomething("");

        List myList = new ArrayList();
    }


}

class ObjectHolder<T> {

    T object;

    public T doSomething(T s) {
        return s;
    }
}
