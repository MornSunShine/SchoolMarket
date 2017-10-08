package com.main.maomorn.bean;

/**
 * Created by MaoMorn on 2017-04-27.
 */

public class LoveBean {
    private int numLove;
    private boolean focusedLove;

    public LoveBean() {
    }

    public LoveBean(int num, boolean focused) {
        numLove = num;
        focusedLove = focused;
    }

    public void setLoveFocused(boolean isFocused) {
        focusedLove = isFocused;
    }

    public boolean isFocusedLove() {
        return focusedLove;
    }

    public void setNumLove(int num) {
        numLove = num;
    }

    public int getNumLove() {
        return numLove;
    }

    public void cancelLoveState() {
        focusedLove = false;
        numLove--;
    }

    public void setLoveState() {
        focusedLove = true;
        numLove++;
    }

    public void changeLoveState() {
        if (focusedLove) {
            focusedLove = false;
            numLove--;
        } else {
            focusedLove = true;
            numLove++;
        }
    }
}
