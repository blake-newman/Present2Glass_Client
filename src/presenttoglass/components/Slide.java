package presenttoglass.components;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Duration;
import presenttoglass.Main;
import presenttoglass.fonts.Roboto;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Slide {
    public VBox layout, contents;
    public Text title;
    public ImageView delete, add;
    public CheckBox hiddenCheckBox;
    public HBox hiddenContainer, bar;

    private int _position;
    public ArrayList<Item> items = new ArrayList<Item>();
    public Timer timer;


    public Slide(int position, Boolean hidden, ArrayList<String> notes, ArrayList<Boolean> stream, ArrayList<Long> timings, ArrayList<Long> cues) {
        try {
            layout = FXMLLoader.load(getClass().getResource("/layouts/slide.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        bar = (HBox) layout.lookup("#bar");
        title = (Text) layout.lookup("#title");
        setPosition(position);
        hiddenContainer = (HBox) layout.lookup("#hiddenContainer");
        hiddenCheckBox = (CheckBox) layout.lookup("#hiddenInput");
        contents = (VBox) layout.lookup("#notesHolder");
        delete = (ImageView) layout.lookup("#deleteIcon");
        add = (ImageView) layout.lookup("#addIcon");

        Roboto roboto = new Roboto(Roboto.REGULAR, 19);
        title.setFont(roboto.font);

        setHidden(hidden);
        addListeners();
        deleteListeners();

        timer = new Timer();
        for (int i = 0; i < notes.size(); i++) {
            addItem(notes.get(i), stream.get(i), timings.get(i), cues.get(i));
        }
        sortItems();
        collapseAll();
    }

    public Boolean isHidden() {
        return hiddenCheckBox.isSelected();
    }


    public void setHidden(Boolean hidden) {
        hiddenCheckBox.setSelected(hidden);
    }


    private void addListeners() {
        add.addEventHandler(MouseEvent.MOUSE_RELEASED,
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent e) {
                        Main.editor.addSlide(_position + 1);
                    }
                }
        );

        add.addEventHandler(MouseEvent.MOUSE_ENTERED_TARGET,
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent e) {
                        FadeTransition ft = new FadeTransition(Duration.millis(200), add);
                        ft.setFromValue(add.getOpacity());
                        ft.setToValue(0.7);
                        ft.play();
                    }
                }
        );

        add.addEventHandler(MouseEvent.MOUSE_EXITED_TARGET,
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent e) {
                        FadeTransition ft = new FadeTransition(Duration.millis(200), add);
                        ft.setFromValue(add.getOpacity());
                        ft.setToValue(1);
                        ft.play();
                    }
                }
        );

    }

    private void deleteListeners() {
        delete.addEventHandler(MouseEvent.MOUSE_RELEASED,
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent e) {
                        Main.editor.removeSlide(_position);
                    }
                }
        );

        delete.addEventHandler(MouseEvent.MOUSE_ENTERED_TARGET,
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent e) {
                        FadeTransition ft = new FadeTransition(Duration.millis(200), delete);
                        ft.setFromValue(delete.getOpacity());
                        ft.setToValue(0.7);
                        ft.play();
                    }
                }
        );

        delete.addEventHandler(MouseEvent.MOUSE_EXITED_TARGET,
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent e) {
                        FadeTransition ft = new FadeTransition(Duration.millis(200), delete);
                        ft.setFromValue(delete.getOpacity());
                        ft.setToValue(1);
                        ft.play();
                    }
                }
        );
    }

    public void setPosition(int position) {
        _position = position;
        title.setText("Slide " + (position + 1));
    }

    public void toggleDelete(Boolean visible) {
        delete.setVisible(visible);
    }

    public int getPosition() {
        return _position;
    }

    public void addItem(String text, Boolean stream, long time, long cue) {
        Item item = new Item(getPosition(), items.size(), text, stream, time, cue);
        addNewItem(item, items.size());
    }

    public void addItem(int pos) {
        pos = pos < items.size() ? pos : items.size();
        Item item = new Item(getPosition(), pos, "", false, 0, -1);
        addNewItem(item, pos);
    }

    private void addNewItem(final Item item, final int position) {
        items.add(position, item);
        contents.getChildren().add(position, item.layout);
        sortItems();
        item.open();
    }

    public void removeItem(int pos) {
        items.remove(pos);
        contents.getChildren().remove(pos);
        sortItems();
    }

    public void collapseAll() {
        for (Item item : items) {
            item.collapse();
        }
    }

    public void sortItems() {
        for (int i = 0; i < items.size(); i++) {
            items.get(i).setPosition(i);
            items.get(i).slide = getPosition();
            items.get(i).toggleDelete(items.size() != 1);
        }
    }

}