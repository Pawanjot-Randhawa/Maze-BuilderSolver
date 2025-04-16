   package org.example;

   import javafx.fxml.FXML;
   import javafx.scene.control.Alert;
   import javafx.scene.control.Button;

   public class MainMenuController {
       private final ViewFactory viewFactory;

       public MainMenuController(ViewFactory viewFactory) {
           this.viewFactory = viewFactory; // Pass this into the controller
       }

       @FXML
       private Button newMazeBtn;
       @FXML
       private Button openMazeBtn;

       @FXML
       public void initialize() {
           newMazeBtn.setOnAction(event -> {
               viewFactory.showMazeBuilderView(null);
           });
           openMazeBtn.setOnAction(event -> {
               Alert alert = new Alert(Alert.AlertType.INFORMATION);
               alert.setTitle("Access Denied");
               alert.setHeaderText(null);
               alert.setContentText("Feature Is In Development.");
               alert.showAndWait();

           });
       }
   }