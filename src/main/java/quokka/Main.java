package quokka;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/** JavaFX entry point (GUI). */
public class Main extends Application {

    @Override
    public void start(Stage stage) {
        Label helloWorld = new Label("Hello World!");
        Scene scene = new Scene(helloWorld);

        stage.setTitle("Quokka");
        stage.setScene(scene);
        stage.show();
    }

    /** No-arg constructor required by JavaFX. */
    public Main() {

    }
}
