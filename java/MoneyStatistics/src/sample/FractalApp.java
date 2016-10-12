package sample;

import static java.lang.Math.PI;
import static java.util.Arrays.asList;
import static sample.FxParam.params;
import static sample.FxSettings.*;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.function.DoubleUnaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

public class FractalApp extends Application {

    private GraphDrawer graphDrawer = new GraphDrawer();

    private FxParam controlledParameter;

    private double lengthLastStroke = 2.0;

    private long tick = 0;

    List<Line> lastLines = new ArrayList<>();

    @SuppressWarnings("InfiniteLoopStatement")
    @Override
    public void start(Stage primaryStage) throws Exception {
        floatFormat = new DecimalFormat("##.###");
        FxSettings.xMax = 4;
        FxSettings.xMin = -4;
        FxSettings.yMax = 4;
        FxSettings.yMin = -4;

        controlledParameter = params().stream().findFirst().get();

        Group root = new Group();

        Scene scene = new Scene(root, SCENE_WIDTH, SCENE_HEIGHT, Color.WHITE);
        redraw(1, root);
        setupOnZoomFinished(scene);
        setupOnKeyPressed(scene, root);

        primaryStage.setTitle("Graphs for Onassis");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void redraw(final double zoomFactor, Group root) {
        xMin = xMin / zoomFactor;
        xMax = xMax / zoomFactor;
        yMin = yMin / zoomFactor;
        yMax = yMax / zoomFactor;

        if (lastLines.isEmpty()) {
            final int xMinTriangle = 100;
            final int xMaxTriangle = 1100;
            final int xMidTriangle = (xMaxTriangle + xMinTriangle) / 2;
            final int yMaxTriangle = 600;
            final double h = (xMidTriangle - xMinTriangle) * Math.sin(60 * PI / 180.0);
            final Line line1 = graphDrawer.rawLine(xMinTriangle, yMaxTriangle, xMidTriangle, yMaxTriangle - h);
            final Line line2 = graphDrawer.rawLine(xMidTriangle, yMaxTriangle - h, xMaxTriangle, yMaxTriangle);
            final Line line3 = graphDrawer.rawLine(xMaxTriangle, yMaxTriangle, xMinTriangle, yMaxTriangle);
            root.getChildren().addAll(line1, line2, line3);
            lastLines.addAll(asList(line1, line2, line3));
            return;
        }

        root.getChildren().clear();
        lastLines = withSpikes(lastLines);
        root.getChildren().addAll(lastLines);
    }

    private List<Line> withSpikes(final List<Line> lastLines) {
        final List<Line> result = new ArrayList<>();
        for (Line line : lastLines) {
            final double dxIn = line.getEndX() - line.getStartX();
            final double dyIn = line.getEndY() - line.getStartY();

            Line line1 = graphDrawer.rawLine(line.getStartX(), line.getStartY(),
                                             line.getStartX() + dxIn / 3.0, line.getStartY() + dyIn / 3.0);
            final Line line2a = rotate(line1, 60 * PI / 180.0, line1.getEndX(), line1.getEndY());
            final Line line2b = rotate(line1, -60 * PI / 180.0, line1.getEndX(), line1.getEndY());

            final double x2_3 = line.getStartX() + dxIn * 2.0/3.0;
            final double y2_3 = line.getStartY() + dyIn * 2.0/3.0;

            final Line line3a = graphDrawer.rawLine(line2a.getEndX(), line2a.getEndY(), x2_3, y2_3);
            final Line line3b = graphDrawer.rawLine(line2b.getEndX(), line2b.getEndY(), x2_3, y2_3);

            final Line line4 = graphDrawer.rawLine(line3a.getEndX(), line3a.getEndY(), line.getEndX(), line.getEndY());

            result.addAll(asList(line1, line2a, line2b, line3a, line3b, line4));
        }
        return result;
    }

    private Line rotate(Line line, double theta, double startX, double startY) {
        final double dxIn = line.getEndX() - line.getStartX();
        final double dyIn = line.getEndY() - line.getStartY();

        final double cosTheta = Math.cos(theta);
        final double sinTheta = Math.sin(theta);

        final double dxOut = dxIn * cosTheta + dyIn * sinTheta;
        final double dyOut = dxIn * -sinTheta + dyIn * cosTheta;

        return graphDrawer.rawLine(startX, startY,
                                   startX + dxOut, startY + dyOut);
    }

    private void setupOnZoomFinished(Scene scene) {
        scene.setOnZoomFinished((event) -> {
            Supplier<Group> sceneRoot =  () -> (Group) ((Scene) event.getSource()).getRoot();
            redraw(event.getTotalZoomFactor(), sceneRoot.get());
        });
    }

    void setupOnKeyPressed(Scene scene, final Group root) {
        scene.setOnKeyPressed(event -> {
            redraw(1, root);
            if (event.getText() != null) {
                final FxParam param = FxParam.param(event.getText());
                if (param != null) {
                    controlledParameter = param;
                    return;
                }
            }

            DoubleUnaryOperator changeMethod = ((Function<KeyCode, DoubleUnaryOperator>) (eventCode -> {
                switch (eventCode) {
                    case UP:
                        return controlledParameter.increase;
                    case DOWN:
                        return controlledParameter.decrease;
                    default:
                        return null;
                }
            })).apply(event.getCode());

            if (changeMethod == null) {
                return;
            }

            controlledParameter.change(changeMethod);
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}

