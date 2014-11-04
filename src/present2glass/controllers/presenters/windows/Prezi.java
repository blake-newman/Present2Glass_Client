package present2glass.controllers.presenters.windows;


import org.jnativehook.keyboard.NativeKeyEvent;
import present2glass.controllers.Presenter;

public class Prezi extends Presenter{

    @Override
    public Boolean isNext(int code) {
        return (code == NativeKeyEvent.VC_RIGHT || code == NativeKeyEvent.VC_SPACE || code == NativeKeyEvent.VC_PAGE_UP) && isDefault();
    }

    @Override
    public Boolean isPrevious(int code) {
        return (code == NativeKeyEvent.VC_LEFT || code == NativeKeyEvent.VC_PAGE_DOWN) && isDefault();
    }

    @Override
    public Boolean isStart(int code) {
        return code == NativeKeyEvent.VC_S && isShift();
    }

    @Override
    public Boolean isStop(int code) {
        return code == NativeKeyEvent.VC_ESCAPE && isDefault();
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
