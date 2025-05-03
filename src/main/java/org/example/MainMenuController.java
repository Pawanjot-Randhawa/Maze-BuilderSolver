   package org.example;

   import javafx.fxml.FXML;
   import javafx.geometry.Insets;
   import javafx.geometry.Pos;
   import javafx.scene.control.*;
   import javafx.scene.layout.HBox;
   import javafx.scene.layout.Priority;
   import javafx.scene.layout.Region;
   import javafx.scene.layout.VBox;

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
       private ScrollPane mazesPane;

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
           populateMazeScrollPane();

       }

       private void populateMazeScrollPane() {
           VBox content = new VBox();
           content.setFillWidth(true);

           ToggleGroup mazeOptions = new ToggleGroup();
           System.out.println(Database.GetInstance().getMazes().size() + " Mazes loaded");

           for (Maze maze: Database.GetInstance().getMazes()){

               ToggleButton mazeButton = new ToggleButton();
               HBox buttonContents = new HBox();

               Label name = new Label(maze.getName());
               Label date = new Label(maze.getDateMade());
               Label size = new Label(maze.getMazeWidth() + "x" + maze.getMazeHeight());

               Region spacer1 = new Region();
               Region spacer2 = new Region();

               HBox.setHgrow(spacer1, Priority.ALWAYS);
               HBox.setHgrow(spacer2, Priority.ALWAYS);

               Button deleteButton = new Button("🗑");
               deleteButton.setPrefHeight(50);
               deleteButton.setPrefWidth(50);
               deleteButton.setVisible(false);
               deleteButton.setFocusTraversable(false);

               //uncomment to make it show only on select
               //deleteButton.visibleProperty().bind(mazeButton.selectedProperty());

               //uncomment for both
               //deleteButton.visibleProperty().bind(mazeButton.hoverProperty().or(mazeButton.selectedProperty()));

               //this makes it show only on hover
               deleteButton.visibleProperty().bind(mazeButton.hoverProperty());
               deleteButton.setOnAction(event -> {
                   Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                   alert.setTitle("Confirmation");
                   alert.setHeaderText(null);
                   alert.setContentText("Are you sure you want to delete this maze?");
                   alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
                   alert.showAndWait();
                   if (alert.getResult() == ButtonType.YES) {
                       //add deletion logic for database

                       content.getChildren().remove(mazeButton);
                       //
                       Alert success = new Alert(Alert.AlertType.INFORMATION);
                       success.setTitle("Maze Deleted");
                       success.setHeaderText(null);
                       success.setContentText("Your maze was deleted successfully.");
                       success.showAndWait();
                   }

               });

               buttonContents.setAlignment(Pos.CENTER);
               buttonContents.setPadding(new Insets(25,10,25,10));
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