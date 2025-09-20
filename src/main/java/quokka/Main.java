package quokka;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * JavaFX entry point for the Quokka GUI.
 * Implements an asymmetric chat layout with Hollow Knight–inspired CSS,
 * distinct error highlighting, and responsive resizing.
 */
public class Main extends Application {

    private Quokka quokka;

    private VBox dialog;
    private ScrollPane scroller;
    private javafx.scene.image.Image appIcon;

    @Override
    public void start(Stage stage) {
        // ----- Root / Header -----
        BorderPane root = new BorderPane();
        root.getStyleClass().add("hk-pane");

        // ----- Header (icon + title) -----
        javafx.scene.image.ImageView hv = new javafx.scene.image.ImageView(appIcon);
        hv.setFitWidth(18);
        hv.setFitHeight(18);
        hv.setPreserveRatio(true);

        javafx.scene.shape.Circle hClip = new javafx.scene.shape.Circle(9, 9, 9);
        hv.setClip(hClip);

        Label header = new Label("Quokka");
        header.getStyleClass().add("hk-header");

        HBox headerBox = new HBox(hv, header);
        headerBox.getStyleClass().add("header-box");
        root.setTop(headerBox);

        // ----- Dialog Area (asymmetric) -----
        dialog = new VBox();
        dialog.getStyleClass().add("vbox");

        scroller = new ScrollPane(dialog);
        scroller.setFitToWidth(true);
        scroller.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroller.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scroller.setPannable(true);
        root.setCenter(scroller);

        // Auto-scroll to bottom on new messages
        dialog.heightProperty().addListener((obs, oldVal, newVal) -> scroller.setVvalue(1.0));

        // ----- Input Bar -----
        TextField input = new TextField();
        input.setPromptText("Type a command…");

        Button send = new Button("Send");

        HBox inputBar = new HBox(input, send);
        inputBar.getStyleClass().add("input-bar");
        HBox.setHgrow(input, Priority.ALWAYS);

        root.setBottom(inputBar);

        // ----- Handlers -----
        send.setOnAction(e -> {
            String cmd = input.getText() == null ? "" : input.getText().trim();
            if (cmd.isEmpty()) {
                return;
            }

            addUser(cmd);

            Reply r = quokka.process(cmd);
            addBot(r.message, r.error);

            input.clear();

            if (r.exit) {
                stage.close();
            }
        });


        input.setOnAction(send.getOnAction());

        // ----- Scene / Stage -----
        Scene scene = new Scene(root, 420, 560);
        scene.getStylesheets().add(
            getClass().getResource("/quokka/view/view/hollowknight.css").toExternalForm()
        );

        appIcon = new javafx.scene.image.Image(
            getClass().getResourceAsStream("/quokka/img/knight.png"));
        stage.getIcons().add(appIcon);

        stage.setTitle("Quokka");
        stage.setMinWidth(360);
        stage.setMinHeight(420);
        stage.setResizable(true);
        stage.setScene(scene);
        quokka = new Quokka("data/tasks.txt");
        stage.show();

        addBot("Hello! I’m Quokka. Type a command.", false);
    }

    private void addBot(String text, boolean isError) {
        Label bubble = new Label(text);
        bubble.setWrapText(true);
        bubble.getStyleClass().add("bot-bubble");
        if (isError || text.startsWith("OOPS!!!")) {
            bubble.getStyleClass().add("error-bubble");
        }
        bubble.maxWidthProperty().bind(scroller.widthProperty().subtract(56));

        javafx.scene.image.ImageView avatar = new javafx.scene.image.ImageView(appIcon);
        avatar.setFitWidth(28);
        avatar.setFitHeight(28);
        avatar.setPreserveRatio(true);
        javafx.scene.shape.Circle clip = new javafx.scene.shape.Circle(14, 14, 14);
        avatar.setClip(clip);

        HBox row = new HBox(avatar, bubble);
        row.getStyleClass().add("row-bot");
        dialog.getChildren().add(row);
    }

    private void addUser(String text) {
        Label chip = new Label(text);
        chip.setWrapText(true);
        chip.getStyleClass().add("user-chip");
        chip.maxWidthProperty().bind(scroller.widthProperty().subtract(28));

        HBox row = new HBox(chip);
        row.getStyleClass().add("row-user");
        dialog.getChildren().add(row);
    }
}
