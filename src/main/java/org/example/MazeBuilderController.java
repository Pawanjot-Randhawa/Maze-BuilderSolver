   package org.example;

   import javafx.fxml.FXML;
   import javafx.scene.Node;
   import javafx.scene.control.Button;
   import javafx.scene.control.ChoiceBox;
   import javafx.scene.control.Spinner;
   import javafx.scene.control.SpinnerValueFactory;
   import javafx.scene.layout.ColumnConstraints;
   import javafx.scene.layout.GridPane;
   import javafx.scene.layout.Priority;
   import javafx.scene.layout.RowConstraints;
   import javafx.scene.paint.Color;
   import javafx.scene.shape.Rectangle;
   import javafx.scene.input.MouseEvent;

   public class MazeBuilderController {
       private final ViewFactory viewFactory;
       private int height;
       private int width;

       public MazeBuilderController(ViewFactory viewFactory) {
           this.viewFactory = viewFactory;
       }

       @FXML
       private Button temp_back;
       @FXML
       private Button solveButton;
       @FXML
       private Button generateGridButton;
       @FXML
       private ChoiceBox<Integer> widthInput;
       @FXML
       private ChoiceBox<Integer> heightInput;

       @FXML
       private GridPane grid;

       @FXML
       public void initialize() {
           temp_back.setOnAction(event -> {
               viewFactory.showMainMenuView();
           });
           generateGridButton.setOnAction(event -> {
               createMaze();
           });

           solveButton.setOnAction(event -> {
               printMazeArray(getMazeArray());
           });

           widthInput.getItems().addAll(5, 6, 7, 8, 9, 10);
           widthInput.setValue(5);
           heightInput.getItems().addAll(5, 6, 7, 8, 9, 10);
           heightInput.setValue(5);
           createMaze();
       }

       public void createMaze() {
           grid.getChildren().clear();
           height = heightInput.getValue();
           width = widthInput.getValue();

           for (int row = 0; row < height; row++) {
               for (int col = 0; col < width; col++) {
                   // Create a rectangle for each grid cell
                   Rectangle cell = new Rectangle();
                   cell.setFill(Color.LIGHTCORAL); // Set initial color
                   cell.setStroke(Color.BLACK);  // Add a border

                   // Dynamically bind the size of the rectangle
                   cell.widthProperty().bind(grid.widthProperty().divide(width)); // Width of each rectangle
                   cell.heightProperty().bind(grid.heightProperty().divide(height)); // Height of each rectangle

                   // Add a mouse click event to make the rectangle clickable
                   cell.setOnMouseClicked(event -> handleCellClick(event, cell));

                   // Add the rectangle to the GridPane at the correct position
                   grid.add(cell, col, row);

               }
           }
       }

       public void createMaze2(){
           grid.getChildren().clear();
           grid.getColumnConstraints().clear();
           grid.getRowConstraints().clear();

           height = heightInput.getValue();
           width = widthInput.getValue();

           for (int i = 0; i < width; i++) {
               ColumnConstraints column = new ColumnConstraints();
               column.setPercentWidth(100.0 / width); // Evenly space columns
               column.setHgrow(Priority.ALWAYS); // Allow the column to grow
               grid.getColumnConstraints().add(column);
           }


           for (int i = 0; i < height; i++) {
               RowConstraints row = new RowConstraints();
               row.setPercentHeight(100.0 / height); // Evenly space rows
               row.setVgrow(Priority.ALWAYS); // Allow the row to grow
               grid.getRowConstraints().add(row);
           }

           for (int row = 0; row < height; row++) {
               for (int col = 0; col < width; col++) {
                   Rectangle cell = new Rectangle();
                   cell.setFill(Color.LIGHTGRAY); // Default fill color
                   cell.setStroke(Color.BLACK);  // Grid border lines

                   // The rectangle will resize to fit its container automatically
                   cell.setWidth(grid.getWidth() / width);
                   cell.setHeight(grid.getHeight() / height);

                   // Add a click handler if needed
                   //cell.setOnMouseClicked(event -> handleCellClick(event, cell));

                   // Add the rectangle to the GridPane at the correct position
                   grid.add(cell, col, row);


               }
           }

       }

       private int[][] getMazeArray(){
           int[][] mazeArray = new int[height][width];

           for (Node cell : grid.getChildren()) {
               int col = GridPane.getColumnIndex(cell);
               int row = GridPane.getRowIndex(cell);
               cell = (Rectangle)cell;
               if(((Rectangle) cell).getFill().equals(Color.LIGHTCORAL)){
                   mazeArray[row][col] = 1;
               }
               else if(((Rectangle) cell).getFill().equals(Color.LIGHTGOLDENRODYELLOW)){
                   mazeArray[row][col] = 0;
               }
           }
           return mazeArray;
       }

       private void printMazeArray(int[][] mazeArray){
           for (int i = 0; i < mazeArray.length; i++) {
               for (int j = 0; j < mazeArray[0].length; j++) {
                   System.out.print(mazeArray[i][j] + " ");
               }
               System.out.println();
           }
       }

       private void handleCellClick(MouseEvent event, Rectangle cell) {
           // Toggle the color of the clicked rectangle
           if (cell.getFill().equals(Color.LIGHTGOLDENRODYELLOW)) {
               cell.setFill(Color.LIGHTCORAL);
           } else {
               cell.setFill(Color.LIGHTGOLDENRODYELLOW);
           }

       }

   }