/* $Id$
 *
 * Audio tests.
 *
 * Released under Gnu Public License
 * Copyright © 2012 Michael G. Binz
 */
package application.audio;

import java.io.File;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;



/**
 * Application wiring for the audio switcher.
 *
 * @version $Rev: 2121 $
 * @author Michael Binz
 */
public class AudioManager extends Application
{
    private static MediaPlayer _player;

    public static void play()
    {
        String bip = "vlv.mp3";
        Media hit = new Media(new File(bip).toURI().toString());
        _player = new MediaPlayer(hit);
        _player.play();
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Hello World!");
        Button btn = new Button();
        btn.setText("Say 'Hello World'");
        btn.setOnAction( (s) -> play() );
        StackPane root = new StackPane();
        root.getChildren().add(btn);
        primaryStage.setScene(new Scene(root, 300, 250));
        primaryStage.show();
    }

    public static void main( String[] argv )
    {
        launch( AudioManager.class, argv );
    }
}
