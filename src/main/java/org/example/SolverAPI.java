package org.example;

import java.util.List;

import org.example.Native.MazeSolver;

public class SolverAPI {
    private Maze maze;
    private static volatile SolverAPI helper;

    private SolverAPI() {
        this.maze = null;
    }

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

    private void SetMaze(Maze maze) {
        this.maze = maze;
    }
    
    public List<int[]> SolveWith(SolveStrategy strategy) {
        MazeSolver.InitializeMaze(this.maze.getMazeArray());
        return MazeSolver.SolveMaze(strategy.get());
    }
}
