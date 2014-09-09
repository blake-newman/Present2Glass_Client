package presenttoglass.controllers;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.*;
import presenttoglass.Main;

import java.io.*;
import java.net.URL;

public class IO {
    public static Boolean isWriting = false;
    public static Boolean isReading = false;
    public static String fileName = "";
    public static File file;
    public static String current = "";

    public static Boolean isSaved(){
        return (isWriting || current.equals(Data.encode(new Data())));
    }

    public static void chooseFile(Boolean open){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(open ? "Open" : "Save");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("p2gg", "*.p2gg"));
        if(open){
            file = fileChooser.showOpenDialog(Main.stage);
        } else {
            file = fileChooser.showSaveDialog(Main.stage);
        }
        if(file != null){
            fileName = file.getName();
        }
    }

    public static void confirmUnsave(final Boolean quit){
        try {
            final Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            URL unsave = IO.class.getResource("/layouts/unsaved.fxml");
            Parent parent = FXMLLoader.load(unsave);
            Scene scene = new Scene(parent, 400, 200);
            dialogStage.setScene(scene);
            dialogStage.setTitle("Confirm Dialog");
            dialogStage.setResizable(false);
            dialogStage.initStyle(StageStyle.UNDECORATED);
            dialogStage.show();

            parent.lookup("#yesButton").addEventHandler(MouseEvent.MOUSE_RELEASED,
                    new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent e) {
                            write();
                            dialogStage.close();
                            if(EventListener.listening) EventListener.stop();
                            if (quit) {
                                Main.server.destroy();
                                System.exit(0);
                            } else if (Main.presenter.isPresenting){
                                Main.presenter.stopPresentation();
                            } else {
                                Main.openSplash();
                            }
                        }
                    }
            );

            parent.lookup("#noButton").addEventHandler(MouseEvent.MOUSE_RELEASED,
                    new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent e) {
                            dialogStage.close();
                            if(EventListener.listening) EventListener.stop();
                            if (quit) {
                                Main.server.destroy();
                                System.exit(0);
                            } else if (Main.presenter.isPresenting){
                                Main.presenter.stopPresentation();
                            } else {
                                Main.openSplash();
                            }
                        }
                    }
            );

            scene.getWindow().setOnCloseRequest(new EventHandler<WindowEvent>() {
                public void handle(WindowEvent ev) {
                    dialogStage.close();
                    if(EventListener.listening) EventListener.stop();
                    if (quit) {
                        Main.server.destroy();
                        System.exit(0);
                    } else if (Main.presenter.isPresenting){
                        Main.presenter.stopPresentation();
                    } else {
                        Main.openSplash();
                    }
                }
            });


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void write(){
        current = Data.encode(new Data());
        if(fileName.equals("")){
            chooseFile(false);
        }
        if(file == null) return;
        isWriting = true;
        FileWriter writer;
        try {
            writer = new FileWriter(file.getPath());
            writer.write(current);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            isWriting = false;
        }

    }

    public static void read(){
        clear();
        chooseFile(true);
        if(file == null) return;
        isReading = true;
        try {
            FileReader reader = new FileReader(file.getPath());
            BufferedReader buffer = new BufferedReader(reader);
            current = buffer.readLine();
            buffer.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            isReading = false;
        }
    }

    public static void clear(){
        current = "";
        fileName = "";
        file = null;
    }

}
