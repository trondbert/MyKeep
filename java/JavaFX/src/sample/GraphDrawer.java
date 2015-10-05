package sample;

import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.ceil;
import static java.lang.Math.log10;
import static java.lang.Math.pow;
import static sample.GraphApp.*;

public class GraphDrawer {

    List<Node> nodes = new ArrayList<>();

    void redraw(List<Point2D> mathPoints) {
        nodes.clear();
        drawFunction(mathPoints);
        createAxes();
    }

    List<Node> drawFunction(List<Point2D> mathPoints) {
        List<Node> functionPoints = new ArrayList<>();
        int nofPoints = mathPoints.size();
        for (int i = 0; i < nofPoints; i++) {
            int opacity = 1;
            int redIncreasing = (int) (i * 255.0 / nofPoints);
            int blueDecreasing = (int) ((nofPoints - i) * 255.0 / nofPoints);
            Color color = Color.rgb(redIncreasing, 0, blueDecreasing, opacity);
            Circle circle = circle(mathPoints.get(i), color);
            functionPoints.add(circle);
        }
        nodes.addAll(functionPoints);
        return functionPoints;
    }

    List<Node> createAxes() {
        List<Node> axesAndTicks = new ArrayList<>();

        Line xAxis = line(-100000, 0, 100000, 0);
        Line yAxis = line(0, -100000, 0, 100000);
        addXTicks(axesAndTicks);
        addYTicks(axesAndTicks);
        axesAndTicks.add(xAxis);
        axesAndTicks.add(yAxis);

        nodes.addAll(axesAndTicks);
        return axesAndTicks;
    }


    private void addXTicks(List<Node> axesAndTicks) {
        double xWidth = xMax - xMin;
        double xStep = transform(pow(10, (int) log10(xWidth)),
                (Double val) -> {
                    double chunkCount = xWidth / val;
                    if (chunkCount < 4) return val / 5.0;
                    if (chunkCount > 8) return val * 2;
                    return val;
                });
        double tickLineLength = xWidth / 200.0;

        for (double tickX = ceil(xMin / xStep) * xStep;  tickX < xMax; tickX += xStep) {
            if (tickX == 0.0) continue;
            axesAndTicks.add(line(tickX, -tickLineLength, tickX, tickLineLength));
            String tickText = formattedAxisMark(tickX);
            double textDisplacement = tickText.length() * xWidth / 230;
            axesAndTicks.add(text(tickX - textDisplacement, tickLineLength * 1.4, tickText));
        }
    }

    private String formattedAxisMark(double position) {
        return floatFormat.format(position);
    }

    private void addYTicks(List<Node> axesAndTicks) {
        double ySpan = yMax - yMin;
        double yStep = transform(pow(10, (int) log10(ySpan)),
                (Double val) -> {
                    double chunkCount = ySpan / val;
                    if (chunkCount < 4) return val / 5.0;
                    if (chunkCount > 8) return val * 2;
                    return val;
                });
        double tickLineLength = ySpan / 200.0;

        for (double tickY = ceil(yMin / yStep) * yStep; tickY < yMax; tickY += yStep) {
            if (tickY == 0.0) continue;
            axesAndTicks.add(line(-tickLineLength, tickY, tickLineLength, tickY));
            String tickText = formattedAxisMark(tickY);
            double textDisplacement = ySpan / 140;
            axesAndTicks.add(text(tickLineLength * 1.4, tickY - textDisplacement, tickText));
        }
    }

    private Circle circle(Point2D functionPoint, Color color) {
        Circle circle = new Circle(0.5, color);
        circle.setCenterX(adjustedX(functionPoint.getX()));
        circle.setCenterY(adjustedY(functionPoint.getY()));
        circle.setStrokeType(StrokeType.OUTSIDE);
        circle.setStroke(color);
        circle.setStrokeWidth(0.5);

        return circle;
    }

    private Line line(double startX, double startY, double endX, double endY) {
        Line line = new Line(adjustedX(startX), adjustedY(startY), adjustedX(endX), adjustedY(endY));
        line.setStroke(Color.web("black", 1));
        line.setFill(Color.web("black", 1));
        line.setStrokeWidth(1);
        return line;
    }

    private Text text(double x, double y, String text) {
        return new Text(adjustedX(x), adjustedY(y), text);
    }

    private double adjustedY(double y) {
        return SCENE_Y_MIN +
                ((SCENE_Y_MAX - SCENE_Y_MIN) *
                        ((y - yMin) / (yMax - yMin)));
    }

    private double adjustedX(double x) {
        return SCENE_X_MIN +
                (SCENE_X_MAX - SCENE_X_MIN) *
                        ((x - xMin) / (xMax - xMin));
    }
}
