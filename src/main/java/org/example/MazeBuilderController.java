   package org.example;

   import javafx.fxml.FXML;
   import javafx.scene.Node;
   import javafx.scene.control.*;
   import javafx.scene.layout.ColumnConstraints;
   import javafx.scene.layout.GridPane;
   import javafx.scene.layout.Priority;
   import javafx.scene.layout.RowConstraints;
   import javafx.scene.paint.Color;
   import javafx.scene.shape.Rectangle;
   import javafx.scene.input.MouseEvent;
   import javafx.geometry.Insets;

   public class MazeBuilderController {
       private final ViewFactory viewFactory;
       private int height;
       private int width;
       private int startPoint;
       private int endPoint;
       private ToggleGroup tools;

       //setting colors
       Color start = Color.LIGHTGREEN;
       Color end = Color.DARKRED;
       Color path = Color.LIGHTGOLDENRODYELLOW;
       Color wall = Color.LIGHTCORAL;

       public MazeBuilderController(ViewFactory viewFactory) {
           this.viewFactory = viewFactory;
       }

       @FXML
       private Button temp_back;
       @FXML
       private ToggleButton startToggle;
       @FXML
       private ToggleButton endToggle;
       @FXML
       private Label startNum;
       @FXML
       private Label endNum;
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
           tools = new ToggleGroup();

           startToggle.setToggleGroup(tools);
           endToggle.setToggleGroup(tools);

           startPoint = 0;
           startNum.setTextFill(Color.RED);
           startNum.setText(String.valueOf(startPoint));

           endPoint = 0;
           endNum.setTextFill(Color.RED);
           endNum.setText(String.valueOf(endPoint));


           temp_back.setOnAction(event -> {
               viewFactory.showMazeSolverView();
           });
           generateGridButton.setOnAction(event -> {
               createMaze();
           });

           widthInput.getItems().addAll(4,5, 6, 10, 12);
           widthInput.setValue(5);
           heightInput.getItems().addAll(4,5, 6, 10, 12);
           heightInput.setValue(5);
           createMaze();
       }

       public void createMaze() {
           grid.getChildren().clear();

           height = heightInput.getValue();
           width = widthInput.getValue();

           double t1 = grid.getPrefWidth() / width;
           double t2 = grid.getPrefHeight() / height;

          /* grid.getRowConstraints().clear();
           grid.getColumnConstraints().clear();


           for (int i = 0; i < height; i++) {
               grid.getRowConstraints().add(new RowConstraints(t2));
           }

           for (int j = 0; j < width; j++) {
               grid.getColumnConstraints().add(new ColumnConstraints(t1));
           }
*/
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

       private void handleCellClick(MouseEvent event, Rectangle cell) {
           // Toggle the color of the clicked rectangle

           Toggle selectedToggle = tools.getSelectedToggle(); // Get the selected toggle in the group

           if (selectedToggle == startToggle) { //start toggle
               if (startPoint<1 && !cell.getFill().equals(start)) { // no start points means we can set one
                   cell.setFill(start);
                   startPoint = 1;
                   startNum.setTextFill(Color.GREEN);
                   startNum.setText(String.valueOf(startPoint));
               } else {
                   if(cell.getFill().equals(start)){ //delete start
                       cell.setFill(path);
                       startPoint = 0;
                       startNum.setTextFill(Color.RED);
                       startNum.setText(String.valueOf(startPoint));

                   }else{ //default behaviour if cell is not a start
                       cell.setFill(path);
                   }
               }
           } else if (selectedToggle == endToggle) { // end toggle
               if (!cell.getFill().equals(end)) { //no limit on end points so we cant just start making them
                   cell.setFill(end);
                   endPoint += 1;
                   endNum.setTextFill(Color.GREEN);
                   endNum.setText(String.valueOf(endPoint));
               } else {
                   if(cell.getFill().equals(end)){ // delete end point
                       cell.setFill(path);
                       endPoint -= 1;
                       endNum.setTextFill(Color.RED);
                       endNum.setText(String.valueOf(endPoint));
                   }else{ //default behaviour if cell is not a end point
                       cell.setFill(path);
                   }
               }
           } else {     //default behaviour
               if (cell.getFill().equals(path)) {
                   cell.setFill(wall);
               } else {
                   cell.setFill(path);
               }
           }



       }

   }