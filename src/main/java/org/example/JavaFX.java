package org.example;

import java.io.IOException;

import org.example.Native.MazeSolver;

import javafx.application.Application;
import javafx.stage.Stage;

public class JavaFX extends Application {

    @Override
    public void start(Stage stage) throws IOException {

        /*
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/mainmenu.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(loader.load());
        } catch (Exception e) {
            e.printStackTrace();
        }

        stage.setScene(scene);
        stage.setTitle("Menu");
        stage.show();
        */
        ViewFactory viewFactory = new ViewFactory(stage);
        viewFactory.showMainMenuView();
    }

    public static void main(String[] args) {
        int[][] _maze = {
            {1, 1, 1, 1, 1},
            {1, 0, 0, 0, 1},
            {1, 0, 1, 0, 1},
            {1, 0, 1, 0, 1},
            {1, 1, 1, 1, 1}
        };

        Maze maze = new Maze(_maze, 5, 5);
        int[][] solvedMaze = MazeSolver.solveMaze(maze.getMazeArray());  // Call the native method

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