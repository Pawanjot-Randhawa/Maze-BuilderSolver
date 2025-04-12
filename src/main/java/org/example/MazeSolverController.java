package org.example;
import java.util.List;

import org.example.Native.MazeSolver;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

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
        System.out.println("Created Maze Solver Controller");
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
            updatePlaybackButtons();
        });
        lastStep.setDisable(true); //on start it should be off
        lastStep.setOnAction(event -> {
            removeStep();
            step -= 1;
            updatePlaybackButtons();
        });
        playPause.setOnAction(event -> {
            if(playPause.isSelected()){ //on toggle on

                solvingAnimation.setCycleCount(solvingSteps.size()-(step)); //update animation size based on steps

                solvingAnimation.play();
                nextStep.setDisable(true);
                lastStep.setDisable(true);
            }
            if(!playPause.isSelected()){ //on toggle off
                solvingAnimation.stop();
                updatePlaybackButtons();
            }
        });
        solvingAnimation = new Timeline(
                new KeyFrame(Duration.millis(750), event -> {
                    playStep();
                    step += 1;
                })
        );
        solvingAnimation.setOnFinished(event -> {
            playPause.setSelected(false);
            updatePlaybackButtons();
        });
        resetBtn.setOnAction(event -> {
            solvingAnimation.stop();
            step = 0;
            nextStep.setDisable(false);
            lastStep.setDisable(true);
            playPause.setDisable(false);
            playPause.setSelected(false);
            copyMazeArray();

        });


        copyMazeArray();

    }

    public void updatePlaybackButtons(){
        lastStep.setDisable(step == 0); //if we are on first step then the button should be disabled

        if(step>=solvingSteps.size()){
            playPause.setDisable(true);
            nextStep.setDisable(true);
        }else{
            playPause.setDisable(false);
            nextStep.setDisable(false);
        }
    }

    public void playStep(){
        //temp way to stop it from breaking
        if(step>=solvingSteps.size()){
            System.out.println("Force-Stop NEXT-STEP, timing issue");
            step = this.solvingSteps.size()-1; //the program sometimes break due to a weird timing issue, this will fix any weird bugs
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

    public void removeStep(){
        //avoid code breaking
        if(step<0){
            System.out.println("Force-Stop LAST-STEP, this should never trigger");
            return;
        }
        grid.getChildren().remove(grid.getChildren().size()-1); //the get children preserves insertion order to just remove the last one
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
