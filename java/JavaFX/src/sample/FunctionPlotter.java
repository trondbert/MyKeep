package sample;

import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.*;

import static java.lang.Math.*;
import static java.lang.String.format;

public class FunctionPlotter extends Application {

    private final int SCENE_WIDTH = 1000;
    private final int SCENE_HEIGHT = 700;

    private double xMax =  80;
    private double xMin = -80;
    private double yMax =  80;
    private double yMin = -80;

    private final double SCENE_X_MIN = 0;
    private final double SCENE_X_MAX = SCENE_WIDTH;
    //Flippet Y-akse, vi regner utifra vanlig koordinatsystem, der y øker oppover
    private final double SCENE_Y_MIN = SCENE_HEIGHT;
    private final double SCENE_Y_MAX = 0;

    private Map<Node, Node> originalsAndAdjusted = new HashMap<>();

    private Map<NodeID, Text> textMap = new HashMap<>();

    private List<Node> axesAndTicks = createAxes();

    enum NodeID { ANGLE, X_Y }

    @Override
    public void start(Stage primaryStage) throws Exception{
        List<Point2D> functionPoints = new FunctionFactory().getFunctionPoints();
        Group circles = drawFunction(functionPoints);

        Text angleText = textNonAdjusted(5, 15, "Angle (rad): ");
        textMap.put(NodeID.ANGLE, angleText);
        Text xyText = textNonAdjusted(5, 50, "(x,y): ");
        textMap.put(NodeID.X_Y, xyText);

        Group root = new Group();
        root.getChildren().addAll(axesAndTicks);
        root.getChildren().addAll(angleText, xyText);
        root.getChildren().add(circles);

        Scene scene = new Scene(root, SCENE_WIDTH, SCENE_HEIGHT, Color.WHITE);
        setupOnZoomFinished(scene);
        setupOnMouseMoved(scene);

        primaryStage.setTitle("Graphs for Onassis");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Group drawFunction(List<Point2D> functionPoints) {
        int nofPoints = functionPoints.size();
        Group circles = new Group();
        for (int i = 0; i < nofPoints; i++) {
            int opacity = 1;
            int redIncreasing = (int) (i * 255.0 / nofPoints);
            int blueDecreasing = (int) ((nofPoints - i) * 255.0 / nofPoints);
            Color color = Color.rgb(redIncreasing, 0, blueDecreasing, opacity);
            Circle circle = circle(functionPoints.get(i), color);
            circles.getChildren().add(circle);
        }
        return circles;
    }

    private List<Node> createAxes() {
        List<Node> axesAndTicks = new ArrayList<Node>();

        Line xAxis = line(-10000, 0, 10000, 0);
        Line yAxis = line(0, -10000, 0, 10000);

        addXTicks(axesAndTicks);
        addYTicks(axesAndTicks);
        axesAndTicks.add(xAxis);
        axesAndTicks.add(yAxis);

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

        double tickX = ceil(xMin / xStep) * xStep;
        while (tickX < xMax) {
            axesAndTicks.add(line(tickX, -tickLineLength, tickX, tickLineLength));
            String tickText = formattedAxisMark(tickX);
            double textDisplacement = tickText.length() * xWidth / 230;
            axesAndTicks.add(textCentered(tickX - textDisplacement, tickLineLength * 1.4, tickText));
            tickX += xStep;
        }
    }

    private String formattedAxisMark(double position) {
        return new DecimalFormat("##.###").format(position);
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

        double tickY = ceil(yMin / yStep) * yStep;
        while (tickY < yMax) {
            axesAndTicks.add(line(-tickLineLength, tickY, tickLineLength, tickY));
            String tickText = formattedAxisMark(tickY);
            double textDisplacement = ySpan / 140;
            axesAndTicks.add(textCentered(tickLineLength * 1.4, tickY - textDisplacement, tickText));
            tickY += yStep;
        }
    }

    private void setupOnZoomFinished(Scene scene) {
        scene.setOnZoomFinished((event) ->
        {
            Scene source = (Scene) event.getSource();
            Group root = (Group) source.getRoot();

            for (Node axesAndTick : axesAndTicks) {
                root.getChildren().remove(root.getChildren().indexOf(axesAndTick));
            }
            redraw(event.getTotalZoomFactor());

            axesAndTicks = createAxes();
            root.getChildren().addAll(axesAndTicks);
        });
    }

    private void setupOnMouseMoved(Scene scene) {
        scene.setOnMouseMoved(event -> {
            double x = toMathX(event.getX());
            double y = toMathY(event.getY());
            String xyText = format("(x,y): %.3f, %.3f", x, y);
            textMap.get(NodeID.X_Y).setText(xyText);

            double angle = acos(x / sqrt((pow(x, 2) + pow(y, 2)))); //TODO sqrt
            //double angle = acos(1);
            String angleText = format("Angle (rad): %.3f / %.3f", angle, angle - 2 * Math.PI);
            textMap.get(NodeID.ANGLE).setText(angleText);
        });

    }

    private void redraw(double zoomFactor) {
        xMin = xMin / zoomFactor;
        xMax = xMax / zoomFactor;
        yMin = yMin / zoomFactor;
        yMax = yMax / zoomFactor;
        for (Node keyNode : originalsAndAdjusted.keySet()) {
            if (keyNode instanceof Circle) {
                Circle circleOriginal = (Circle) keyNode;
                Circle circle = (Circle) originalsAndAdjusted.get(keyNode);
                circle.setCenterX(adjustedX(circleOriginal.getCenterX()));
                circle.setCenterY(adjustedY(circleOriginal.getCenterY()));
            }
            else if (keyNode instanceof Line) {
                Line lineOriginal = (Line) keyNode;
                Line line = (Line) originalsAndAdjusted.get(keyNode);
                line.setStartX(adjustedX(lineOriginal.getStartX()));
                line.setStartY(adjustedY(lineOriginal.getStartY()));
                line.setEndX(adjustedX(lineOriginal.getEndX()));
                line.setEndY(adjustedY(lineOriginal.getEndY()));
            }
            else if (keyNode instanceof Text) {
                Text lineOriginal = (Text) keyNode;
                Text line = (Text) originalsAndAdjusted.get(keyNode);
                line.setX(adjustedX(lineOriginal.getX()));
                line.setX(adjustedY(lineOriginal.getY()));
            }
        }
    }

    private Line line(double startX, double startY, double endX, double endY) {
        Line line = new Line(adjustedX(startX), adjustedY(startY),
                             adjustedX(endX),   adjustedY(endY));
        line.setStroke(Color.web("black", 1));
        line.setFill(Color.web("black", 1));
        line.setStrokeWidth(1);
        originalsAndAdjusted.put(new Line(startX, startY, endX, endY), line);
        return line;
    }

    private Circle circle(Point2D functionPoint, Color color) {
        Circle circle = new Circle(0.5, color);
        circle.setCenterX(adjustedX(functionPoint.getX()));
        circle.setCenterY(adjustedY(functionPoint.getY()));
        circle.setStrokeType(StrokeType.OUTSIDE);
        circle.setStroke(color);
        circle.setStrokeWidth(0.5);

        originalsAndAdjusted.put(
                new Circle(functionPoint.getX(), functionPoint.getY(), 1.0),
                circle);
        return circle;
    }

    private Text textNonAdjusted(double x, double y, String text) {
        return new Text(x, y, text);
    }

    private Text text(double x, double y, String text) {
        Text text1 = new Text(adjustedX(x), adjustedY(y), text);
        text1.setTextAlignment(TextAlignment.CENTER);
        return text1;
    }

    private Text textCentered(double x, double y, String text) {
        Text textNode = text(x, y, text);
        textNode.setTextAlignment(TextAlignment.CENTER);
        return textNode;
    }

    private double adjustedY(double y) {
        return SCENE_Y_MIN +
                (SCENE_Y_MAX - SCENE_Y_MIN) *
                        ((y - yMin) / (yMax - yMin));
    }

    private double adjustedX(double x) {
        return SCENE_X_MIN +
                (SCENE_X_MAX - SCENE_X_MIN) *
                        ((x - xMin) / (xMax - xMin));
    }

    private double toMathX(double sceneX) {
        return xMin +
                (xMax - xMin) *
                        ((sceneX - SCENE_X_MIN) / (SCENE_X_MAX - SCENE_X_MIN));
    }

    private double toMathY(double sceneY) {
        return yMin +
                (yMax - yMin) *
                        ((sceneY - SCENE_Y_MIN) / (SCENE_Y_MAX - SCENE_Y_MIN));
    }

    public static void main(String[] args) {
        launch(args);
    }

    <T> T transform(T operand, UnaryOperator<T> operator) {
        return operator.apply(operand);
    }
}
