package present2glass.controllers.presenters.windows;


import org.jnativehook.keyboard.NativeKeyEvent;
import present2glass.controllers.EventListener;
import present2glass.controllers.Presenter;

import java.awt.event.KeyEvent;

public class PowerPoint extends Presenter{

    @Override
    public Boolean isNext(int code) {
        return (code == NativeKeyEvent.VC_SPACE || code == NativeKeyEvent.VC_DOWN || code == NativeKeyEvent.VC_RIGHT || code == NativeKeyEvent.VC_PAGE_DOWN || code == NativeKeyEvent.VC_ENTER || code == NativeKeyEvent.VC_N) && isDefault();
    }

    @Override
    public Boolean isPrevious(int code) {
        return (code == NativeKeyEvent.VC_BACKSPACE || code == NativeKeyEvent.VC_UP || code == NativeKeyEvent.VC_LEFT || code == NativeKeyEvent.VC_PAGE_UP || code == NativeKeyEvent.VC_P) && isDefault();
    }

    @Override
    public Boolean isStart(int code) {
        return code == NativeKeyEvent.VC_F5 && (isDefault() || isShift());
    }

    @Override
    public Boolean isReset(int code){
        return super.isReset(code) || (code == NativeKeyEvent.VC_F5 && isDefault());
    }

    @Override
    public Boolean isStop(int code){
        return (code == NativeKeyEvent.VC_ESCAPE || code == NativeKeyEvent.VC_MINUS) && isDefault();
    }

    @Override
    public Boolean isNextHiddenSlide(int code){
        return code == NativeKeyEvent.VC_H && isDefault();
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
    public Boolean isGoTo(int code) {
        return false;
    }

    @Override
    public Boolean isGoToSet(int code) {
        return false;
    }

    @Override
    public void simulateStart(){
        robot.keyPress(KeyEvent.VK_F5);
        robot.keyRelease(KeyEvent.VK_F5);
    }

}
