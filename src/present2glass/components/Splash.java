package present2glass.components;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.json.simple.JSONObject;
import present2glass.Main;
import present2glass.controllers.Data;
import present2glass.controllers.IO;
import present2glass.fonts.Roboto;

import java.io.IOException;
import java.util.ArrayList;

public class Splash {
    private ImageView newBtn, openBtn;
    private VBox layout;
    public Splash(){
        try {
            layout = FXMLLoader.load(getClass().getResource("/layouts/splash.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        newBtn = (ImageView) layout.lookup("#new");
        newBtn.setCursor(Cursor.HAND);

        openBtn = (ImageView) layout.lookup("#open");
        openBtn.setCursor(Cursor.HAND);


        Main.pane.getChildren().add(layout);

        addListeners();
    }

    /**
     * Add event listeners to the new and open buttons
     */
    private void addListeners(){
        newBtn.addEventHandler(MouseEvent.MOUSE_RELEASED,
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent e) {
                        create();
                    }
                }
        );

        openBtn.addEventHandler(MouseEvent.MOUSE_RELEASED,
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent e) {
                        open();
                    }
                }
        );
    }

    /**
     * Create a new blank editor
     */
    private void create(){
        Main.openEditor();
    }

    /**
     * Open a file and parse files data to a new editor
     */
    private void open(){
        IO.read();
        if(IO.current.equals("")) return;
        JSONObject jsonObject = Data.parse(IO.current);
        if(jsonObject.isEmpty()) return;
        Main.openEditor(Data.decode(jsonObject));
    }
}
