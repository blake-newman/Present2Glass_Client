package presenttoglass.controllers.presenters.mac;


import org.jnativehook.keyboard.NativeKeyEvent;

public class PowerPoint extends presenttoglass.controllers.presenters.windows.PowerPoint {
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
}
