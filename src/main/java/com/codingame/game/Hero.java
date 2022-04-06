package com.codingame.game;

import com.codingame.game.action.Action;

public class Hero extends GameEntity {
    int index;
    Player owner;
    double rotation;
    Action intent;
    String message;

    public Hero(int index, Vector position, Player owner, double rotation) {
        super(position, owner.getIndex());
        this.index = index;
        this.owner = owner;
        this.rotation = rotation;
        this.intent = Action.IDLE;
    }

    @Override
    protected Player getOwner() {
        return owner;
    }

    public void setMessage(String message) {
        this.message = message;
        if (message != null && message.length() > 48) {
            this.message = message.substring(0, 46) + "...";
        }

    }

}
