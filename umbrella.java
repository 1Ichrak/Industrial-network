import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Worker;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.media.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.nio.file.Paths;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;
import javafx.stage.Window;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;

public class umbrella extends Application {

    private static final String BLACK_BG_STYLE = "-fx-background-color: black;";
    private static final String LOGO_PATH = "file:images/logo.png";
    private static final String IMAGE_PATH = "file:images/umbrella_corp.jpg";
    private static final String SOUND_PATH = "sounds/Intro_sound.mp3";

    private Scene scene;

    @Override
    public void start(Stage primaryStage) {
        System.setProperty("sun.net.http.allowRestrictedHeaders", "true");
        scene = new Scene(new StackPane(), 800, 600);
        scene.setFill(Color.BLACK);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Umbrella Corp.");
        scene.setCursor(Cursor.NONE);
        
        
        primaryStage.setFullScreen(true);
        // Disable default ESC fullscreen exit to handle it yourself
        primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);

        primaryStage.getIcons().add(new Image(LOGO_PATH));
        primaryStage.show();
        
        Font.loadFont("file:fonts/Livvic-Regular.ttf", 14);
        Font.loadFont("file:fonts/MPLUS2-Regular.ttf", 14);

        scene.getRoot().setStyle(BLACK_BG_STYLE);
        
        PauseTransition waitBeforeIntro = new PauseTransition(Duration.seconds(4));
        waitBeforeIntro.setOnFinished(e -> showIntro());
        waitBeforeIntro.play();
        
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                PauseTransition holdEsc = new PauseTransition(Duration.seconds(3));
                holdEsc.setOnFinished(e -> {
                    Platform.exit();
                });
                
            scene.setUserData(holdEsc);  // Save it for stopping later
            holdEsc.play();
        }
    });
    
    scene.setOnKeyReleased(event -> {
        if (event.getCode() == KeyCode.ESCAPE) {
            Object data = scene.getUserData();
            if (data instanceof PauseTransition) {
                ((PauseTransition) data).stop();
            }
        }
    });

}

    public static class JavaConnector {
        public void loginSuccess() {
            System.out.println("Login successful! Proceeding...");
            Platform.runLater(() -> {
                // Replace scene, show next page, etc.
                // For example, just show a simple label here
                // You should replace with your actual next scene code
                showNextPageStatic();
            });
        }
    }

    private void showIntro() {
        ImageView imageView = new ImageView(new Image(IMAGE_PATH));
        imageView.setOpacity(0);
        imageView.setFitWidth(300);
        imageView.setPreserveRatio(true);

        StackPane introRoot = new StackPane(imageView);
        introRoot.setStyle(BLACK_BG_STYLE);
        scene.setRoot(introRoot);

        try {
            MediaPlayer mediaPlayer = new MediaPlayer(new Media(Paths.get(SOUND_PATH).toUri().toString()));
            mediaPlayer.setVolume(0.5);
            mediaPlayer.play();
        } catch (Exception ex) {
            System.err.println("Media playback error: " + ex.getMessage());
        }

        FadeTransition fade = createFadeTransition(imageView, 4, 0, 1, true, 2);
        fade.setOnFinished(e -> {
            PauseTransition waitAfterIntro = new PauseTransition(Duration.seconds(3));
            waitAfterIntro.setOnFinished(ev -> showLogin());
            waitAfterIntro.play();
        });

        fade.play();
    }

    private void showLogin() {

        scene.setCursor(Cursor.DEFAULT);

        WebView webView = new WebView();
        WebEngine webEngine = webView.getEngine();

        // Bind WebView size to scene size for fullscreen responsiveness
        webView.prefWidthProperty().bind(scene.widthProperty());
        webView.prefHeightProperty().bind(scene.heightProperty());

        StackPane webRoot = new StackPane(webView);
        webRoot.setStyle(BLACK_BG_STYLE);

        scene.setRoot(webRoot);

        // Load local login.html
        String loginPagePath = Paths.get("login/login.html").toUri().toString();
        webEngine.load(loginPagePath);

        FadeTransition fadeLogin = new FadeTransition(Duration.seconds(2), webRoot);
        fadeLogin.setFromValue(0.0);
        fadeLogin.setToValue(1.0);
        fadeLogin.play();
    }

    private FadeTransition createFadeTransition(Node node, double seconds, double from, double to, boolean autoReverse, int cycleCount) {
        FadeTransition ft = new FadeTransition(Duration.seconds(seconds), node);
        ft.setFromValue(from);
        ft.setToValue(to);
        ft.setAutoReverse(autoReverse);
        ft.setCycleCount(cycleCount);
        return ft;
    }

    private static void showNextPageStatic() {
        Platform.runLater(() -> {
            Stage stage = (Stage) Stage.getWindows().filtered(Window::isShowing).get(0);
            StackPane root = new StackPane(new Label("Login successful! Welcome to the app."));
            root.setStyle(BLACK_BG_STYLE);
            Scene scene = new Scene(root, Screen.getPrimary().getBounds().getWidth(), Screen.getPrimary().getBounds().getHeight());
            scene.setFill(Color.BLACK);
            stage.setScene(scene);
            stage.setFullScreen(true);
            stage.show();
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}