   package org.example;

   import javafx.fxml.FXML;
   import javafx.geometry.Insets;
   import javafx.geometry.Pos;
   import javafx.scene.control.*;
   import javafx.scene.layout.HBox;
   import javafx.scene.layout.Priority;
   import javafx.scene.layout.Region;
   import javafx.scene.layout.VBox;

   import static org.example.JavaFX.Mazes;

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
           System.out.println(Mazes.size() + " Mazes loaded");

           for (Maze maze: Mazes){
               ToggleButton button = new ToggleButton(maze.getName());
               button.setMinHeight(100);
               button.setMaxWidth(Double.MAX_VALUE);

               ToggleButton mazeButton = new ToggleButton();
               HBox buttonContents = new HBox();

               Label name = new Label(maze.getName());
               Label date = new Label(maze.getDateMade());
               Label size = new Label(maze.getMazeWidth() + "x" + maze.getMazeHeight());

               Region spacer1 = new Region();
               Region spacer2 = new Region();

               HBox.setHgrow(spacer1, Priority.ALWAYS);
               HBox.setHgrow(spacer2, Priority.ALWAYS);

               buttonContents.setAlignment(Pos.CENTER);
               buttonContents.setPadding(new Insets(30,10,30,10));
               buttonContents.getChildren().addAll(name, spacer1, size, spacer2, date);

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