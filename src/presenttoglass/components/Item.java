package presenttoglass.components;

import javafx.animation.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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

public class Item {
    public VBox layout;
    public ScrollPane contents;
    public TextArea input;
    public Text title;
    public ImageView delete, add;
    public CheckBox streamCheckBox;
    public TextField displayTimeInput, cueTimeInput;
    public HBox displayTimeHBox, cueTimeHBox, bar;
    private Boolean _open = false;

    private int _position;
    public int slide;

    public Item(int slide, int position, String text, Boolean stream, long time, long cue){
        this.slide = slide;
        try {
            layout = FXMLLoader.load(getClass().getResource("/layouts/item.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        bar = (HBox) layout.lookup("#bar");
        contents = (ScrollPane) layout.lookup("#content");
        HBox wrapper = (HBox) contents.getContent();
        input = (TextArea) wrapper.getChildren().get(0);
        VBox options = (VBox) wrapper.getChildren().get(1);

        streamCheckBox = (CheckBox) options.getChildren().get(0);
        displayTimeHBox = (HBox) options.getChildren().get(2);
        displayTimeInput = (TextField) displayTimeHBox.getChildren().get(1);
        cueTimeHBox = (HBox) options.getChildren().get(3);
        cueTimeInput = (TextField) cueTimeHBox.getChildren().get(1);

        displayTimeInput.lengthProperty().addListener(new ChangeListener<Number>(){
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if(newValue.intValue() > oldValue.intValue()){
                    char ch = displayTimeInput.getText().charAt(oldValue.intValue());
                    if(!(ch >= '0' && ch <= '9' ) || ch != '.'){
                        displayTimeInput.setText(displayTimeInput.getText().substring(0,displayTimeInput.getText().length()-1));
                    }
                }
            }
        });

        cueTimeInput.lengthProperty().addListener(new ChangeListener<Number>(){
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if(newValue.intValue() > oldValue.intValue()){
                    char ch = cueTimeInput.getText().charAt(oldValue.intValue());
                    if(!(ch >= '0' && ch <= '9' ) || ch != '.'){
                        cueTimeInput.setText(cueTimeInput.getText().substring(0,cueTimeInput.getText().length()-1));
                    }
                }
            }
        });

        title = (Text) layout.lookup("#title");
        delete = (ImageView) layout.lookup("#deleteIcon");
        add = (ImageView) layout.lookup("#addIcon");

        Roboto robotoThin = new Roboto(Roboto.THIN, 19);
        title.setFont(robotoThin.font);
        setStream(stream);
        setDisplayTime(time);
        setCueTime(time);
        setPosition(position);
        setText(text);
        addListeners();
        barListeners();
        deleteListeners();
    }

    public Boolean isStream(){
        return streamCheckBox.isSelected();
    }

    public void setStream(Boolean stream){
        streamCheckBox.setSelected(stream);
    }

    public Long getDisplayTime(){
        Long result;
        try{
            String data = displayTimeInput.getText();
            result =  Long.parseLong(data)*1000;
        } catch (NumberFormatException e) {
            return Long.parseLong("0");
        }
        return result;
    }

    public void setDisplayTime(Long time){
        if(time == Long.parseLong("0")) return;
        displayTimeInput.setText(String.valueOf(time/1000));
    }

    public Long getCueTime(){
        Long result;
        try{
            String data = cueTimeInput.getText();
            result =  Long.parseLong(data)*1000;
        } catch (NumberFormatException e) {
            return Long.parseLong("-1");
        }
        return result;
    }

    public void setCueTime(Long time){
        if(time == Long.parseLong("-1")) return;
        cueTimeInput.setText(String.valueOf(time/1000));
    }

    private void barListeners(){
        bar.addEventHandler(MouseEvent.MOUSE_RELEASED,
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent e) {
                        if(e.getTarget() != add && e.getTarget() != delete) {
                            toggleOpen();
                        }
                    }
                }
        );
    }

    private void addListeners(){
        add.addEventHandler(MouseEvent.MOUSE_RELEASED,
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent e) {
                        collapse();
                        Main.editor.addItem(slide, _position + 1);
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

    private void deleteListeners(){
        delete.addEventHandler(MouseEvent.MOUSE_RELEASED,
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent e) {
                        Main.editor.removeItem(slide, _position);
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

    public String getText(){
        return input.getText();
    }

    public void setText(String text){
        input.setText(text);
    }

    public void setPosition(int position) {
        _position = position;
        title.setText("Note " + (position + 1));
    }

    public void toggleDelete(Boolean visible){
        delete.setVisible(visible);
    }

    public void toggleOpen(){
        if (_open){
            collapse();
        } else {
            open();
        }
    }
    public void collapse(){
        _open = false;
        Timeline timeline = new Timeline();
        final KeyValue kvMinHeight = new KeyValue(contents.minHeightProperty(), 1);
        final KeyFrame kfMinHeight = new KeyFrame(Duration.millis(350), kvMinHeight);
        timeline.getKeyFrames().add(kfMinHeight);
        timeline.play();
    }

    public void open(){
        _open = true;
        Timeline timeline = new Timeline();
        final KeyValue kvMinHeight = new KeyValue(contents.minHeightProperty(), 150);
        final KeyFrame kfMinHeight = new KeyFrame(Duration.millis(350), kvMinHeight);
        timeline.getKeyFrames().add(kfMinHeight);
        timeline.play();
    }

}