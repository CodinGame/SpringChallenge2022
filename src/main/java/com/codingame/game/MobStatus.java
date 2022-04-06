package com.codingame.game;

public class MobStatus {
    int turns;
    Player target;
    int state;

    static final int YOU = 1;
    static final int ENEMY = 2;
    static final int NEITHER = 0;

    public MobStatus(int state, Player target, int turns) {
        this.turns = turns;
        this.target = target;
        this.state = state;
    }

    public String toStringFor(Player player) {
        return String.format(
            "%d %d",
            state,
            target == null ? NEITHER : (target == player ? YOU : ENEMY)

        );
    }

}
