package presenttoglass.controllers;

import org.jnativehook.keyboard.NativeKeyEvent;
import presenttoglass.Main;

import java.io.IOException;

public class Presenter implements presenttoglass.interfaces.Presenter {


    public Boolean isPresenting = false;
    public int position = 0;
    public int slide = 0;
    private Data data;
    private Boolean inHiddenSlide = false;

    protected String goToValue = "";

    @Override
    public Boolean isNext(int code) {
        return false;
    }

    @Override
    public Boolean isPrevious(int code) {
        return false;
    }

    @Override
    public Boolean isStart(int code) {
        return false;
    }

    @Override
    public Boolean isStop(int code) {
        return false;
    }

    @Override
    public Boolean isGoTo(int code) {
        return false;
    }

    @Override
    public Boolean isGoToSet(int code) {
        return false;
    }

    @Override
    public Boolean isNextSlide(int code) {
        return false;
    }

    @Override
    public Boolean isNextEndSlide(int code) {
        return false;
    }

    @Override
    public Boolean isPreviousEndSlide(int code) {
        return false;
    }

    @Override
    public Boolean isPreviousSlide(int code) {
        return false;
    }

    @Override
    public Boolean isFirstSlide(int code) {
        return false;
    }

    @Override
    public Boolean isLastSlide(int code) {
        return false;
    }

    @Override
    public Boolean isNextHiddenSlide(int code) {
        return false;
    }

    @Override
    public Boolean isDefault(){
        return !EventListener.isShift && !EventListener.isCommand && !EventListener.isCtrl && !EventListener.isAlt;
    }

    @Override
    public Boolean isCtrl() {
        return !EventListener.isShift && !EventListener.isCommand && EventListener.isCtrl && !EventListener.isAlt;
    }

    @Override
    public Boolean isAlt() {
        return !EventListener.isShift && !EventListener.isCommand && !EventListener.isCtrl && EventListener.isAlt;
    }

    @Override
    public Boolean isShift() {
        return EventListener.isShift && !EventListener.isCommand && !EventListener.isCtrl && !EventListener.isAlt;
    }

    @Override
    public Boolean isCommand() {
        return !EventListener.isShift && EventListener.isCommand && !EventListener.isCtrl && !EventListener.isAlt;
    }

    @Override
    public Boolean isShiftCtrl() {
        return EventListener.isShift && !EventListener.isCommand && EventListener.isCtrl && !EventListener.isAlt;
    }

    @Override
    public Boolean isShiftCommand() {
        return EventListener.isShift && EventListener.isCommand && !EventListener.isCtrl && !EventListener.isAlt;
    }

    @Override
    public Boolean isShiftAlt() {
        return EventListener.isShift && !EventListener.isCommand && !EventListener.isCtrl && EventListener.isAlt;
    }

    @Override
    public Boolean isShiftCtrlAlt() {
        return EventListener.isShift && !EventListener.isCommand && EventListener.isCtrl && EventListener.isAlt;
    }

    @Override
    public Boolean isShiftCommandAlt() {
        return EventListener.isShift && EventListener.isCommand && !EventListener.isCtrl && EventListener.isAlt;
    }

    @Override
    public Boolean isShiftCommandCtrl() {
        return EventListener.isShift && EventListener.isCommand && EventListener.isCtrl && !EventListener.isAlt;
    }

    @Override
    public Boolean isShiftCommandCtrlAlt() {
        return EventListener.isShift && EventListener.isCommand && EventListener.isCtrl && EventListener.isAlt;
    }

    @Override
    public Boolean isCtrlAlt() {
        return !EventListener.isShift && !EventListener.isCommand && EventListener.isCtrl && EventListener.isAlt;
    }

    @Override
    public Boolean isCommandCtrlAlt() {
        return !EventListener.isShift && EventListener.isCommand && EventListener.isCtrl && EventListener.isAlt;
    }

    @Override
    public Boolean isCommandAlt() {
        return !EventListener.isShift && EventListener.isCommand && !EventListener.isCtrl && EventListener.isAlt;
    }

    @Override
    public Boolean isCommandCtrl() {
        return !EventListener.isShift && EventListener.isCommand && EventListener.isCtrl && !EventListener.isAlt;
    }

    @Override
    public Boolean isReset(int code) {
        return EventListener.isShift && EventListener.isAlt && code == NativeKeyEvent.VC_R;
    }

    @Override
    public void simulateNext() {

    }

    @Override
    public void simulatePrevious() {

    }

    @Override
    public void simulateStop() {

    }

    @Override
    public void simulateStart() {

    }

    @Override
    public void simulateReset() {

    }

    @Override
    public void clearGoTo() {
        goToValue = "";
    }

