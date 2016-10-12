package sample;

import static java.lang.Math.PI;
import static java.lang.Math.pow;
import static sample.FxSettings.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.DoubleUnaryOperator;

import javafx.geometry.Point2D;

public class FunctionFactory {

    public List<Point2D> getFunctionPoints(final Map<String, FxParam> params) {
        Double a = params.get("a").get();
        Double b = params.get("b").get();
        Double c = params.get("c").get();

        List<Point2D> pointsPolar = makePointsPolar(angle -> (a * Math.pow(angle, 2) + b * angle + c),
                -2 * PI,
                2 * PI,
                0.005);

        List<Point2D> points = makePointsPolar(x -> b - pow((x - a), 2),
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
                Math.pow(xMax - xMin, 2) +
                Math.pow(yMax - yMin, 2));
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
