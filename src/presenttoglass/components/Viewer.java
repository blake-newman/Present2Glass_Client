package presenttoglass.components;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class Viewer {
    public VBox layout;
    private Text content;
    private Text timer;
    public Viewer() {
        try {
            layout = FXMLLoader.load(getClass().getResource("/layouts/viewer.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        content = (Text) layout.lookup("#contentHolder");
        timer = (Text) layout.lookup("#timerHolder");
    }

    public void set(final String note, final Boolean stream, final Long time){
        Platform.runLater(new Runnable() {
            public void run(){
                setContent(note);
                timer.setText("");
                setTime(stream, time);
            }
        });
    }

    public void setTime(Boolean stream, final Long time){
        final Timer myTimer = new Timer();
        if(stream){
            myTimer.scheduleAtFixedRate(new TimerTask() {
                int total = 0;
                public void run() {
                    Platform.runLater(new Runnable() {
                        public void run() {
                            long remaining = (time/1000)-total;
                            timer.setText("Streaming from glass in "+remaining+" seconds");
                            total = total+1;

                            if (remaining == 0){
                                timer.setText("");
                                setContent("Streaming from Glass...");
                                myTimer.cancel();
                            }
                        }
                    });
                }
            }, 0, 1000);
        } else if (time != 0) {
            myTimer.schedule(new TimerTask() {
                public void run() {
                    Platform.runLater(new Runnable() {
                        public void run() {
                            setContent("");
                            myTimer.cancel();
                        }
                    });
                }
            }, time);
        }
    }

    private void setContent(String note){
        content.setText(note);
        content.setFont(Font.font("Verdana", 24));

    }

}