    @Override
    public int getGoToValue() {
        return Integer.parseInt(goToValue);
    }

    private int getNextNonHiddenNote(int pos){
        while(true){
            pos++;
            int slide = getSlideFromNote(pos);
            if(slide < data.totalSlides && !data.hidden.get(slide)){
                return pos;
            } else if(slide >= data.totalSlides){
                return data.totalNotes;
            }
        }
    }

    private int getPreviousNonHiddenNote(int pos){
        int orig = pos;
        while(true){
            pos--;
            int slide = getSlideFromNote(pos);
            if(slide >= 0 && !data.hidden.get(slide)){
                return pos;
            } else if(slide < 0){
                return orig;
            }
        }
    }

    private int getNextNonHiddenSlide(int slide){
        int orig = slide;
        while(true){
            slide++;
            if(slide < data.totalSlides && !data.hidden.get(slide)){
                return slide;
            } else if (slide > data.totalSlides){
                return orig;
            }
        }
    }

    private int getPreviousNonHiddenSlide(int slide){
        int orig = slide;
        while(true){
            slide--;
            if(slide >= 0 && !data.hidden.get(slide)){
                return slide;
            } else if (slide < 0){
                return orig;
            }
        }
    }

    private int getFirstNoteOfSlide(int slide){
        int total = 0;
        for(int i = 0; i < slide; i++){
            total += data.amounts.get(i);
        }
        return total;
    }

    private int getLastNoteOfSlide(int slide){
        int total = -1;
        for(int i = 0; i <= slide; i++){
            total += data.amounts.get(i);
        }
        return total;
    }

    private int getSlideFromNote(int pos){
        int total = 0;
        slide = 0;
        for (Long amount : data.amounts) {
            total+=amount;
            if(total > pos){
                break;
            }
            slide++;
        }
        return slide;
    }

    private Boolean checkHiddenState(int slide){
        return slide < data.hidden.size() && data.hidden.get(slide);
    }

    public void goNext(){
        if(inHiddenSlide){
            setPosition(position+1);
        } else {
            setPosition(getNextNonHiddenNote(position));
        }
    }

    public void goPrevious(){
        if(inHiddenSlide){
            setPosition(position-1);
        } else {
            setPosition(getPreviousNonHiddenNote(position));
        }
    }

    public void goNextSlide(){
        int slidePos = getNextNonHiddenSlide(slide);
        if (slidePos == slide){
            setPosition(data.totalNotes);
        } else {
            setPosition(getFirstNoteOfSlide(slidePos));
        }
    }

    public void goNextEndSlide(){
        int slidePos = getNextNonHiddenSlide(slide);

        if (slidePos == slide){
            setPosition(data.totalNotes);
        } else {
            setPosition(getLastNoteOfSlide(slidePos));
        }
    }

    public void goNextHiddenSlide(){
        if(checkHiddenState(slide+1)){
            setPosition(getFirstNoteOfSlide(slide + 1));
        }
    }

    public void goPreviousSlide(){
        int slidePos = getPreviousNonHiddenSlide(slide);
        if (slidePos < slide){
            setPosition(getFirstNoteOfSlide(slidePos));
        }
    }

    public void goPreviousEndSlide(){
        int slidePos = getPreviousNonHiddenSlide(slide);
        if (slidePos < slide){
            setPosition(getLastNoteOfSlide(slidePos));
        }
    }

    public void goLastSlide(){
        int lastTotal = Integer.parseInt(String.valueOf(data.amounts.get(data.totalSlides - 1)));
        setPosition(data.totalNotes-lastTotal);
    }

    public void setPosition(int pos){
        if(!isPresenting) return;

        // Creates a new data object from the editor so that the notes can seamlessly be edited while glass is presenting.
        Boolean end = pos >= data.totalNotes;
        position = end ? data.totalNotes : (pos < 0 ? 0 : pos);
        slide = getSlideFromNote(position);
        inHiddenSlide = checkHiddenState(slide);
        String note = end ? "End of presentation" : data.notes.get(position);
        Boolean stream = end ? false : data.stream.get(position);
        Long time = end ? Long.parseLong("0") : data.timings.get(position);
        sendData(note, stream, time);
    }

    public void sendData(String note, Boolean stream, Long time){
        if(!isPresenting) return;
        Main.viewer.set(note, stream, time);
        Main.glass.sendData(note, stream, time);
    }

    public void stopPresentation(){
        Main.toggleViewer(false);
        isPresenting = false;
        Main.glass.stopPresentation();
    }

    public void startPresentation(){
        isPresenting = true;
        data = new Data();
        Main.toggleViewer(true);
        Main.glass.startPresentation();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        setPosition(position);
    }

}
