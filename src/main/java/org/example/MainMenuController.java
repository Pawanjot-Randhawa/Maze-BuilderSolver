   package org.example;

   import javafx.fxml.FXML;
   import javafx.scene.control.Button;

   public class MainMenuController {
       private final ViewFactory viewFactory;

       public MainMenuController(ViewFactory viewFactory) {
           this.viewFactory = viewFactory; // Pass this into the controller
       }

       @FXML
       private Button new_maze_btn;

       @FXML
       public void initialize() {
           new_maze_btn.setOnAction(event -> {
               viewFactory.showMazeBuilderView(null);
           });
       }
   }