package present2glass.controllers.presenters.mac;


import org.jnativehook.keyboard.NativeKeyEvent;

import java.awt.event.KeyEvent;

public class PowerPoint extends present2glass.controllers.presenters.windows.PowerPoint {
    @Override
    public Boolean isStart(int code) {
        return (isCommand() || isShiftCommand()) && code == NativeKeyEvent.VC_ENTER;
    }

    @Override
    public Boolean isReset(int code){
        return super.isReset(code) || (isCommand() && code == NativeKeyEvent.VC_ENTER);
    }

    @Override
    public Boolean isStop(int code){
        return super.isStop(code) || (isCommand() && code == NativeKeyEvent.VC_PERIOD);
    }

    @Override
    public void simulateStart(){
        robot.keyPress(KeyEvent.VK_CONTEXT_MENU);
        robot.delay(50);
        robot.keyPress(KeyEvent.VK_ENTER);

        robot.keyRelease(KeyEvent.VK_ENTER);
        robot.keyRelease(KeyEvent.VK_CONTEXT_MENU);
    }
}
