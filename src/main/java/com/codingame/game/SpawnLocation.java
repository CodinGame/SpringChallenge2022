package com.codingame.game;

public class SpawnLocation {
    Vector position, symetry;
    Vector direction;

    SpawnLocation(int x, int y) {
        position = new Vector(x, y);
        symetry = new Vector(Configuration.MAP_WIDTH - x, Configuration.MAP_HEIGHT - y);
        direction = new Vector(0, position.getY() <= Configuration.MAP_HEIGHT / 2 ? 1 : -1);
    }
}
