package org.example;

import java.util.List;

import org.example.Native.MazeSolver;

/**
 * A Facade class for 
 * accessing the JNI 
 */
public class SolverAPI {
    private Maze maze;
    private static volatile SolverAPI helper;

    /**
     * Default Constructor to default assign the maze
     */
    private SolverAPI() {
        this.maze = null;
    }


    /**
     * Returns an instance of SolverAPI, the same
     * instance, singleton method, working for 
     * multi-threaded and single-threaded. And Sets the maze
     * @param maze
     * @return localRef : [SolverAPI] 
     */
    public static SolverAPI GetInstance(Maze maze) {   
        SolverAPI localRef = helper;
        if(helper == null) {
            synchronized (SolverAPI.class) {
                localRef = helper;
                if(localRef == null) {
                    helper = localRef = new SolverAPI();
                }
            }
        }

        localRef.SetMaze(maze);
        return localRef;
    }


    /**
     * Sets the maze
     * @param maze
     */
    private void SetMaze(Maze maze) {
        this.maze = maze;
    }
    

    /**
     * Initializes the maze and returns 
     * the steps for solving the maze
     * @param strategy
     * @return path: [List<int[]>] 
     */
    public List<int[]> SolveWith(SolveStrategy strategy) {
        MazeSolver.InitializeMaze(this.maze.getMazeArray());
        return MazeSolver.SolveMaze(strategy.get());
    }


    /**
     * Initializes the maze and returns 
     * the steps for solving the maze,
     * returns a step generator, that will give steps
     * 1 by 1
     * @param strategy
     * @return StepGenerator 
     */
    public StepGenerator Solve(SolveStrategy strategy) {
        MazeSolver.InitializeMaze(this.maze.getMazeArray());
        return new StepGenerator(MazeSolver.SolveMaze(strategy.get()));
    }
}
