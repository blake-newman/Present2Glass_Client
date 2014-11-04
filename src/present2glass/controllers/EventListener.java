package present2glass.controllers;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseListener;
import present2glass.Main;

import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class EventListener implements NativeMouseListener, NativeKeyListener{
    public static Boolean listening = false;
    public static Boolean isCtrl = false;
    public static Boolean isShift = false;
    public static Boolean isAlt = false;
    public static Boolean isCommand = false;
    public static Boolean started = false;
    public static void start() {

        if(!started){
            try {
                GlobalScreen.registerNativeHook();
            } catch (NativeHookException e) {
                e.printStackTrace();
            }

            // Clear previous logging configurations.
            LogManager.getLogManager().reset();

            // Get the logger for "org.jnativehook" and set the level to off.
            Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
            logger.setLevel(Level.OFF);
            //Construct the example object and initialze native hook.
            EventListener evtListener = new EventListener();
            GlobalScreen.getInstance().addNativeKeyListener(evtListener);
            GlobalScreen.getInstance().addNativeMouseListener(evtListener);
            started = true;
        }

        listening = true;
    }

    public static void pause(){
        listening = false;
    }

    public static void stop(){
        try {
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent e) {
        if(!listening) return;
        switch(e.getKeyCode()){
            case(NativeKeyEvent.VC_SHIFT_L):
            case(NativeKeyEvent.VC_SHIFT_R):
                isShift = true;
                break;
            case(NativeKeyEvent.VC_ALT_L):
            case(NativeKeyEvent.VC_ALT_R):
                isAlt = true;
                break;
            case(NativeKeyEvent.VC_CONTROL_L):
            case(NativeKeyEvent.VC_CONTROL_R):
                isCtrl = true;
                break;
            case(NativeKeyEvent.VC_META_L):
            case(NativeKeyEvent.VC_META_R):
                isCommand = true;
                break;
        }
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent e) {
        if(!listening) return;
        int code = e.getKeyCode();
        switch(code){
            case(NativeKeyEvent.VC_SHIFT_L):
            case(NativeKeyEvent.VC_SHIFT_R):
                isShift = false;
                return;
            case(NativeKeyEvent.VC_ALT_L):
            case(NativeKeyEvent.VC_ALT_R):
                isAlt = false;
                return;
            case(NativeKeyEvent.VC_CONTROL_L):
            case(NativeKeyEvent.VC_CONTROL_R):
                isCtrl = false;
                return;
            case(NativeKeyEvent.VC_META_L):
            case(NativeKeyEvent.VC_META_R):
                isCommand = false;
                return;
        }

        if (Main.presenter.isReset(code)) {
            Main.presenter.position = 0;
        }

        if(Main.presenter.isPresenting){
            if (Main.presenter.isGoTo(code)) {
                if (Main.presenter.isGoToSet(code)) {
                    Main.presenter.setPosition(Main.presenter.getGoToValue());
                    Main.presenter.clearGoTo();
                }
            } else {
                Main.presenter.clearGoTo();
                if (Main.presenter.isNext(code)) {
                    Main.presenter.goNext();
                } else if (Main.presenter.isPrevious(code)) {
                    Main.presenter.goPrevious();
                } else if (Main.presenter.isNextSlide(code)) {
                    Main.presenter.goNextSlide();
                } else if (Main.presenter.isNextEndSlide(code)) {
                    Main.presenter.goNextEndSlide();
                } else if (Main.presenter.isPreviousSlide(code)) {
                    Main.presenter.goPreviousSlide();
                } else if (Main.presenter.isPreviousEndSlide(code)) {
                    Main.presenter.goPreviousEndSlide();
                } else if (Main.presenter.isLastSlide(code)) {
                    Main.presenter.goLastSlide();
                } else if (Main.presenter.isFirstSlide(code)) {
                    Main.presenter.setPosition(0);
                } else if (Main.presenter.isNextHiddenSlide(code)) {
                    Main.presenter.goNextHiddenSlide();
                } if (Main.presenter.isStop(code)) {
                    Main.presenter.stopPresentation();
                }
            }
        } else {
            if(Main.presenter.isStart(code)){
                Main.presenter.startPresentation();
            }
        }
    }

    @Override
    public void nativeKeyTyped(NativeKeyEvent e) {
        if(!listening) return;

    }

    @Override
    public void nativeMouseClicked(NativeMouseEvent e) {
        if(!listening) return;

    }

    @Override
    public void nativeMousePressed(NativeMouseEvent e) {
        if(!listening) return;

    }

    @Override
    public void nativeMouseReleased(NativeMouseEvent e) {
        if(!listening) return;

    }
}
