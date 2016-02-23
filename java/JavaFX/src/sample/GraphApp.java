package sample;

import static java.lang.Math.*;
import static java.lang.String.format;
import static sample.FxParam.param;
import static sample.FxParam.params;
import static sample.FxSettings.*;

import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.DoubleUnaryOperator;
import java.util.function.Function;

import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class GraphApp extends Application {

    private static Map<Object, Text> textMap = new HashMap<>();

    private static Map<FxParam, Double> functionParams = new TreeMap<>();
    private GraphDrawer graphDrawer = new GraphDrawer();
    private FxParam controlledParameter;

    enum NodeID { ANGLE, X_Y }

    @Override
    public void start(Stage primaryStage) throws Exception {
        floatFormat = new DecimalFormat("##.###");
        FxSettings.xMax = 4;
        FxSettings.xMin = -4;
        FxSettings.yMax = 4;
        FxSettings.yMin = -4;

        controlledParameter = params().stream().findFirst().get();

        textMap.put(NodeID.ANGLE, new Text(5, 15, "Angle (rad): "));
        textMap.put(NodeID.X_Y, new Text(5, 50, "(x,y): "));
        textMap.put("a", new Text(5,  70, "a: "    + floatFormat.format(param("a").get())));
        textMap.put("b", new Text(5,  90, "b: "    + floatFormat.format(param("b").get())));
        textMap.put("c", new Text(5, 110, "c: "    + floatFormat.format(param("c").get())));
        textMap.put("s", new Text(5, 130, "step: " + floatFormat.format(param("s").get())));

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
            if (event.getText() != null) {
                final FxParam param = FxParam.param(event.getText());
                if (param != null) {
                    controlledParameter = param;
                    return;
                }
            }

            DoubleUnaryOperator changeParam =
                ((Function<KeyCode, DoubleUnaryOperator>) (eventCode -> {
                    switch (eventCode) {
                        case DOWN:  return controlledParameter.decrease;
                        case UP:    return controlledParameter.increase;
                        default:    return null;
                    }
                })).apply(event.getCode());

            if (changeParam == null) {
                return;
            }

            Double paramValue = functionParams.get(controlledParameter);
            double newValue = changeParam.applyAsDouble(paramValue);
            functionParams.put(controlledParameter, newValue);

            Text paramText = textMap.get(controlledParameter);
            paramText.setText(
                    MessageFormat.format("{0}: {1}",
                            controlledParameter,
                            floatFormat.format(newValue)));
            redraw(1, (Group) scene.getRoot());
        });
    }

    private void redraw(double zoomFactor, Group root) {
        xMin = xMin / zoomFactor;
        xMax = xMax / zoomFactor;
        yMin = yMin / zoomFactor;
        yMax = yMax / zoomFactor;

        root.getChildren().removeAll(graphDrawer.nodes);

        List<Point2D> mathPoints = new FunctionFactory().getFunctionPoints(FxParam.instances);
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
}

