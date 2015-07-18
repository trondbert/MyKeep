package sample;

import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.*;
import java.util.function.*;

import static java.lang.Math.*;
import static java.lang.String.format;
import static sample.GraphApp.NodeID.valueOf;

public class GraphApp extends Application {

    static final int SCENE_WIDTH = 1200;
    static final int SCENE_HEIGHT = 900;

    static final DecimalFormat floatFormat = new DecimalFormat("##.###");

    static double xMax = 4;
    static double xMin = -4;
    static double yMax = 4;
    static double yMin = -4;

    static double SCENE_X_MIN = 0;
    static double SCENE_X_MAX = SCENE_WIDTH;
    //Flippet Y-akse, vi regner utifra vanlig koordinatsystem, der y øker oppover
    static double SCENE_Y_MIN = SCENE_HEIGHT;
    static double SCENE_Y_MAX = 0;

    private Map<NodeID, Text> textMap = new HashMap<>();

    private Map<String, Double> functionParams = new TreeMap<>();
    private GraphDrawer graphDrawer = new GraphDrawer();
    private String controlledParameter;

    enum NodeID {ANGLE, PARAM_A, PARAM_B, X_Y}

    @Override
    public void start(Stage primaryStage) throws Exception {
        functionParams.put("a", 3.0);
        functionParams.put("b", 1.0);
        controlledParameter = functionParams.keySet().iterator().next();

        textMap.put(NodeID.ANGLE, new Text(5, 15, "Angle (rad): "));
        textMap.put(NodeID.X_Y, new Text(5, 50, "(x,y): "));
        textMap.put(NodeID.PARAM_A, new Text(5, 70, "a: " + floatFormat.format(functionParams.get("a"))));
        textMap.put(NodeID.PARAM_B, new Text(5, 90, "b: " + floatFormat.format(functionParams.get("b"))));

        Group root = new Group();
        root.getChildren().addAll(textMap.values());

        Scene scene = new Scene(root, SCENE_WIDTH, SCENE_HEIGHT, Color.WHITE);
        redraw(1, root);
        setupOnZoomFinished(scene);
        setupOnMouseMoved(scene);
        setupOnKeyPressed(scene);

        primaryStage.setTitle("Graphs for Onassis");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void setupOnZoomFinished(Scene scene) {
        scene.setOnZoomFinished((event) ->
        {
            double zoomFactor = event.getTotalZoomFactor();
            Scene source = (Scene) event.getSource();
            Group root = (Group) source.getRoot();
            redraw(zoomFactor, root);
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

    void setupOnKeyPressed(Scene scene) {
        scene.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.UP)) {
                Double newValue = functionParams.get(controlledParameter) + 0.2;
                functionParams.put(controlledParameter, newValue);
            }
            else if (event.getCode().equals(KeyCode.DOWN)) {
                Double newValue = functionParams.get(controlledParameter) - 0.2;
                functionParams.put(controlledParameter, newValue);
            }
            else if (event.getText() != null && functionParams.get(event.getText()) != null) {
                controlledParameter = event.getText();
            }
            else
                return;

            Text paramText = textMap.get(valueOf("PARAM_" + controlledParameter.toUpperCase()));
            String newValue = floatFormat.format(functionParams.get(controlledParameter));
            paramText.setText(MessageFormat.format("{0}: {1}", controlledParameter, newValue));
            redraw(1, (Group) scene.getRoot());
        });
    }

    private void redraw(double zoomFactor, Group root) {
        xMin = xMin / zoomFactor;
        xMax = xMax / zoomFactor;
        yMin = yMin / zoomFactor;
        yMax = yMax / zoomFactor;

        root.getChildren().removeAll(graphDrawer.nodes);

        List<Point2D> mathPoints = new FunctionFactory().getFunctionPoints(functionParams);
        graphDrawer.redraw(mathPoints);
        root.getChildren().addAll(graphDrawer.nodes);
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

    static <T> T transform(T operand, UnaryOperator<T> operator) {
        return operator.apply(operand);
    }
}