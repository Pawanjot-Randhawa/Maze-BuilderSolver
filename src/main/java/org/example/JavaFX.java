package org.example;

import java.io.IOException;

import org.example.Models.Maze;

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
        Maze maze = new Maze();
        System.out.println(maze.sayHello());

        launch();
    }

}