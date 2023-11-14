package com.example.stick;

public class StickHeroGame extends Game {
    private int level;

    public StickHeroGame() {
        super();
        this.level = 1;
    }

    @Override
    public void startGame() {
        System.out.println("Stick Hero game started!");
    }

    @Override
    public void endGame() {
        System.out.println("Stick Hero game ended!");
    }

    @Override
    public void increaseScore(int points) {
        this.getScore();
        this.levelUp();
        System.out.println("Score increased by " + points + " points!");
    }

    private void levelUp() {
        System.out.println("Level up! Current level: " + ++level);
    }

    public void specialPower() {
        System.out.println("Stick Hero's special power activated!");
    }
}
