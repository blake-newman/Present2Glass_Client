package presenttoglass.components;

import javafx.animation.FadeTransition;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.InnerShadow;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import presenttoglass.Main;
import presenttoglass.controllers.Data;
import presenttoglass.controllers.EventListener;

import java.util.ArrayList;

public class Editor {
    public VBox contents;
    public ArrayList<Slide> slides = new ArrayList<Slide>();

    public Editor(Data data) {
        setup();
        Main.title.setText(data.title);
        Main.nav.setIP(data.ip);
        Main.nav.setSoftware(data.software);
        int current = 0;
        for (int i = 0; i < data.amounts.size(); i++) {
            ArrayList<String> notes = new ArrayList<String>();
            ArrayList<Boolean> stream = new ArrayList<Boolean>();
            ArrayList<Long> timings = new ArrayList<Long>();
            for(int j = 0; j < data.amounts.get(i); j++){
                notes.add(data.notes.get(j+current));
                stream.add(data.stream.get(j+current));
                timings.add(data.timings.get(j+current));
            }
            current += data.amounts.get(i);
            addSlide(data.hidden.get(i), notes, stream, timings);
        }
        sortSlides();
    }

    public Editor() {
        setup();
        Main.title.setText("");
        Main.nav.setIP("0.0.0.0");
        addSlide(0);
        sortSlides();
        slides.get(0).items.get(0).toggleOpen();
    }

    public void setup(){
        EventListener.start();

        VBox container = new VBox();
        container.setFillWidth(true);

        contents = new VBox();
        contents.setFillWidth(true);

        ScrollPane scroller = new ScrollPane(contents);
        scroller.setFitToHeight(true);
        scroller.setFitToWidth(true);
        scroller.setFitToWidth(true);
        scroller.setFitToHeight(true);
        scroller.setMinWidth(900);
        scroller.setMinHeight(605);
        scroller.setStyle("-fx-focus-color: transparent;-fx-background-insets: 0, 0, 1, 2;");
        scroller.toBack();
        InnerShadow innerShadow = new InnerShadow();
        innerShadow.setColor(Color.color(0, 0, 0, 0.3));
        innerShadow.setWidth(0);
        scroller.setEffect(innerShadow);
        scroller.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroller.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        container.getChildren().add(scroller);
        Main.pane.getChildren().add(container);

        showTitle();
    }

    private void showTitle(){
        FadeTransition ftTitle = new FadeTransition(Duration.millis(300), Main.title);
        ftTitle.setFromValue(Main.title.getOpacity());
        ftTitle.setToValue(1);
        ftTitle.setDelay(Duration.millis(200));
        ftTitle.play();
    }

    public void sortSlides(){
        for (int i = 0; i < slides.size(); i++) {
            slides.get(i).setPosition(i);
            slides.get(i).toggleDelete(slides.size() != 1);
        }
    }

    public void addItem(int slide, int pos){
        pos = pos < slides.get(slide).items.size() ? pos : slides.get(slide).items.size();
        slides.get(slide).addItem(pos);
    }

    public void addSlide(Boolean hidden, ArrayList<String> notes, ArrayList<Boolean> streams, ArrayList<Long> timings){
        Slide slide = new Slide(slides.size(), hidden, notes, streams, timings);
        addNewSlide(slide, slides.size());
    }

    public void addSlide(int pos){
        pos = pos < slides.size() ? pos : slides.size();
        ArrayList<String> notes = new ArrayList<String>();
        ArrayList<Boolean> streams = new ArrayList<Boolean>();
        ArrayList<Long> timings = new ArrayList<Long>();

        notes.add("");
        streams.add(false);
        timings.add(Long.parseLong("0"));

        Slide slide = new Slide(pos, false, notes, streams, timings);
        addNewSlide(slide, pos);
    }

    private void addNewSlide(final Slide slide, final int position){
        slides.add(position, slide);
        contents.getChildren().add(position, slide.layout);
        sortSlides();
    }

    public void removeSlide(int pos){
        slides.remove(pos);
        contents.getChildren().remove(pos);
        sortSlides();
    }

    public void removeItem(int slide, int pos){
        slides.get(slide).removeItem(pos);
    }

}
