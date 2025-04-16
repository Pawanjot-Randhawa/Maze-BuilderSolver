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
       private boolean isNewMaze;
       private Maze maze;
       private int height;
       private int width;
       private int startPoint;
       private int endPoint;
       private ToggleGroup tools;
       private Rectangle lastStart;

       //setting colors
       Color start = Color.LIGHTGREEN;
       Color end = Color.DARKRED;
       Color path = Color.LIGHTGOLDENRODYELLOW;
       Color wall = Color.LIGHTCORAL;

       public MazeBuilderController(ViewFactory viewFactory, Maze maze) {
           this.viewFactory = viewFactory;
           isNewMaze = maze == null;//if maze is null then its false
           if(!isNewMaze){
               this.maze = maze;
           }
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

           temp_back.setOnAction(event -> {
               if(startPoint == 1 && endPoint >= 1){
                   viewFactory.showMazeSolverView(getMazeArray());
               }else{
                   Alert alert = new Alert(Alert.AlertType.ERROR);
                   alert.setTitle("Incompatible Maze");
                   alert.setHeaderText(null);
                   alert.setContentText("The Maze needs at least one starting point and at least one ending point in order to be solvable");
                   alert.showAndWait();
               }
           });
           generateGridButton.setOnAction(event -> {
               createMaze();
           });

           widthInput.getItems().addAll(5, 6);
           widthInput.setValue(5);
           heightInput.getItems().addAll(5, 6);
           heightInput.setValue(5);
           if(isNewMaze){
               createMaze();
           }else{
               copyMazeArray();
           }
       }

       public void createMaze() {
           grid.getChildren().clear();

           reset();

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

       public void copyMazeArray(){
           grid.getChildren().clear();
           this.height = maze.getMazeHeight();
           this.width = maze.getMazeWidth();

           widthInput.setValue(maze.getMazeWidth());
           heightInput.setValue(maze.getMazeHeight());

           reset();

           int[][] mazeArray = maze.getMazeArray();

           for (int row = 0; row < maze.getMazeHeight(); row++) {
               for (int col = 0; col < maze.getMazeWidth(); col++) {
                   // Create a rectangle for each grid cell
                   Rectangle cell = new Rectangle();

                   if(mazeArray[row][col]==1){
                       cell.setFill(wall);
                   }else if(mazeArray[row][col]==0){
                       cell.setFill(path);
                   }else if (mazeArray[row][col]==8) {
                       cell.setFill(start);
                       this.startPoint = 1;
                       this.lastStart = cell;
                   }else if (mazeArray[row][col]==3) {
                       cell.setFill(end);
                       this.endPoint += 1;
                   }
                   cell.setStroke(Color.BLACK);  // Add a border
                   // Dynamically bind the size of the rectangle
                   cell.widthProperty().bind(grid.widthProperty().divide(maze.getMazeWidth())); // Width of each rectangle
                   cell.heightProperty().bind(grid.heightProperty().divide(maze.getMazeHeight())); // Height of each rectangle
                   // Add a mouse click event to make the rectangle clickable
                   cell.setOnMouseClicked(event -> handleCellClick(event, cell));
                   // Add the rectangle to the GridPane at the correct position
                   grid.add(cell, col, row);
                   update();
               }
           }
       }

       public void reset(){
           startPoint = 0;
           startNum.setTextFill(Color.RED);
           startNum.setText(String.valueOf(startPoint));

           endPoint = 0;
           endNum.setTextFill(Color.RED);
           endNum.setText(String.valueOf(endPoint));
       }

       public void update(){
           if(startPoint==1){
               startNum.setTextFill(Color.GREEN);
               startNum.setText(String.valueOf(startPoint));
           } else if (startPoint==0) {
               startNum.setTextFill(Color.RED);
               startNum.setText(String.valueOf(startPoint));
           }
           if(endPoint>0){
               endNum.setTextFill(Color.GREEN);
               endNum.setText(String.valueOf(endPoint));
           } else if (endPoint==0) {
               endNum.setTextFill(Color.RED);
               endNum.setText(String.valueOf(endPoint));
           }
       }

       private Maze getMazeArray(){
           int[][] mazeArray = new int[height][width];

           for (Node cell : grid.getChildren()) {
               int col = GridPane.getColumnIndex(cell);
               int row = GridPane.getRowIndex(cell);
               cell = (Rectangle)cell;
               if(((Rectangle) cell).getFill().equals(wall)){
                   mazeArray[row][col] = 1;
               }else if(((Rectangle) cell).getFill().equals(path)){
                   mazeArray[row][col] = 0;
               }else if (((Rectangle) cell).getFill().equals(start)) {
                   mazeArray[row][col] = 8;
               }else if (((Rectangle) cell).getFill().equals(end)) {
                   mazeArray[row][col] = 3;
               }
           }
           return new Maze(mazeArray, width, height);
       }

       private void handleCellClick(MouseEvent event, Rectangle cell) {
           // Toggle the color of the clicked rectangle

           Toggle selectedToggle = tools.getSelectedToggle(); // Get the selected toggle in the group

           if (selectedToggle == startToggle) { //start toggle; no matter where we click we want this to make a start, we can also only have one start
               if (startPoint<1 && cell.getFill().equals(end)) { // no start points and we click on an end, make it a start point
                   cell.setFill(start);
                   startPoint = 1;
                   lastStart = cell;
                   endPoint -= 1;
               }else if(startPoint<1){ // no start point and we click on path or wall, make it a start
                   cell.setFill(start);
                   startPoint = 1;
                   lastStart = cell;
               }else { // branch if a start point already exists
                   if(cell.getFill().equals(start)){ //delete start if we click on start
                       cell.setFill(path);
                       startPoint = 0;
                   }else if(cell.getFill().equals(end)){//if start toggle is on and you hit an end point, replace the end point with start and remove last start
                       cell.setFill(start);
                       lastStart.setFill(path);
                       lastStart = cell;
                       endPoint -= 1;
                   }else{ //if cell is not start or end, switch the last start to path and move new start here
                       cell.setFill(start);
                       lastStart.setFill(path);
                       lastStart = cell;
                   }
               }
           } else if (selectedToggle == endToggle) { // end toggle; we want to make end points where we click, we can have as many end points as we want
               if(cell.getFill().equals(end)){ // delete end point if we click on end point
                   cell.setFill(path);
                   endPoint -= 1;
               }else if(cell.getFill().equals(start)){//if end toggle is on and you hit a start, replace the start with an end point
                   cell.setFill(end);
                   startPoint = 0;
                   endPoint += 1;
               }else{ //default behaviour if cell is not a end point
                   cell.setFill(end);
                   endPoint += 1;
               }
           } else {     //default behaviour
               if (cell.getFill().equals(path)) { // if we click on path, set it to wall
                   cell.setFill(wall);
               }else if(cell.getFill().equals(start)){ // if we click on start, set it to path
                   cell.setFill(path);
                   startPoint = 0;
               }else if(cell.getFill().equals(end)){ //if we click on end, set it to path
                   cell.setFill(path);
                   endPoint -= 1;
               }else { // if we click on a wall, we set it to path
                   cell.setFill(path);
               }
           }
           update();
       }
   }