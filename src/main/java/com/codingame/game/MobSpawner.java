package com.codingame.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MobSpawner {

    private Random random;
    private SpawnLocation[] spawnLocations;
    private double maxDirectionDelta;
    private int spawnRate;
    private int lastSpawn;
    private double currentMaxHealth = Configuration.MOB_STARTING_MAX_ENERGY;

    public MobSpawner(Random random, SpawnLocation[] spawnLocations, double maxDirectionDelta, int spawnRate) {
        this.random = random;

        this.spawnLocations = spawnLocations;
        this.maxDirectionDelta = maxDirectionDelta;
        this.spawnRate = spawnRate;

        lastSpawn = -spawnRate;
    }

    List<Mob> update(int turn) {
        // It's the end of the game, help the random generate a suitably epic final frame
        boolean suddenDeath = turn >= 200;

        if (turn - lastSpawn >= spawnRate) {
            lastSpawn = turn;
            return spawn(suddenDeath);
        }
        return Collections.emptyList();
    }

    private Vector opposite(Vector v) {
        return new Vector(Configuration.MAP_WIDTH - v.getX(), Configuration.MAP_HEIGHT - v.getY());
    }

    private List<Mob> spawn(boolean suddenDeath) {
        List<Mob> newMobs = new ArrayList<>();

        for (SpawnLocation pairToUse : spawnLocations) {
            Vector suddenDeathTarget = null;

            if (suddenDeath) {
                int tx = random.nextInt(Configuration.BASE_ATTRACTION_RADIUS);
                int ty = random.nextInt(Configuration.BASE_ATTRACTION_RADIUS);
                if (random.nextBoolean()) {
                    tx = Configuration.MAP_WIDTH - tx;
                    ty = Configuration.MAP_HEIGHT - ty;
                }
                suddenDeathTarget = new Vector(tx, ty);
            }

            double directionDelta = random.nextDouble() * maxDirectionDelta * 2 - maxDirectionDelta;
            for (int i = 0; i < 2; ++i) {
                Vector location = i == 0 ? pairToUse.position : pairToUse.symetry;
                Vector direction = i == 0 ? pairToUse.direction : pairToUse.direction.symmetric();
                Mob mob = new Mob(location, (int) currentMaxHealth);
                if (suddenDeath) {
                    Vector v = new Vector(location, i == 0 ? suddenDeathTarget : opposite(suddenDeathTarget)).normalize()
                        .mult(Configuration.MOB_MOVE_SPEED).truncate();
                    mob.setSpeed(v);
                } else {
                    mob.setSpeed(direction.rotate(directionDelta).normalize().mult(Configuration.MOB_MOVE_SPEED).truncate());
                }
                newMobs.add(mob);
            }

        }
        currentMaxHealth += Configuration.MOB_GROWTH_MAX_ENERGY;
        return newMobs;
    }

}
