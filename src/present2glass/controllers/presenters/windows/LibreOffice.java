package present2glass.controllers.presenters.windows;

import org.jnativehook.keyboard.NativeKeyEvent;
import present2glass.controllers.Presenter;

import java.awt.event.KeyEvent;

public class LibreOffice extends Presenter{

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
        return code == NativeKeyEvent.VC_F5 && isDefault();
    }

    @Override
    public Boolean isReset(int code){
        return super.isReset(code) || (code == NativeKeyEvent.VC_F5 && isDefault());
    }

    @Override
    public Boolean isStop(int code){
        return code == NativeKeyEvent.VC_ESCAPE && isDefault();
    }

    @Override
    public Boolean isPreviousSlide(int code) {
        return code == NativeKeyEvent.VC_PAGE_UP && isCtrl();
    }

    @Override
    public Boolean isNextSlide(int code) {
        return code == NativeKeyEvent.VC_PAGE_DOWN && isCtrl();
    }

    @Override
    public Boolean isPreviousEndSlide(int code) {
        return code == NativeKeyEvent.VC_PAGE_UP && isAlt();
    }

    @Override
    public Boolean isNextEndSlide(int code) {
        return code == NativeKeyEvent.VC_PAGE_DOWN && isAlt();
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
    public void simulateReset() {

    }

    @Override
    public void simulateNextSlide() {
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.delay(50);
        robot.keyPress(KeyEvent.VK_PAGE_DOWN);
        robot.keyRelease(KeyEvent.VK_CONTROL);
        robot.keyRelease(KeyEvent.VK_PAGE_DOWN);
    }

    @Override
    public void simulatePreviousSlide() {
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.delay(50);
        robot.keyPress(KeyEvent.VK_PAGE_UP);
        robot.keyRelease(KeyEvent.VK_CONTROL);
        robot.keyRelease(KeyEvent.VK_PAGE_UP);
    }

    @Override
    public void simulateStart(){
        robot.keyPress(KeyEvent.VK_F5);
        robot.keyRelease(KeyEvent.VK_F5);
    }
}
