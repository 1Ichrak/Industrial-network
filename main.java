import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.nio.file.Paths;

public class main extends Application {

    @Override
    public void start(Stage primaryStage) {

        // Load your mp3 file
        String musicFile = "Sounds/Intro_sound.mp3";
        Media sound = new Media(Paths.get(musicFile).toUri().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.setVolume(0.7); // 70% volume

        // Load image from the Images folder
        Image logo = new Image("file:Images/umbrella_corp.jpg");

        // Create ImageView and configure size and aspect ratio
        ImageView imageView = new ImageView(logo);
        imageView.setFitWidth(400);
        imageView.setPreserveRatio(true);

        // Make the image initially invisible (fully transparent)
        imageView.setOpacity(0);

        // Create root pane and set background color
        StackPane root = new StackPane(imageView);
        root.setStyle("-fx-background-color: black;");

        // Create scene with root pane and size
        Scene scene = new Scene(root, 800, 600);

        // Set scene and window title
        primaryStage.setScene(scene);
        primaryStage.setTitle("Umbrella Corp.");

        // Disable fullscreen exit hint message (optional)
        primaryStage.setFullScreenExitHint("");

        // Make window fullscreen
        primaryStage.setFullScreen(true);

        Image windowIcon = new Image("file:Images/logo.png"); // icon file path
        primaryStage.getIcons().add(windowIcon);

        // Show the window
        primaryStage.show();

        // Setup fade-in and fade-out animation for the image
        FadeTransition fade = new FadeTransition(Duration.seconds(4), imageView);
        fade.setFromValue(0.0);
        fade.setToValue(1.0);
        fade.setAutoReverse(true);
        fade.setCycleCount(2); // fade in then fade out

        // Create a 5-second pause before starting sound and fade animation
        PauseTransition pause = new PauseTransition(Duration.seconds(3));
        pause.setOnFinished(e -> {
            mediaPlayer.play();
            fade.play();
        });

        // Start the pause
        pause.play();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
