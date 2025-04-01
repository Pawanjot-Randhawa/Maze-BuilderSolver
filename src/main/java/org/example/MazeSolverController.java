package org.example;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.control.Spinner;
import org.example.Native.MazeSolver;

import java.util.List;

public class MazeSolverController {
    private final ViewFactory viewFactory;
    private final Maze maze;
    List<int[]> pathYes;
    int temp;
    private Thread setup;

    //setting colors
    Color start = Color.LIGHTGREEN;
    Color end = Color.DARKRED;
    Color path = Color.LIGHTGOLDENRODYELLOW;
    Color wall = Color.LIGHTCORAL;


    public MazeSolverController(ViewFactory viewFactory, Maze maze) {
        this.viewFactory = viewFactory;
        this.maze = maze;
        System.out.println("Maze Solver Controller");
        printMazeArray(this.maze.getMazeArray());
        this.setup = new Thread(() -> {
            MazeSolver.InitializeMaze(this.maze.getMazeArray());
        });
        this.temp = 0;
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
        System.out.println("Maze Solver Start Initialized");
        switchBuild.setOnAction(event -> {
            viewFactory.showMazeBuilderView(getMazeArray());
        });
        nextStep.setOnAction(event -> {
            tempPlay();
            temp += 1;
        });
        copyMazeArray();
        System.out.println("Maze Solver Initialized");

    }

    public void tempPlay(){
        if(this.setup.isAlive()){
            try{
                this.setup.join();
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
        this.pathYes = MazeSolver.AStar();
        int[] values = pathYes.get(temp);
        Rectangle cell = new Rectangle();

        cell.setFill(Color.GAINSBORO);

        cell.widthProperty().bind(grid.widthProperty().divide(maze.getMazeWidth())); // Width of each rectangle
        cell.heightProperty().bind(grid.heightProperty().divide(maze.getMazeHeight())); // Height of each rectangle


        grid.add(cell, values[0], values[1]);

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

    private Maze getMazeArray(){
        int[][] mazeArray = new int[maze.getMazeHeight()][maze.getMazeWidth()];

        for (Node cell : grid.getChildren()) {
            int col = GridPane.getColumnIndex(cell);
            int row = GridPane.getRowIndex(cell);
            cell = (Rectangle)cell;
            if(((Rectangle) cell).getFill().equals(wall)){
                mazeArray[row][col] = 1;
            }else if(((Rectangle) cell).getFill().equals(path)){
                mazeArray[row][col] = 0;
            }else if (((Rectangle) cell).getFill().equals(start)) {
                mazeArray[row][col] = 8;
            }else if (((Rectangle) cell).getFill().equals(end)) {
                mazeArray[row][col] = 3;
            }
        }
        return new Maze(mazeArray, maze.getMazeWidth(), maze.getMazeHeight());
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
