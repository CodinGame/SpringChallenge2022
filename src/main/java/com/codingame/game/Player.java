package com.codingame.game;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.codingame.gameengine.core.AbstractMultiplayerPlayer;

public class Player extends AbstractMultiplayerPlayer {

    List<Hero> heroes = new ArrayList<>();
    private int mana = Configuration.STARTING_MANA;
    private boolean manaChanged = true;
    private boolean baseHealthChanged = true;
    private int baseHealth = Configuration.STARTING_BASE_HEALTH;
    public Set<Integer> spotted = new HashSet<>();
    private int manaGainedOutsideOfBase = 0;

    @Override
    public int getExpectedOutputLines() {
        return heroes.size();
    }

    public void addHero(Hero hero) {
        heroes.add(hero);
    }

    public void gainMana(Integer[] amount) {
        mana += amount[0];
        manaGainedOutsideOfBase += amount[1];
        if (Configuration.MAX_MANA > 0) {
            if (mana > Configuration.MAX_MANA) {
                mana = Configuration.MAX_MANA;
            }
            if (manaGainedOutsideOfBase > Configuration.MAX_MANA) {
                manaGainedOutsideOfBase = Configuration.MAX_MANA;
            }
        }
        manaChanged = true;
    }

    public int getMana() {
        return mana;
    }

    public boolean manaHasChanged() {
        return manaChanged;
    }

    public void resetViewData() {
        manaChanged = false;
        baseHealthChanged = false;
    }

    public void spendMana(int amount) {
        mana -= amount;
        manaChanged = true;
    }

    public int getBaseHealth() {
        return baseHealth;
    }

    public int getManaGainedOutsideOfBase() {
        return manaGainedOutsideOfBase;
    }

    public void damageBase() {
        this.baseHealth--;
        if (this.baseHealth < 0) {
            this.baseHealth = 0;
        }
        baseHealthChanged = true;
    }

    public boolean baseHealthHasChanged() {
        return baseHealthChanged;
    }

}
