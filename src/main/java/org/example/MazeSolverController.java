package org.example;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class MazeSolverController {
    private final ViewFactory viewFactory;


    public MazeSolverController(ViewFactory viewFactory) {
        this.viewFactory = viewFactory;
    }


    @FXML
    public void initialize() {

    }


    private void printMazeArray(int[][] mazeArray){
        for (int i = 0; i < mazeArray.length; i++) {
            for (int j = 0; j < mazeArray[0].length; j++) {
                System.out.print(mazeArray[i][j] + " ");
            }
            System.out.println();
        }
    }


}
