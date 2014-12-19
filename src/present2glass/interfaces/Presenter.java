package present2glass.interfaces;

public interface Presenter {
    Boolean isNext(int code);
    Boolean isPrevious(int code);
    Boolean isStart(int code);
    Boolean isStop(int code);
    Boolean isGoTo(int code);
    Boolean isGoToSet(int code);
    Boolean isNextSlide(int code);
    Boolean isPreviousSlide(int code);
    Boolean isNextEndSlide(int code);
    Boolean isPreviousEndSlide(int code);
    Boolean isFirstSlide(int code);
    Boolean isLastSlide(int code);
    Boolean isReset(int code);
    Boolean isNextHiddenSlide(int code);

    Boolean isDefault();
    Boolean isCtrl();
    Boolean isAlt();
    Boolean isShift();
    Boolean isCommand();
    Boolean isShiftCtrl();
    Boolean isShiftCommand();
    Boolean isShiftAlt();
    Boolean isShiftCtrlAlt();
    Boolean isShiftCommandAlt();
    Boolean isShiftCommandCtrl();
    Boolean isShiftCommandCtrlAlt();
    Boolean isCtrlAlt();
    Boolean isCommandCtrlAlt();
    Boolean isCommandAlt();
    Boolean isCommandCtrl();

    void simulateNext();
    void simulatePrevious();

    void simulateNextSlide();

    void simulatePreviousSlide();

    void simulateStop();
    void simulateStart();
    void simulateReset();

    void clearGoTo();
    int getGoToValue();
}
