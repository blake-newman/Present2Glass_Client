package present2glass.components;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Shape;
import javafx.util.Duration;
import present2glass.Main;
import present2glass.controllers.IO;
import present2glass.controllers.Net;
import present2glass.controllers.OS;
import present2glass.controllers.Server;
import present2glass.controllers.presenters.mac.Keynote;
import present2glass.controllers.presenters.windows.LibreOffice;
import present2glass.controllers.presenters.windows.Prezi;
import present2glass.controllers.presenters.windows.PowerPoint;

import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Nav {
    public ImageView warning, sync, save, back;
    private StackPane softwareButton;
    private Shape record;
    private Boolean saveFlashing = false;
    private Boolean recordFlashing = false;
    private int software = 0;

    public static TextField ip;
    private static HBox container;

    public Nav(HBox root){
        container = root;
        warning = (ImageView) container.lookup("#warningIcon");
        sync = (ImageView) container.lookup("#syncIcon");
        save = (ImageView) container.lookup("#saveIcon");
        back = (ImageView) container.lookup("#backIcon");
        record = (Shape) container.lookup("#recordIcon");
        ip = (TextField) container.lookup("#ipInput");

        softwareButton = (StackPane) container.lookup("#softwareButton");
        
        setSoftware(software);
        addWatchers();
        addBackListeners();
        addSaveListeners();
        addSoftwareListeners();
    }

    /**
     * Get the ip input box' text value
     * @return String representing the ip address
     */
    public String getIP(){
        String address = ip.getText();
        return Net.validateIP(address) ? address : "0.0.0.0";
    }

    /**
     * Set the ip input box' text value
     * @param address String representing the ip address
     */
    public void setIP(String address){
        ip.setText(Net.validateIP(address) ? address : "");
    }

    public int getSoftware(){
        return software;
    }

    public void setSoftware(int num){
        int total = softwareButton.getChildren().size();
        num = num >= total ? 0 : num;
        software = num;
        for(int i = 0; i < total; i++){
            softwareButton.getChildren().get(i).setVisible(false);
        }
        softwareButton.getChildren().get(total - 1 - num).setVisible(true);
        switch(num){
            case(1):
                present2glass.Main.presenter = new Keynote();
                break;
            case(2):
                present2glass.Main.presenter = new LibreOffice();
                break;
            case(3):
                present2glass.Main.presenter = new Prezi();
                break;
            default:
                present2glass.Main.presenter = OS.isMac() ? new present2glass.controllers.presenters.mac.PowerPoint() : new PowerPoint();
                break;
        }
    }

    private void addSoftwareListeners(){
        softwareButton.addEventHandler(MouseEvent.MOUSE_RELEASED,
                new EventHandler<MouseEvent>() {
                    @Override public void handle(MouseEvent e) {
                        setSoftware(software+1);
                    }
                }
        );

        softwareButton.addEventHandler(MouseEvent.MOUSE_ENTERED_TARGET,
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent e) {
                        FadeTransition ft = new FadeTransition(Duration.millis(200), softwareButton);
                        ft.setFromValue(softwareButton.getOpacity());
                        ft.setToValue(0.6);
                        ft.play();
                    }
                }
        );

        softwareButton.addEventHandler(MouseEvent.MOUSE_EXITED_TARGET,
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent e) {
                        FadeTransition ft = new FadeTransition(Duration.millis(200), softwareButton);
                        ft.setFromValue(softwareButton.getOpacity());
                        ft.setToValue(1);
                        ft.play();
                    }
                }
        );
    }
    


    /**
     * Add Event listeners to the back button
     */
    private void addBackListeners(){
        back.addEventHandler(MouseEvent.MOUSE_RELEASED,
                new EventHandler<MouseEvent>() {
                    @Override public void handle(MouseEvent e) {
                        if(IO.isSaved()) {
                            if(Main.presenter.isPresenting){
                                Main.presenter.stopPresentation();
                            } else {
                                present2glass.Main.openSplash();
                            }
                        } else {
                            IO.confirmUnsave(false);
                        }
                    }
                }
        );

        back.addEventHandler(MouseEvent.MOUSE_ENTERED_TARGET,
                new EventHandler<MouseEvent>() {
                    @Override public void handle(MouseEvent e) {
                        FadeTransition ft = new FadeTransition(Duration.millis(200), back);
                        ft.setFromValue(back.getOpacity());
                        ft.setToValue(0.6);
                        ft.play();
                    }
                }
        );

        back.addEventHandler(MouseEvent.MOUSE_EXITED_TARGET,
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent e) {
                        FadeTransition ft = new FadeTransition(Duration.millis(200), back);
                        ft.setFromValue(back.getOpacity());
                        ft.setToValue(1);
                        ft.play();
                    }
                }
        );
    }

    /**
     * Add Event listeners to the save button
     */
    private void addSaveListeners(){
        save.addEventHandler(MouseEvent.MOUSE_RELEASED,
                new EventHandler<MouseEvent>() {
                    @Override public void handle(MouseEvent e) {
                        if(IO.isSaved()) return;
                        IO.write();
                    }
                }
        );

        save.addEventHandler(MouseEvent.MOUSE_ENTERED_TARGET,
                new EventHandler<MouseEvent>() {
                    @Override public void handle(MouseEvent e) {
                        if(IO.isWriting) return;
                        FadeTransition ft = new FadeTransition(Duration.millis(200), save);
                        ft.setFromValue(save.getOpacity());
                        ft.setToValue(0.6);
                        ft.play();
                    }
                }
        );

        save.addEventHandler(MouseEvent.MOUSE_EXITED_TARGET,
                new EventHandler<MouseEvent>() {
                    @Override public void handle(MouseEvent e) {
                        if(IO.isWriting) return;
                        FadeTransition ft = new FadeTransition(Duration.millis(200), save);
                        ft.setFromValue(save.getOpacity());
                        ft.setToValue(1);
                        ft.play();
                    }
                }
        );
    }

    /**
     * Add Watch listeners to components for animating them depending on application state
     */
    private void addWatchers(){
        Timer timer = new Timer();

        final FadeTransition ftSave = new FadeTransition(Duration.millis(600), save);
        final FadeTransition ftRecord = new FadeTransition(Duration.millis(600), record);

        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                Platform.runLater(new Runnable() {
                    public void run() {
                        Boolean presenting = Main.presenter.isPresenting;

                        Boolean connected =  Main.glass != null && Main.glass.isConnected;
                        warning.setVisible(!connected);
                        sync.setVisible(!presenting && connected);
                        record.setVisible(presenting);
                        Nav.ip.setDisable(connected);
                    }
                });
            }
        }, 0, 500);

        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                Platform.runLater(new Runnable() {
                    public void run() {
                        if(IO.isWriting && !saveFlashing){
                            saveFlashing = true;
                            ftSave.setFromValue(save.getOpacity());
                            ftSave.setToValue(0.3);
                            ftSave.play();
                            ftSave.setOnFinished(new EventHandler<ActionEvent>(){
                                @Override
                                public void handle(ActionEvent e) {
                                    ftSave.setFromValue(0.3);
                                    ftSave.setToValue(1);
                                    ftSave.play();

                                    ftSave.setOnFinished(new EventHandler<ActionEvent>() {
                                        @Override
                                        public void handle(ActionEvent e) {
                                            saveFlashing = false;
                                        }
                                    });
                                }
                            });
                        }
                    }
                });
            }
        }, 0, 50);

        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                Platform.runLater(new Runnable() {
                    public void run() {
                        if(Main.presenter.isPresenting && !recordFlashing){
                            recordFlashing = true;
                            ftRecord.setFromValue(save.getOpacity());
                            ftRecord.setToValue(0.3);
                            ftRecord.play();
                            ftRecord.setOnFinished(new EventHandler<ActionEvent>(){
                                @Override
                                public void handle(ActionEvent e) {
                                    ftRecord.setFromValue(0.3);
                                    ftRecord.setToValue(1);
                                    ftRecord.play();

                                    ftRecord.setOnFinished(new EventHandler<ActionEvent>() {
                                        @Override
                                        public void handle(ActionEvent e) {
                                            recordFlashing = false;
                                        }
                                    });
                                }
                            });
                        }
                    }
                });
            }
        }, 0, 50);
    }

    /**
     * Show the nav bar
     */
    public static void show(){
        animate(true);
    }

    /**
     * Hide the nav bar
     */
    public static void hide(){
        animate(false);
    }

    /**
     * Animate the nav bar in or out
     */
    private static void animate(Boolean hide){
        TranslateTransition ttContainer = new TranslateTransition(Duration.millis(300), container);
        ttContainer.setFromY(container.getTranslateY());
        ttContainer.setToY(hide ? 0 : 35);
        ttContainer.play();
    }

}
