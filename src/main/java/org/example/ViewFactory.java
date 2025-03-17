package org.example;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ViewFactory {
    private final Stage stage;

    public ViewFactory(Stage stage) {
        this.stage = stage; // Primary stage is passed from your `start()` method
    }

    public void showMainMenuView() {
        try {
            // Set up FXMLLoader
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/mainmenu.fxml"));

            // Manually instantiate the controller, passing the ViewFactory dependency
            MainMenuController controller = new MainMenuController(this);

            // Set the controller
            loader.setController(controller);

            // Load the view
            Parent root = loader.load();

            // Display it
            stage.setScene(new Scene(root));
            stage.setTitle("Main Menu");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showMazeSolverView(Maze mazeArray) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/solvearea.fxml"));

            // Instantiate the controller and pass the Maze and ViewFactory as dependencies
            MazeSolverController controller = new MazeSolverController(this, mazeArray);

            loader.setController(controller);

            Parent root = loader.load();

            stage.setScene(new Scene(root));
            stage.setTitle("Maze Solver");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void showMazeBuilderView(Maze mazeArray) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/paintarea.fxml"));

            // Instantiate the controller and pass the Maze and ViewFactory as dependencies
            MazeBuilderController controller = new MazeBuilderController(this, mazeArray);

            loader.setController(controller);

            Parent root = loader.load();

            stage.setScene(new Scene(root));
            stage.setTitle("Maze Builder");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void loadView(String fxmlFile, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();

            // If necessary, pass data to controller using loader.getController()

            stage.setScene(new Scene(root));
            stage.setTitle(title);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle errors in loading the FXML
        }
    }
}