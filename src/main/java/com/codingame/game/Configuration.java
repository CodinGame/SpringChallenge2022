package com.codingame.game;

import java.util.Properties;

public class Configuration {

    public static int MAP_WIDTH = 17630;
    public static int MAP_HEIGHT = 9000;
    public static int MAP_LIMIT = 800;

    public static int BASE_ATTRACTION_RADIUS = 5000;
    public static int BASE_VIEW_RADIUS = 6000;
    public static int BASE_RADIUS = 300;

    public static int HERO_MOVE_SPEED = 800;
    public static int HEROES_PER_PLAYER = 3;
    public static int HERO_VIEW_RADIUS = 2200;
    public static int HERO_ATTACK_RANGE = 800;
    public static int HERO_ATTACK_DAMAGE = 2;

    public static int MAX_MANA = -1;
    public static int STARTING_MANA = 0;
    public static int STARTING_BASE_HEALTH = 3;

    public static int MOB_MOVE_SPEED = 400;
    public static final SpawnLocation[] MOB_SPAWN_LOCATIONS = new SpawnLocation[] {
        new SpawnLocation(MAP_WIDTH / 2, -MAP_LIMIT + 1),
        new SpawnLocation(MAP_WIDTH / 2 + 4000, -MAP_LIMIT + 1),
    };
    public static final double MOB_SPAWN_MAX_DIRECTION_DELTA = 5 * Math.PI / 12;
    public static int MOB_SPAWN_RATE = 5;
    public static int MOB_STARTING_MAX_ENERGY = 10;
    public static double MOB_GROWTH_MAX_ENERGY = 0.5;

    public static int SPELL_WIND_COST = 10;
    public static int SPELL_CONTROL_COST = 10;
    public static int SPELL_PROTECT_COST = 10;
    public static int SPELL_PROTECT_DURATION = 12;
    public static int SPELL_WIND_DISTANCE = 2200;
    public static int SPELL_WIND_RADIUS = 1280;
    public static boolean ENABLE_FOG = true;
    public static boolean ENABLE_WIND = true;
    public static boolean ENABLE_CONTROL = true;
    public static boolean ENABLE_SHIELD = true;
    public static boolean ENABLE_TIE_BREAK = true;

    private static int getFromParams(Properties params, String name, int defaultValue) {
        String inputValue = params.getProperty(name);
        if (inputValue != null) {
            try {
                return Integer.parseInt(inputValue);
            } catch (NumberFormatException e) {
                // Do naught
            }
        }
        return defaultValue;
    }

    private static double getFromParams(Properties params, String name, double defaultValue) {
        String inputValue = params.getProperty(name);
        if (inputValue != null) {
            try {
                return Double.parseDouble(inputValue);
            } catch (NumberFormatException e) {
                // Do naught
            }
        }
        return defaultValue;
    }

    public static void take(Properties params) {
        SPELL_CONTROL_COST = getFromParams(params, "SPELL_CONTROL_COST", SPELL_CONTROL_COST);
        SPELL_PROTECT_COST = getFromParams(params, "SPELL_PROTECT_COST", SPELL_PROTECT_COST);
        SPELL_WIND_COST = getFromParams(params, "SPELL_PUSH_COST", SPELL_WIND_COST);
        SPELL_WIND_DISTANCE = getFromParams(params, "SPELL_PUSH_DISTANCE", SPELL_WIND_DISTANCE);
        SPELL_WIND_RADIUS = getFromParams(params, "SPELL_PUSH_RADIUS", SPELL_WIND_RADIUS);
        SPELL_PROTECT_DURATION = getFromParams(params, "SPELL_PROTECT_DURATION", SPELL_PROTECT_DURATION);
        MAP_WIDTH = getFromParams(params, "MAP_WIDTH", MAP_WIDTH);
        MAP_HEIGHT = getFromParams(params, "MAP_HEIGHT", MAP_HEIGHT);
        BASE_ATTRACTION_RADIUS = getFromParams(params, "BASE_ATTRACTION_RADIUS", BASE_ATTRACTION_RADIUS);
        BASE_VIEW_RADIUS = getFromParams(params, "BASE_VIEW_RADIUS", BASE_VIEW_RADIUS);
        BASE_RADIUS = getFromParams(params, "BASE_RADIUS", BASE_RADIUS);
        HERO_MOVE_SPEED = getFromParams(params, "HERO_MOVE_SPEED", HERO_MOVE_SPEED);
        HEROES_PER_PLAYER = getFromParams(params, "HEROES_PER_PLAYER", HEROES_PER_PLAYER);
        HERO_VIEW_RADIUS = getFromParams(params, "HERO_VIEW_RADIUS", HERO_VIEW_RADIUS);
        HERO_ATTACK_RANGE = getFromParams(params, "HERO_ATTACK_RANGE", HERO_ATTACK_RANGE);
        HERO_ATTACK_DAMAGE = getFromParams(params, "HERO_ATTACK_DAMAGE", HERO_ATTACK_DAMAGE);
        MAX_MANA = getFromParams(params, "MAX_MANA", MAX_MANA);
        STARTING_MANA = getFromParams(params, "STARTING_MANA", STARTING_MANA);
        STARTING_BASE_HEALTH = getFromParams(params, "STARTING_BASE_HEALTH", STARTING_BASE_HEALTH);
        MOB_MOVE_SPEED = getFromParams(params, "MOB_MOVE_SPEED", MOB_MOVE_SPEED);
        MOB_SPAWN_RATE = getFromParams(params, "MOB_SPAWN_RATE", MOB_SPAWN_RATE);
        MOB_STARTING_MAX_ENERGY = getFromParams(params, "MOB_STARTING_MAX_ENERGY", MOB_STARTING_MAX_ENERGY);
        MOB_GROWTH_MAX_ENERGY = getFromParams(params, "MOB_GROWTH_MAX_ENERGY", MOB_GROWTH_MAX_ENERGY);
    }
}
