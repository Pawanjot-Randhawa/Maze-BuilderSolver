package org.example;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class MazeSolverController {
    private final ViewFactory viewFactory;
    private final Maze maze;
    List<int[]> solvingSteps;
    private int step;
    Timeline solvingAnimation;
    private Color solutionColor = Color.DARKOLIVEGREEN;
    private List<Color> colorList = List.of(
            //traffic lights
            Color.RED,
            Color.GREEN,
            Color.BLUE
    );
    private boolean flag = true;
    private int counter = 0;

    //setting colors
    Color start = Color.LIGHTGREEN;
    Color end = Color.DARKRED;
    Color path = Color.LIGHTGOLDENRODYELLOW;
    Color wall = Color.LIGHTCORAL;


    public MazeSolverController(ViewFactory viewFactory, Maze maze) {
        System.out.println("Created Maze Solver Controller");
        this.viewFactory = viewFactory;
        this.maze = maze;

        this.solvingSteps = SolverAPI.GetInstance(maze)
                                     .SolveWith(SolveStrategy.ASTAR);
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
    private ComboBox playSpeed;;
    @FXML
    private GridPane grid;
    @FXML
    private ScrollPane algoSelector;
    @FXML
    private BorderPane borderRoot;


    @FXML
    public void initialize() {
        MenuBarBuilder builder = new MenuBarBuilder(this.viewFactory);

        borderRoot.setTop(builder.buildForSolver());

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
                new KeyFrame(Duration.millis(1000), event -> {
                    playStep();
                    step += 1;
                })
        );
        solvingAnimation.setOnFinished(event -> {
            playPause.setSelected(false);
            updatePlaybackButtons();
        });

        playSpeed.getItems().addAll("1x", "2x", "3x", "4x", "5x");
        playSpeed.setValue("1x");
        solvingAnimation.setRate(1.5);
        playSpeed.setEditable(false);
        playSpeed.setOnAction(event -> {
            solvingAnimation.setRate(Double.parseDouble(playSpeed.getValue().toString().substring(0, playSpeed.getValue().toString().length() - 1)));

        });


        resetBtn.setOnAction(event -> {
            solvingAnimation.stop();
            step = 0;
            updatePlaybackButtons();
            copyMazeArray();

        });


        copyMazeArray();
        configureAlgorithms();

    }

    public void updatePlaybackButtons(){
        lastStep.setDisable(step == 0); //if we are on first step then the button should be disabled

        if(step>=solvingSteps.size()){
            playPause.setDisable(true);
            nextStep.setDisable(true);
        }else{
            playPause.setDisable(false);
            playPause.setSelected(false);
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

        if(Arrays.equals(values, solvingSteps.get(0))){
            if (counter+1 == colorList.size()) {
                this.solutionColor = colorList.get(0);
                counter = 0;
            }else{
                counter += 1;
                this.solutionColor = colorList.get(counter);
            }

        }
        Rectangle cell = new Rectangle();

        cell.setFill(this.solutionColor);
        //cell.setFill(Color.DARKOLIVEGREEN);

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

                if(mazeArray[row][col]==8){
                    cell.setFill(wall);
                }else if(mazeArray[row][col]==0){
                    cell.setFill(path);
                }else if (mazeArray[row][col]==2) {
                    cell.setFill(start);
                }else if (mazeArray[row][col]==1) {
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

    public void configureAlgorithms(){
        VBox content = new VBox();
        content.setFillWidth(true);

        ToggleGroup algos = new ToggleGroup();

        String[] algorithms = {"A*", "Dijkstra", "BlockNPath", "BFS", "IDAStar"};

        for (String algo : algorithms) {
            ToggleButton button = new ToggleButton(algo);
            button.setMinHeight(80);
            button.setMaxWidth(Double.MAX_VALUE);
            button.setToggleGroup(algos);
            button.setOnMousePressed(event -> {
                Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
                confirmation.setTitle("Configure Algorithm Change");
                confirmation.setHeaderText(null);
                confirmation.setContentText("Are you sure you want to switch to the " + algo + " algorithm?");
                confirmation.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
                Optional<ButtonType> result = confirmation.showAndWait();
                if (result.get() == ButtonType.YES) {
                    button.setSelected(true);
                    changeAlgorithms(button.getText());
                }else{
                    event.consume();
                }
            });
            if (algo.equals("A*")) { //default
                button.setSelected(true);
            }
            content.getChildren().add(button);

        }

        algoSelector.setFitToWidth(true);
        algoSelector.setContent(content);

    }

    private void changeAlgorithms(String algo){
        switch(algo){
            case "A*":
                this.solvingSteps = SolverAPI.GetInstance(maze).SolveWith(SolveStrategy.ASTAR);
                break;
            case "Dijkstra":
                this.solvingSteps = SolverAPI.GetInstance(maze).SolveWith(SolveStrategy.DIJKSTRA);
                break;
            case "BlockNPath":
                this.solvingSteps = SolverAPI.GetInstance(maze).SolveWith(SolveStrategy.BLOCKNDPATH);
                break;
            case "BFS":
                this.solvingSteps = SolverAPI.GetInstance(maze).SolveWith(SolveStrategy.BFS);
                break;
            case "IDAStar":
                this.solvingSteps = SolverAPI.GetInstance(maze).SolveWith(SolveStrategy.IDASTAR);
                break;
            default:
                break;
        }
        resetBtn.fire();
    }

    private Maze getMazeArray(){
        int[][] mazeArray = new int[maze.getMazeHeight()][maze.getMazeWidth()];

        for (Node cell : grid.getChildren()) {
            int col = GridPane.getColumnIndex(cell);
            int row = GridPane.getRowIndex(cell);
            if(((Rectangle) cell).getFill().equals(wall)){
                mazeArray[row][col] = 8;
            }else if(((Rectangle) cell).getFill().equals(path)){
                mazeArray[row][col] = 0;
            }else if (((Rectangle) cell).getFill().equals(start)) {
                mazeArray[row][col] = 2;
            }else if (((Rectangle) cell).getFill().equals(end)) {
                mazeArray[row][col] = 1;
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
