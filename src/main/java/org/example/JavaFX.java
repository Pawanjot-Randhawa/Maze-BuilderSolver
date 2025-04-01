package org.example;

import java.io.IOException;
import java.util.List;

import org.example.Native.MazeSolver;

import javafx.application.Application;
import javafx.stage.Stage;

public class JavaFX extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        ViewFactory viewFactory = new ViewFactory(stage);
        viewFactory.showMainMenuView();
    }

    public static void main(String[] args) {
        int[][] _maze = {
            {8, 0, 1, 0, 0, 3},
            {1, 0, 1, 0, 1, 0},
            {0, 0, 0, 0, 1, 3},
            {1, 1, 1, 0, 1, 1},
            {0, 0, 0, 0, 0, 3}
        };

        Maze maze = new Maze(_maze, 5, 5);
        MazeSolver.InitializeMaze(maze.getMazeArray());
        
        List<int[]> path = MazeSolver.AStar();  // Call the native method
        for(var pairs : path) {
            System.out.println(pairs[0] + " , " + pairs[1]);
        }
        // solvedMaze = MazeSolver.solveMaze(maze.getMazeArray()); 

        // If the maze is solved, print the solved maze
        System.out.println("Solved Maze:");
        // for (int[] solvedMaze1 : solvedMaze) {
        //     for (int j = 0; j < solvedMaze1.length; j++) {
        //         System.out.print(solvedMaze1[j] + " ");
        //     }
        //     System.out.println();
        // }
        
        
        
        launch();
    }

}