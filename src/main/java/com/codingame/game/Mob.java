package com.codingame.game;

import java.util.ArrayList;
import java.util.List;

public class Mob extends GameEntity {

    private Vector speed;
    private int health;
    boolean healthChanged = true;
    public MobStatus status;
    protected List<Vector> nextControls;

    public Mob(Vector position, int health) {
        super(position, Referee.TYPE_MOB);
        speed = new Vector(0, 0);
        this.health = health;
        pushed = false;
        nextControls = new ArrayList<>();
    }

    public boolean isAlive() {
        return health > 0;
    }

    public void hit(int damage) {
        health -= damage;
        healthChanged = true;
    }

    public int getHealth() {
        return health;
    }

    public boolean healthHasChanged() {
        return healthChanged;
    }

    public boolean moveCancelled() {
        return !isAlive() || pushed;
    }

    public void setSpeed(Vector speed) {
        this.speed = speed;
        status = null;
    }

    public Vector getSpeed() {
        return speed;

    }

    public void reset() {
        pushed = false;
        healthChanged = false;
        activeControls.clear();
        activeControls.addAll(nextControls);
        nextControls.clear();
    }

    @Override
    protected Player getOwner() {
        return null;
    }
    
    @Override
    protected void applyControl(Vector destination) {
        nextControls.add(destination);
    }

    @Override
    public void pushTo(Vector position) {
        super.pushTo(position);
        status = null;
        pushed = true;
    }

}
