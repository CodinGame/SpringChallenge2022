package com.codingame.game.action;

import com.codingame.game.Vector;

public class Action {

    public static final Action IDLE = new IdleAction();

    Vector destination;
    ActionType type;
    int target;
    boolean forced;
    
    public boolean getForced() {
        return forced;
    }

    public void setForced(boolean forced) {
        this.forced = forced;
    }

    public Action(ActionType type) {
        this.type = type;
    }
    
    public ActionType getType() {
        return type;
    }

    public Vector getDestination() {
        return destination;
    }

    public void setDestination(Vector target) {
        this.destination = target;
    }
    
    public void setTarget(int target) {
        this.target = target;
    }
    
    public int getTarget() {
        return target;
    }
    
    public boolean isMove() {
        return this.getType() == ActionType.MOVE;
    }

}