package present2glass;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import present2glass.components.Editor;
import present2glass.components.Nav;
import present2glass.components.Splash;
import present2glass.components.Viewer;
import present2glass.controllers.*;

import present2glass.fonts.Roboto;

import java.util.Timer;
import java.util.TimerTask;

public class Main extends Application {
    static public Parent root;
    static public Pane pane, viewerHolder;
    static public TextField title;
    static public Editor editor;
    static public Stage stage;
    static public Scene scene;
    static public Nav nav;
    static public Presenter presenter;
    static public Viewer viewer;
    static public Server server;
    static public Glass glass;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage main) throws Exception{
        stage = main;
        root = FXMLLoader.load(getClass().getResource("/layouts/wrapper.fxml"));
        scene = new Scene(root, 890, 690);
        main.setTitle("Present To Glass");
        main.setScene(scene);
        main.setResizable(false);
        main.show();

        setup();
        openSplash();
    }

    /**
     * Setup the views and components of the application
     */
    private void setup(){
        pane = (Pane) root.lookup("#contentPane");
        title = (TextField) root.lookup("#titleField");

        Roboto roboto18 = new Roboto(Roboto.REGULAR, 18);
        title.setFont(roboto18.font);
        nav = new Nav((HBox) root.lookup("#navBar"));
        scene.getWindow().setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent ev) {
                try {
                    if (!IO.isSaved()) {
                        ev.consume();
                        IO.confirmUnsave(true);
                    } else {
                        if (EventListener.listening) EventListener.stop();
                    }
                } catch (NullPointerException e){
                    quit();
                }
            }
        });

        viewerHolder = (Pane) root.lookup("#viewerHolder");
        viewer = new Viewer();
        viewerHolder.getChildren().add(viewer.layout);

    }

    /**
     * Open up the splash page.
     */
    public static void openSplash(){
        if(!pane.getChildren().isEmpty()) {
            EventListener.pause();
            server.stopPresentation();
            server.destroy();
        }
        clear();
        IO.clear();

        new Splash();
        Nav.hide();
        animatePaneIn();

        FadeTransition ftTitle = new FadeTransition(Duration.millis(200), title);
        ftTitle.setFromValue(title.getOpacity());
        ftTitle.setToValue(0);
        ftTitle.play();
    }

    /**
     * Open up the Editor.
     * @param data Data for the editor to start with.
     */
    public static void openEditor(Data data){
        clear();
        server = new Server();
        glass = new Glass();
        Nav.ip.setText("");

        editor = new Editor(data);
        Nav.show();
        animatePaneIn();
    }

    /**
     * Open up the Editor. With no data
     */
    public static void openEditor(){
        clear();
        server = new Server();
        glass = new Glass();
        Nav.ip.setText("");

        editor = new Editor();
        Nav.show();
        animatePaneIn();

    }

    public static void toggleViewer(final Boolean show){
        Platform.runLater(new Runnable() {
            public void run() {
                viewerHolder.setVisible(show);
                viewerHolder.setDisable(!show);
                pane.setVisible(!show);
                pane.setDisable(show);
                title.setDisable(show);
                nav.save.setFitWidth(show ? 1 : 20);
                nav.save.setVisible(!show);
            }
        });
    }



    /**
     * Animate the content pane in
     */
    private static void animatePaneIn(){
        FadeTransition ftPane = new FadeTransition(Duration.millis(400), pane);
        ftPane.setFromValue(0);
        ftPane.setToValue(1);
        ftPane.play();
    }

    /**
     * Clear the content pane and stop the event listener
     */
    private static void clear(){
        if(!pane.getChildren().isEmpty()){
            pane.getChildren().clear();
        }
    }

    public static void quit(){
        if (server != null){
            server.stopPresentation();
            server.destroy();
        }


        if (server != null && glass != null){
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    System.exit(0);
                }
            }, 1500);
        } else {
            System.exit(0);
        }

    }

}
