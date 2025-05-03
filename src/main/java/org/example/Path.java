package org.example;

public enum Path {
    WALL(8),
    PATH(0),
    GOAL(1),
    START(2);

    private final int value;
 
    Path(int value) {
        this.value = value;
    }

    public int get() {
        return this.value;
    }
}

/*
 * WALL 8
 * GOAL 1
 * PATH 0
 * START 2
 * BLOCKED 7
 */