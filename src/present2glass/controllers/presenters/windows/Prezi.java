package present2glass.controllers.presenters.windows;


import org.jnativehook.keyboard.NativeKeyEvent;
import present2glass.controllers.Presenter;

import java.awt.event.KeyEvent;

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
    public void simulateStart(){
        robot.keyPress(KeyEvent.VK_SHIFT);
        robot.delay(50);
        robot.keyPress(KeyEvent.VK_S);

        robot.keyRelease(KeyEvent.VK_S);
        robot.keyRelease(KeyEvent.VK_SHIFT);
    }
}
