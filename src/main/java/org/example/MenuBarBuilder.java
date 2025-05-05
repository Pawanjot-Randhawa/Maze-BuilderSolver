package org.example;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.Optional;

public class MenuBarBuilder {
    private ViewFactory viewFactory;
    private MenuBar menuBar;
    private Menu file;
    private MenuItem save;
    private MenuItem open;
    private MenuItem exit;

    public MenuBarBuilder(ViewFactory viewFactory) {
        this.viewFactory = viewFactory;
        this.menuBar = new MenuBar();
        this.file = new Menu("File");
        this.open = new MenuItem("Open");
        open.setOnAction(event -> {
            showOpenMazeDialog();
        });
        this.save = new MenuItem("Save");
        this.exit = new MenuItem("Exit");
        exit.setOnAction(e -> {
            viewFactory.showMainMenuView();
        });

        file.getItems().addAll(open, save, exit);


    }

    public MenuBar buildForBuilder(MazeBuilderController controller) {
        save.setOnAction(e -> {
            handleSaveOrUpdate(controller);
        });

        Menu edit = new Menu("Edit");
        MenuItem Clear = new MenuItem("Clear");
        Clear.setOnAction(e -> {
            controller.createMaze();
        });

        edit.getItems().addAll(Clear);

        Menu help = new Menu("Help");
        MenuItem about = new MenuItem("About");
        MenuItem howto = new MenuItem("How to Use");

        help.getItems().addAll(about, howto);

        menuBar.getMenus().addAll(file, edit, help);

        return menuBar;
    }

    public MenuBar buildForSolver() {
        save.setDisable(true);
        Menu algos = new Menu("Algorithms");
        MenuItem a = new MenuItem("A*");
        MenuItem b = new MenuItem("Dijkstra");
        MenuItem c = new MenuItem("BlockNPath");
        MenuItem d = new MenuItem("BFS");
        MenuItem e = new MenuItem("IDAStar");

        algos.getItems().addAll(a, b, c, d, e);

        for(MenuItem algo : algos.getItems()){
            algo.setOnAction(event -> {
                Alert info = new Alert(Alert.AlertType.INFORMATION);
                info.setTitle(algo.getText());
                info.setHeaderText(null);

                switch(algo.getText()){
                    case "A*":
                        info.setContentText("Uses a heuristic (like distance to goal) to guide search; efficient and finds shortest path if the heuristic is admissible.");
                        break;
                    case "Dijkstra":
                        info.setContentText("Expands outward based on lowest total cost; finds shortest path in weighted graphs without a heuristic.");
                        break;
                    case "BlockNPath":
                        info.setContentText("Recursively explores paths in a predefined order; Upon reaching a dead end, the algorithm marks it as blocked and restarts, gradually refining the solution.");
                        break;
                    case "BFS":
                        info.setContentText("Explores the maze layer by layer, ensuring the shortest path is found in unweighted graphs.");
                        break;
                    case "IDAStar":
                        info.setContentText("Combines A* with depth-first search, using cost limits to find the shortest path efficiently with low memory use.");
                        break;
                    default:
                        info.setContentText("Error, this should not happen");

                }
                info.showAndWait();
            });
        }

        menuBar.getMenus().addAll(file, algos);
        return menuBar;

    }

    private void handleSaveOrUpdate(MazeBuilderController controller){
        if(controller.getMaze().getMazeID() == 0){//id of 0 means the maze is not from the database and is new
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Save New Maze");
            dialog.setHeaderText(null);
            dialog.setContentText("Enter Maze Name:");
            Optional<String> result = dialog.showAndWait();
            if (result.isPresent()) {
                if(Database.GetInstance().saveMaze(result.get(), controller.getMaze().getMazeArray())){
                    //show confirmation
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Maze Saved");
                    alert.setHeaderText(null);
                    alert.setContentText("Your maze was saved successfully!");
                    alert.showAndWait();
                }else{
                    //show duplicate name error
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("A maze with this name already exists!");
                    alert.showAndWait();
                }
            }
        }else{//maze is from the database
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Update Maze");
            confirmation.setHeaderText(null);
            confirmation.setContentText("This Maze alrady exists under the name " + controller.getMaze().getName() + "\nDo you want to update it?");
            confirmation.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
            Optional<ButtonType> result = confirmation.showAndWait();
            if(result.get() == ButtonType.YES){
                Database.GetInstance().updateMaze(controller.getMaze());
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Maze Updated");
                alert.setHeaderText(null);
                alert.setContentText("Your maze was updated successfully!");
                alert.showAndWait();
            }
        }
    }

