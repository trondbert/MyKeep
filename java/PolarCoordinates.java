import javafx.application.Application;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.Exception;
import java.lang.Override;

/**
 * Created by trond on 14/07/15.
 */
public class PolarCoordinates extends Application {

    public static void main(String... args) {
        launch(args);

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("numbers.csv"));

            for (double angle = -2 * Math.PI; angle < 4 * Math.PI; ) {

                double functionValue = Math.pow(angle, 2.0) - 2 * angle;

                double x = Math.cos(angle) * functionValue;
                double y = Math.sin(angle) * functionValue;

                writer.write(String.format("%f\t%f\n", x, y));

                angle += Math.max(0.1, 1.0 / Math.log(Math.abs(functionValue)));
            }

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Hello World!");
        Button btn = new Button();
        btn.setText("Say 'Hello World'");
        btn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                System.out.println("Hello World!");
            }
        });

        StackPane root = new StackPane();
        root.getChildren().add(btn);
        primaryStage.setScene(new Scene(root, 300, 250));
        primaryStage.show();
    }
}
