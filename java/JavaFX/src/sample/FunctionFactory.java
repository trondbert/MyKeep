package sample;

import javafx.geometry.Point2D;

import java.util.*;
import java.util.List;
import java.util.function.DoubleUnaryOperator;
import java.util.function.Function;

import static java.lang.Math.PI;

public class FunctionFactory {

    public List<Point2D> getFunctionPoints() {

        return makePoints(angle -> Math.tan(angle),
                            0,
                            4 * PI,
                            0.005);
    }

    private List<Point2D> makePoints(DoubleUnaryOperator function, double minAngle, double maxAngle, double stepAngle) {
        List<Point2D> points = new ArrayList<>();
        for (double angle = minAngle; angle < maxAngle; angle += stepAngle) {
            double functionValue = function.applyAsDouble(angle);
            double x = Math.cos(angle) * functionValue;
            double y = Math.sin(angle) * functionValue;
            points.add(new Point2D(x, y));
        }
        return points;
    }
}
