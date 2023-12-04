package com.example.stick;

public class StickHeroPlayer extends Player {
    private int currentDistance;

    public StickHeroPlayer(String name) {
        super(name);
        this.currentDistance = 0;
    }

    @Override
    public void createBridge() {
        System.out.println(getName() + " created a bridge in Stick Hero!");
    }

    @Override
    public void measureDistance() {
        System.out.println("Distance measured in Stick Hero.");
    }

    public void usePowerUp() {
        System.out.println(getName() + " used a power-up in Stick Hero!");
    }
}
