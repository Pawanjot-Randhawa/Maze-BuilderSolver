package org.example;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.control.Spinner;

public class MazeSolverController {
    private final ViewFactory viewFactory;
    private final Maze maze;

    //setting colors
    Color start = Color.LIGHTGREEN;
    Color end = Color.DARKRED;
    Color path = Color.LIGHTGOLDENRODYELLOW;
    Color wall = Color.LIGHTCORAL;


    public MazeSolverController(ViewFactory viewFactory, Maze maze) {
        this.viewFactory = viewFactory;
        this.maze = maze;
    }

    @FXML
    private ToggleButton playPause;
    @FXML
    private Button switchBuild;
    @FXML
    private Button nextStep;
    @FXML
    private Button lastStep;
    @FXML
    private Button skipBtn;
    @FXML
    private Spinner playSpeed;;
    @FXML
    private GridPane grid;


    @FXML
    public void initialize() {
        switchBuild.setOnAction(event -> {
            viewFactory.showMazeBuilderView3();
        });
        copyMazeArray();

    }

    public void copyMazeArray(){
        grid.getChildren().clear();
        int[][] mazeArray = maze.getMazeArray();

        for (int row = 0; row < maze.getMazeHeight(); row++) {
            for (int col = 0; col < maze.getMazeWidth(); col++) {
                // Create a rectangle for each grid cell
                Rectangle cell = new Rectangle();

                if(mazeArray[row][col]==1){
                    cell.setFill(wall);
                }else if(mazeArray[row][col]==0){
                    cell.setFill(path);
                }else if (mazeArray[row][col]==8) {
                    cell.setFill(start);
                }else if (mazeArray[row][col]==3) {
                    cell.setFill(end);
                }
                cell.setStroke(Color.BLACK);  // Add a border
                // Dynamically bind the size of the rectangle
                cell.widthProperty().bind(grid.widthProperty().divide(maze.getMazeWidth())); // Width of each rectangle
                cell.heightProperty().bind(grid.heightProperty().divide(maze.getMazeHeight())); // Height of each rectangle
                // Add the rectangle to the GridPane at the correct position
                grid.add(cell, col, row);
            }
        }
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
