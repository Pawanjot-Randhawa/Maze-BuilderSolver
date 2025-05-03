package org.example;

import java.io.IOException;
import java.util.List;

import javafx.application.Application;
import javafx.stage.Stage;

public class JavaFX extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        ViewFactory viewFactory = new ViewFactory(stage);
        viewFactory.showMainMenuView();
    }

    public static void main(String[] args) {

        Database db = Database.GetInstance();

        db.createMazeTable();

        //testing the maze database

        for (var zeMaze : db.getMazes()) {
            System.out.println(zeMaze.getMazeID() + ": " + zeMaze.getName());
        }
        
        launch();
    }

}

