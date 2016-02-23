package sample;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.DoubleUnaryOperator;

/**
 * @author trond.
 */
class FxParam {
    public static final Map<String, FxParam> instances;
    static {
        instances = new HashMap<>();
        FxParam s = new FxParam("s", 0.1, val -> val * 10, val -> val / 10);
        new FxParam("a", 1.0, val -> val + s.value, val -> val - s.value);
        new FxParam("b", 1.0, val -> val + s.value, val -> val - s.value);
        new FxParam("c", 1.0, val -> val + s.value, val -> val - s.value);
    }

    private double value;
    DoubleUnaryOperator increase;
    DoubleUnaryOperator decrease;

    FxParam(String name, Double value, DoubleUnaryOperator increase, DoubleUnaryOperator decrease ) {
        instances.put(name, this);
        this.value = value;
        this.increase = increase;
        this.decrease = decrease;
    }
    public void change(DoubleUnaryOperator changeMethod) {
        value = changeMethod.applyAsDouble(value);
    }
    public Double get() {
        return value;
    }
    static FxParam param(String name) {
        return instances.get(name);
    }
    public static Collection<FxParam> params() {
        return instances.values();
    }
}
