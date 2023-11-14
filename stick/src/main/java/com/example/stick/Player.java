package com.example.stick;

public abstract class Player {
    private String name;

    public Player(String name) {
        this.name = name;
    }

    public abstract void createBridge();

    public abstract void measureDistance();

    public String getName() {
        return name;
    }
}
