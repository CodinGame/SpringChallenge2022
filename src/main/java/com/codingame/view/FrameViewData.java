package com.codingame.view;

import java.util.List;
import java.util.Map;

public class FrameViewData {
    public static class EntityData {
        public int type, id;
        public Integer health;
    }

    // States
    public Map<Integer, Coord> positions;
    public Map<Integer, String> messages;
    public List<Integer> controlled;
    public List<Integer> pushed;
    public List<Integer> shielded;

    // Diffs
    public Map<Integer, Integer> mana;
    public Map<Integer, Integer> baseHealth;
    public Map<Integer, Integer> mobHealth;

    // Events
    public List<EntityData> spawns;
    public List<Attack> attacks;
    public List<BaseAttack> baseAttacks;
    public List<SpellUse> spellUses;
}
