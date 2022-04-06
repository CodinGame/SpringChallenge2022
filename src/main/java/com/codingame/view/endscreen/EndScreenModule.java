package com.codingame.view.endscreen;

import com.codingame.gameengine.core.AbstractPlayer;
import com.codingame.gameengine.core.GameManager;
import com.codingame.gameengine.core.Module;
import com.google.inject.Inject;

public class EndScreenModule implements Module {

    private GameManager<AbstractPlayer> gameManager;
    private int[] scores;
    private boolean tie;

    @Inject
    EndScreenModule(GameManager<AbstractPlayer> gameManager) {
        this.gameManager = gameManager;
        gameManager.registerModule(this);
    }

    public void setScores(int[] scores, boolean tie) {
        this.scores = scores;
        this.tie = tie;

    }

    @Override
    public final void onGameInit() {
    }

    @Override
    public final void onAfterGameTurn() {
    }

    @Override
    public final void onAfterOnEnd() {
        gameManager.setViewData("endScreen", new Object[] { scores, tie });
    }

}