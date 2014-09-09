package presenttoglass.controllers.presenters.mac;


import org.jnativehook.keyboard.NativeKeyEvent;
import presenttoglass.controllers.Presenter;

public class Keynote extends Presenter{

    @Override
    public Boolean isNext(int code) {
        return ((code == NativeKeyEvent.VC_SPACE || code == NativeKeyEvent.VC_DOWN || code == NativeKeyEvent.VC_RIGHT || code == NativeKeyEvent.VC_PAGE_DOWN || code == NativeKeyEvent.VC_ENTER || code == NativeKeyEvent.VC_N) && isDefault()) || (code == NativeKeyEvent.VC_RIGHT && isShift());
    }

    @Override
    public Boolean isPrevious(int code) {
        return ((code == NativeKeyEvent.VC_OPEN_BRACKET) && isDefault()) || ((code == NativeKeyEvent.VC_PAGE_UP || code == NativeKeyEvent.VC_UP) && isShift());
    }

    @Override
    public Boolean isStart(int code) {
        return code == NativeKeyEvent.VC_P && isCommandAlt();
    }

    @Override
    public Boolean isReset(int code){
        return super.isReset(code);
    }

    @Override
    public Boolean isStop(int code) {
        return ((code == NativeKeyEvent.VC_ESCAPE || code == NativeKeyEvent.VC_Q)&& isDefault()) || (code == NativeKeyEvent.VC_PERIOD && (isCommand() || isDefault()));
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
        return ((code == NativeKeyEvent.VC_CLOSE_BRACKET) && isDefault()) || ((code == NativeKeyEvent.VC_PAGE_DOWN || code == NativeKeyEvent.VC_DOWN) && isShift());
    }

    @Override
    public Boolean isPreviousSlide(int code) {
        return ((code == NativeKeyEvent.VC_DELETE || code == NativeKeyEvent.VC_UP || code == NativeKeyEvent.VC_LEFT || code == NativeKeyEvent.VC_PAGE_UP || code == NativeKeyEvent.VC_P) && isDefault()) || (code == NativeKeyEvent.VC_UP && isShift());
    }

    @Override
    public Boolean isFirstSlide(int code) {
        return code == NativeKeyEvent.VC_HOME && isDefault();
    }

    @Override
    public Boolean isLastSlide(int code) {
        return code == NativeKeyEvent.VC_END && isDefault();
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
}
