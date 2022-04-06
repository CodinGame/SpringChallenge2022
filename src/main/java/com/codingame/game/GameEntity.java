package com.codingame.game;

import java.util.ArrayList;
import java.util.List;

public abstract class GameEntity {
    static private int ENTITY_COUNT = 0;

    protected int id;
    protected Vector position;
    protected int type;
    protected List<Vector> activeControls;
    protected int shieldDuration;
    protected boolean pushed;

    public GameEntity(Vector position, int type) {
        this.id = ENTITY_COUNT++;
        this.position = position;
        this.type = type;
        activeControls = new ArrayList<>();
        shieldDuration = 0;
    }

    public int getId() {
        return id;
    }

    protected void applyControl(Vector destination) {
        activeControls.add(destination);
    }

    public void applyShield() {
        shieldDuration = Configuration.SPELL_PROTECT_DURATION + 1;
    }

    protected abstract Player getOwner();

    public boolean hasActiveShield() {
        return shieldDuration > 0 && shieldDuration < Configuration.SPELL_PROTECT_DURATION + 1;
    }

    public boolean hadActiveShield() {
        return shieldDuration > 0 && shieldDuration < Configuration.SPELL_PROTECT_DURATION;
    }

    public boolean gotPushed() {
        return pushed;
    }

    public boolean isControlled() {
        return !activeControls.isEmpty();
    }

    public void pushTo(Vector position) {
        this.position = position;
    }
}
