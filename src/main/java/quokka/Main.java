package quokka;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Objects;

/**
 * JavaFX entry point for the Quokka GUI.
 * Asymmetric chat layout, centered column, avatars, error highlighting, responsive resizing.
 */
public class Main extends Application {

    private Quokka quokka;

    private VBox dialog;
    private ScrollPane scroller;
    private VBox lane;

    private Image botAvatar;
    private Image userAvatar;

    @Override
    public void start(Stage stage) {
        // ----- Root -----
        BorderPane root = new BorderPane();
        root.getStyleClass().add("hk-pane");
        root.setPadding(new Insets(12));

        // ----- Header -----
        HBox headerBar = new HBox(10);
        headerBar.getStyleClass().add("hk-headerbar");

        ImageView titleIcon = loadIconView("/images/knight.png", 22);
        Label header = new Label(AppInfo.PRODUCT_NAME);
        header.getStyleClass().add("hk-header");
        headerBar.getChildren().addAll(
            titleIcon != null ? titleIcon : new Label(), header
        );
        root.setTop(headerBar);

        // ----- Dialog Area (centered column) -----
        dialog = new VBox(10);
        dialog.setFillWidth(true);

        lane = new VBox(dialog);
        lane.setAlignment(Pos.TOP_CENTER);
        lane.setPadding(new Insets(8, 12, 12, 12));
        lane.setMaxWidth(720);
        lane.getStyleClass().add("lane");

        StackPane content = new StackPane(lane);
        StackPane.setAlignment(lane, Pos.TOP_CENTER);

        scroller = new ScrollPane(content);
        scroller.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        content.setStyle("-fx-background-color: transparent;");
        lane.setStyle("-fx-background-color: transparent;");
        dialog.setStyle("-fx-background-color: transparent;");

        scroller.setFitToWidth(true);
        scroller.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroller.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scroller.setPannable(true);

        // Auto-scroll to bottom on new messages
        dialog.heightProperty().addListener((obs, oldVal, newVal) -> scroller.setVvalue(1.0));

        root.setCenter(scroller);

        // ----- Input Bar -----
        TextField input = new TextField();
        input.setPromptText("Type a command…");
        input.getStyleClass().add("hk-input");

        Button send = new Button("Send");
        send.getStyleClass().add("hk-send");
        send.setDefaultButton(true); // Enter triggers

        HBox inputBar = new HBox(10, input, send);
        inputBar.getStyleClass().add("input-bar");
        inputBar.setPadding(new Insets(10, 12, 8, 12));
        HBox.setHgrow(input, Priority.ALWAYS);
        root.setBottom(inputBar);

        // ----- Handlers -----
        send.setOnAction(e -> {
            String cmd = input.getText() == null ? "" : input.getText().trim();
            if (cmd.isEmpty()) return;

            addUser(cmd);

            Reply r = quokka.process(cmd);
            addBot(r.message, r.error);

            input.clear();
            if (r.exit) stage.close();
        });
        input.setOnAction(send.getOnAction());

        // Ctrl+L: clear chat
        root.setOnKeyPressed(ev -> {
            switch (ev.getCode()) {
                case L:
                    if (ev.isControlDown()) {
                        dialog.getChildren().clear();
                        addBot("Cleared.", false);
                    }
                    break;
                default:
            }
        });

        // ----- Scene / Stage -----
        Scene scene = new Scene(root, 560, 720);
        scene.getStylesheets().add(
            Objects.requireNonNull(
                getClass().getResource("/quokka/view/hollowknight.css")
            ).toExternalForm()
        );

        stage.setTitle(AppInfo.PRODUCT_NAME);
        stage.setMinWidth(420);
        stage.setMinHeight(540);
        stage.setResizable(true);
        stage.setScene(scene);

        // Load icon + avatars from classpath (works in fat JAR)
        Image botIcon = loadImage("/images/knight.png");
        Image userIcon = loadImage("/images/user.png");

        if (botIcon != null) {
            stage.getIcons().add(botIcon);
            botAvatar = botIcon;
        }
        if (userIcon != null) {
            userAvatar = userIcon;
        } else {
            userAvatar = botIcon;
        }

        quokka = new Quokka("data/tasks.txt");
        stage.show();

        addBot("Hello! I’m " + AppInfo.PRODUCT_NAME + ". Type a command.", false);
    }

    // -------- Chat row builders --------

    private void addBot(String text, boolean isError) {
        Label bubble = new Label(text);
        bubble.setWrapText(true);
        bubble.getStyleClass().add("bot-bubble");
        if (isError || text.startsWith("OOPS!!!")) {
            bubble.getStyleClass().add("error-bubble");
        }

        bubble.maxWidthProperty().bind(lane.widthProperty().subtract(120));

        HBox row = new HBox(10);
        row.getStyleClass().add("row");
        row.setAlignment(Pos.TOP_LEFT);

        ImageView avatar = avatarView(botAvatar, 42, false);

        if (avatar != null) {
            row.getChildren().addAll(avatar, bubble);
        } else {
            row.getChildren().add(bubble);
        }

        dialog.getChildren().add(row);
    }

    private void addUser(String text) {
        Label chip = new Label(text);
        chip.setWrapText(true);
        chip.getStyleClass().add("user-chip");
        chip.maxWidthProperty().bind(lane.widthProperty().subtract(120));

        HBox row = new HBox(10);
        row.getStyleClass().add("row");
        row.setAlignment(Pos.TOP_RIGHT);

        ImageView avatar = avatarView(userAvatar, 42, true);

        if (avatar != null) {
            row.getChildren().addAll(chip, avatar);
        } else {
            row.getChildren().add(chip);
        }

        dialog.getChildren().add(row);
    }

    // -------- Helpers --------

    private Image loadImage(String classpath) {
        try {
            return new Image(Objects.requireNonNull(
                getClass().getResourceAsStream(classpath),
                "Missing " + classpath
            ));
        } catch (NullPointerException e) {
            return null;
        }
    }

    private ImageView loadIconView(String classpath, double size) {
        Image img = loadImage(classpath);
        if (img == null) return null;
        ImageView iv = new ImageView(img);
        iv.setFitWidth(size);
        iv.setFitHeight(size);
        iv.setPreserveRatio(true);
        return iv;
    }

    private ImageView avatarView(Image img, double size, boolean mirror) {
        if (img == null) return null;
        ImageView iv = new ImageView(img);
        iv.setFitWidth(size);
        iv.setFitHeight(size);
        iv.setPreserveRatio(true);
        if (mirror) iv.setScaleX(-1);
        iv.getStyleClass().add("avatar");
        return iv;
    }

}
