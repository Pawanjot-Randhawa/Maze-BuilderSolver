package org.example;

public enum SolveStrategy {
    ASTAR("AStar"),
    DIJKSTRA("Dijkstra"),;

    private String strategy = null;
    
    private SolveStrategy(String strategy) {
        this.strategy = strategy;
    }

    public String get() {
        return this.strategy;
    }

};
