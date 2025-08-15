package org.example;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.StageStyle;

public class MainMenuController {

    private final int ARBITRARY_LOAD_TIME_IN_MS = 1300; // ms
    private final ViewFactory viewFactory;
    private final ToggleGroup mazeOptions = new ToggleGroup();
    private Maze selectedMaze;
    private ProgressBar bar;

    public MainMenuController(ViewFactory viewFactory) {
        this.viewFactory = viewFactory; // Pass this into the controller
        this.bar = new ProgressBar();
    }

    @FXML
    private Button newMazeBtn;
    @FXML
    private Button openMazeBtn;
    @FXML
    private ScrollPane mazesPane;

    @FXML
    public void initialize() {
        newMazeBtn.setOnAction(event -> {
            viewFactory.showMazeBuilderView(null);
        });
        openMazeBtn.setOnAction(event -> {
            Toggle selectedToggle = mazeOptions.getSelectedToggle();
            if (selectedToggle == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("No Maze Selected");
                alert.setHeaderText(null);
                alert.setContentText("Please select a maze to open.");
                alert.showAndWait();
            } else {
                this.bar.setVisible(true);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.initStyle(StageStyle.UNDECORATED);
                alert.setHeaderText("Loading Maze...");

                VBox box = new VBox(this.bar);
                alert.getDialogPane().setContent(box);
                alert.getButtonTypes().clear();

                Thread getMaze = new Thread(() -> {
                    this.selectedMaze = (Maze) selectedToggle.getUserData();
                });
                getMaze.start();

                Task<Void> showProgress = new Task<>() {
                    @Override
                    protected Void call() throws Exception {
                        Thread.sleep(ARBITRARY_LOAD_TIME_IN_MS);
                        Platform.runLater(() -> {
                            ButtonType closeBtn = new ButtonType("Close");
                            alert.getButtonTypes().add(closeBtn);
                        });
                        return null;
                    }
                };

                showProgress.setOnSucceeded(eh -> {
                    alert.close();
                    viewFactory.showMazeBuilderView(this.selectedMaze);
                });
                alert.show();

                Thread t = new Thread(showProgress);
                t.setDaemon(true);
                t.start();
            }
        });
        populateMazeScrollPane();

    }

    private void populateMazeScrollPane() {
        VBox content = new VBox();
        content.setFillWidth(true);
        System.out.println(Database.GetInstance().getMazes().size() + " Mazes loaded");

        for (Maze maze : Database.GetInstance().getMazes()) {

            ToggleButton mazeButton = new ToggleButton();
            HBox buttonContents = new HBox();

            Label name = new Label(maze.getName());
            Label date = new Label(maze.getDateMade());
            Label size = new Label(maze.getMazeWidth() + "x" + maze.getMazeHeight());
            mazeButton.setUserData(maze);

            Region spacer1 = new Region();
            Region spacer2 = new Region();

            HBox.setHgrow(spacer1, Priority.ALWAYS);
            HBox.setHgrow(spacer2, Priority.ALWAYS);

            Button deleteButton = new Button("🗑");
            deleteButton.setPrefHeight(50);
            deleteButton.setPrefWidth(50);
            deleteButton.setVisible(false);
            deleteButton.setFocusTraversable(false);

            // uncomment to make it show only on select
            // deleteButton.visibleProperty().bind(mazeButton.selectedProperty());
            // uncomment for both
            // deleteButton.visibleProperty().bind(mazeButton.hoverProperty().or(mazeButton.selectedProperty()));
            // this makes it show only on hover
            deleteButton.visibleProperty().bind(mazeButton.hoverProperty());
            deleteButton.setOnAction(event -> {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to delete this maze?");
                alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
                alert.showAndWait();
                if (alert.getResult() == ButtonType.YES) {
                    Database.GetInstance().deleteMaze(maze.getMazeID()); // remove from database
                    content.getChildren().remove(mazeButton); // remove from ui

                    Alert success = new Alert(Alert.AlertType.INFORMATION);
                    success.setTitle("Maze Deleted");
                    success.setHeaderText(null);
                    success.setContentText("Your maze was deleted successfully.");
                    success.showAndWait();
                }

            });

            buttonContents.setAlignment(Pos.CENTER);
            buttonContents.setPadding(new Insets(25, 10, 25, 10));
            buttonContents.setSpacing(20);
            buttonContents.getChildren().addAll(name, spacer1, size, spacer2, date, deleteButton);

            mazeButton.setMinHeight(100);
            mazeButton.setMaxWidth(Double.MAX_VALUE);
            mazeButton.setGraphic(buttonContents);

            mazeButton.setToggleGroup(mazeOptions);

            content.getChildren().add(mazeButton);
        }

        this.mazesPane.setFitToWidth(true);
        this.mazesPane.setContent(content);
    }
}