    private void showOpenMazeDialog() {
        Dialog<Maze> dialog = new Dialog<>();
        dialog.setTitle("Open Maze");
        dialog.setHeaderText("Select a maze to open");

        ButtonType openButtonType = new ButtonType("Open", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(openButtonType, ButtonType.CANCEL);

        // === Maze List ===
        VBox mazeList = new VBox(10);
        mazeList.setPrefWidth(250);
        mazeList.setPadding(new Insets(10));
        ToggleGroup group = new ToggleGroup();

        // === Preview Pane ===
        VBox previewPane = new VBox(10);
        previewPane.setPadding(new Insets(10));
        previewPane.setPrefWidth(250);
        previewPane.setStyle("-fx-background-color: #f4f4f4;");

        Label namePreview = new Label("Name: ");
        Label sizePreview = new Label("Size: ");
        Label datePreview = new Label("Created: ");
        GridPane miniMazeGrid = new GridPane();
        miniMazeGrid.setHgap(1);
        miniMazeGrid.setVgap(1);
        miniMazeGrid.setPadding(new Insets(10));
        miniMazeGrid.setStyle("-fx-background-color: #ffffff;");

        previewPane.getChildren().addAll(namePreview, sizePreview, datePreview, miniMazeGrid);

        for (Maze maze : Database.GetInstance().getMazes()) {
            ToggleButton button = new ToggleButton();
            button.setToggleGroup(group);
            button.setUserData(maze);
            button.setMaxWidth(Double.MAX_VALUE);
            button.setMinHeight(60);

            HBox content = new HBox(15);
            content.setAlignment(Pos.CENTER_LEFT);
            content.setPadding(new Insets(5));

            Label name = new Label(maze.getName());

            content.getChildren().addAll(name);
            button.setGraphic(content);
            mazeList.getChildren().add(button);

            button.setOnAction(e -> {
                Maze selected = (Maze) button.getUserData();
                namePreview.setText("Name: " + selected.getName());
                sizePreview.setText("Size: " + selected.getMazeWidth() + "x" + selected.getMazeHeight());
                datePreview.setText("Created: " + selected.getDateMade());

                // === Render mini maze ===
                miniMazeGrid.getChildren().clear();

                int[][] grid = selected.getMazeArray();
                int rows = grid.length;
                int cols = grid[0].length;
                int cellSize = Math.min(200 / Math.max(rows, cols), 20);

                for (int y = 0; y < rows; y++) {
                    for (int x = 0; x < cols; x++) {
                        Rectangle cell = new Rectangle(cellSize, cellSize);
                        int value = grid[y][x];

                        if (value == 8) {
                            cell.setFill(Color.BLACK); // Wall
                        } else if (value == 0) {
                            cell.setFill(Color.WHITE); // Path
                        }else if (value == 2) {
                            cell.setFill(Color.LIMEGREEN); // Start
                        }else if (value == 1) {

                            cell.setFill(Color.RED); // End
                        }
                        cell.setStroke(Color.LIGHTGRAY);
                        miniMazeGrid.add(cell, x, y);
                    }
                }
            });
        }

        ScrollPane scrollPane = new ScrollPane(mazeList);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(300);

        BorderPane dialogLayout = new BorderPane();
        dialogLayout.setLeft(scrollPane);
        dialogLayout.setRight(previewPane);
        dialog.getDialogPane().setContent(dialogLayout);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == openButtonType) {
                if (group.getSelectedToggle() == null) {
                    Alert warning = new Alert(Alert.AlertType.WARNING);
                    warning.setTitle("No Maze Selected");
                    warning.setHeaderText(null);
                    warning.setContentText("Please select a maze to open.");
                    warning.showAndWait();
                    return null;
                }
                return (Maze) group.getSelectedToggle().getUserData();
            }
            return null;
        });

        Optional<Maze> result = dialog.showAndWait();
        result.ifPresent(selectedMaze -> {
            viewFactory.showMazeBuilderView(selectedMaze);
        });
    }


}
