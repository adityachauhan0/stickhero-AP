package com.example.stick;

public abstract class Game {
    private int score;

    public Game() {
        this.score = 0;
    }

    public abstract void startGame();

    public abstract void endGame();

    public abstract void increaseScore(int points);

    public int getScore() {
        return score;
    }
}
