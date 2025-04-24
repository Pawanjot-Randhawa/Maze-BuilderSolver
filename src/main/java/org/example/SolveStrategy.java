package org.example;
/**
 * 
 * Stores the strategies to 
 * solve the maze as strings. 
 * 
 */
public enum SolveStrategy {
    ASTAR("AStar"),
    DIJKSTRA("Dijkstra"),
    BLOCKNDPATH("BlockNdPath"),
    BFS("Bfs");

    private String strategy = null;
    

    /**
     * Assigns the strategy to the string value
     * @param strategy
     */
    private SolveStrategy(String strategy) {
        this.strategy = strategy;
    }

    
    /**
     * Returns the value of the strategy (the string value)
     * @return strategy: [String] 
     */
    public String get() {
        return this.strategy;
    }

};
