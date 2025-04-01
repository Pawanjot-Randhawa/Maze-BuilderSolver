package org.example;
import java.util.List;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.util.Duration;
import org.example.Native.MazeSolver;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class MazeSolverController {
    private final ViewFactory viewFactory;
    private final Maze maze;
    List<int[]> solvingSteps;
    private int step;
    Timeline solvingAnimation;

    //setting colors
    Color start = Color.LIGHTGREEN;
    Color end = Color.DARKRED;
    Color path = Color.LIGHTGOLDENRODYELLOW;
    Color wall = Color.LIGHTCORAL;


    public MazeSolverController(ViewFactory viewFactory, Maze maze) {
        this.viewFactory = viewFactory;
        this.maze = maze;
        MazeSolver.InitializeMaze(maze.getMazeArray());
        this.solvingSteps = MazeSolver.AStar();
        this.step = 0;
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
    private Button resetBtn;
    @FXML
    private Spinner playSpeed;;
    @FXML
    private GridPane grid;


    @FXML
    public void initialize() {
        switchBuild.setOnAction(event -> {
            viewFactory.showMazeBuilderView(getMazeArray());
        });
        nextStep.setOnAction(event -> {
            playStep();
            step += 1;
            if(step>=solvingSteps.size()){
                nextStep.setDisable(true);
                return;
            }
            solvingAnimation.setCycleCount(solvingSteps.size()-(step));
        });
        playPause.setOnAction(event -> {
            if(playPause.isSelected()){
                solvingAnimation.play();
            }
            if(!playPause.isSelected()){
                solvingAnimation.pause();
            }
        });
        solvingAnimation = new Timeline(
                new KeyFrame(Duration.millis(750), event -> {
                    playStep();
                    step += 1;
                })
        );
        solvingAnimation.setCycleCount(solvingSteps.size()-(step));
        solvingAnimation.setOnFinished(event -> {
            playPause.setSelected(false);
            playPause.setDisable(true);
            nextStep.setDisable(true);
        });
        resetBtn.setOnAction(event -> {
            solvingAnimation.stop();
            step = 0;
            solvingAnimation.setCycleCount(solvingSteps.size()-(step));
            nextStep.setDisable(false);
            playPause.setDisable(false);
            playPause.setSelected(false);
            copyMazeArray();

        });


        copyMazeArray();

    }

    public void playStep(){
        //temp way to stop it from breaking
        if(step>=solvingSteps.size()){
            System.out.println("Force-Stop");
            return;
        }

        int[] values = solvingSteps.get(step);
        Rectangle cell = new Rectangle();

        cell.setFill(Color.DARKOLIVEGREEN);

        cell.widthProperty().bind(grid.widthProperty().divide(maze.getMazeWidth()*3)); // times 2 to make it smaller as the denominator needs to be bigger
        cell.heightProperty().bind(grid.heightProperty().divide(maze.getMazeHeight()*3));
        grid.setHalignment(cell, HPos.CENTER);
        grid.setValignment(cell, VPos.CENTER);

        grid.add(cell, values[1], values[0]);

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
