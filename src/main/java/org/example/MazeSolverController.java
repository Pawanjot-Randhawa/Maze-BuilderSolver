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


    public MazeSolverController(ViewFactory viewFactory) {
        this.viewFactory = viewFactory;
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
    public void initialize() {
        switchBuild.setOnAction(event -> {
            viewFactory.showMazeBuilderView3();
        });

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
