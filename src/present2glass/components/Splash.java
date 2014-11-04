package present2glass.components;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import org.json.simple.JSONObject;
import present2glass.Main;
import present2glass.controllers.Data;
import present2glass.controllers.IO;
import present2glass.fonts.Roboto;

import java.util.ArrayList;

public class Splash {
    private Button newBtn, openBtn;
    public Splash(){
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(25, 25, 25, 25));
        grid.setVgap(10);
        grid.setHgap(10);

        Text welcome = new Text("Welcome!");
        Roboto robotoThin42 = new Roboto(Roboto.THIN, 42);
        welcome.setFont(robotoThin42.font);
        grid.add(welcome, 0, 0);

        Text intro = new Text("To start; either create a new presentation or open a previous presentation.");
        Roboto robotoThin18 = new Roboto(Roboto.THIN, 18);
        intro.setFont(robotoThin18.font);
        grid.add(intro, 0, 1);

        HBox buttonContainer = new HBox();
        buttonContainer.setSpacing(20);

        newBtn = new Button("New Presentation");
        newBtn.setFont(robotoThin18.font);
        newBtn.setCursor(Cursor.HAND);
        buttonContainer.getChildren().add(newBtn);

        openBtn = new Button("Open Presentation");
        openBtn.setFont(robotoThin18.font);
        openBtn.setCursor(Cursor.HAND);
        buttonContainer.getChildren().add(openBtn);

        grid.add(buttonContainer, 0, 4);

        Main.pane.getChildren().add(grid);

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
