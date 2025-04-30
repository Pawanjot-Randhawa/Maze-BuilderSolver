package org.example;

import java.io.IOException;
import java.util.List;

import javafx.application.Application;
import javafx.stage.Stage;

public class JavaFX extends Application {

    public static List<Maze> Mazes;

    @Override
    public void start(Stage stage) throws IOException {
        ViewFactory viewFactory = new ViewFactory(stage);
        viewFactory.showMainMenuView();
    }

    public static void main(String[] args) {

        Database db = Database.GetInstance();

        db.createMazeTable();

        //testing the mazes
        List<Integer> mazes = db.getMazes();
        for (var maze : mazes) {
            System.out.println(maze);
        }

        Mazes = db.getAllMazes();
        for (var zeMaze : Mazes) {
            System.out.println(zeMaze.getMazeID() + ": " + zeMaze.getName());
        }
        
        launch();
    }

}

