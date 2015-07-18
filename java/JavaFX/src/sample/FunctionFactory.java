package sample;

import javafx.geometry.Point2D;

import java.util.*;
import java.util.List;
import java.util.function.DoubleUnaryOperator;

import static java.lang.Math.PI;
import static java.lang.Math.pow;

public class FunctionFactory {

    public List<Point2D> getFunctionPoints(Map<String, Double> params) {
        Double a = params.get("a");
        Double b = params.get("b");

        List<Point2D> pointsPolar = makePointsPolar(angle -> Math.log(b*angle + (a * PI)),
                -2 * PI,
                2 * PI,
                0.005);

        List<Point2D> points = makePointsPolar(x -> 3 - pow((x - a), 2),
                -10,
                10,
                0.01
        );

        return pointsPolar;
    }

    private List<Point2D> makePointsPolar(DoubleUnaryOperator function,
                                          double minAngle,
                                          double maxAngle,
                                          double stepAngle) {
        List<Point2D> points = new ArrayList<>();
        double last = 0;
        double functionValue = 0;
        double graphDiagonal = Math.sqrt(
                    Math.pow(GraphApp.xMax - GraphApp.xMin, 2) +
                    Math.pow(GraphApp.yMax - GraphApp.yMin, 2));
        //(functionValue - last) > graphDiagonal / 2000 ? stepAngle / 12 : stepAngle

        for (double angle = minAngle; angle < maxAngle; ) {
            last = functionValue;
            functionValue = function.applyAsDouble(angle);
            double x = Math.cos(angle) * functionValue;
            double y = Math.sin(angle) * functionValue;
            points.add(new Point2D(x, y));
            angle += stepAngle;
        }
        return points;
    }

    private List<Point2D> makePointsCartesian(DoubleUnaryOperator function, double minX, double maxX, double stepX) {
        List<Point2D> points = new ArrayList<>();
        for (double x = minX; x < maxX; ) {
            double y = function.applyAsDouble(x);
            points.add(new Point2D(x, y));
            x += stepX; // (functionValue - last) > graphDiagonal / 2000 ? stepAngle / 12 : stepAngle;
        }
        return points;
    }
}
