package org.example;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

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

        VBox mazeList = new VBox(10);
        mazeList.setPrefWidth(400);
        ToggleGroup group = new ToggleGroup();

        for (Maze maze : Database.GetInstance().getMazes()) {
            ToggleButton button = new ToggleButton();
            button.setToggleGroup(group);
            button.setUserData(maze);
            button.setMaxWidth(Double.MAX_VALUE);
            button.setMinHeight(80);

            HBox content = new HBox(20);
            content.setAlignment(Pos.CENTER_LEFT);
            content.setPadding(new Insets(10));

            Label name = new Label(maze.getName());
            Label date = new Label(maze.getDateMade());
            Label size = new Label(maze.getMazeWidth() + "x" + maze.getMazeHeight());

            content.getChildren().addAll(name, size, date);
            button.setGraphic(content);

            mazeList.getChildren().add(button);
        }

        ScrollPane scrollPane = new ScrollPane(mazeList);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(300);

        dialog.getDialogPane().setContent(scrollPane);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == openButtonType && group.getSelectedToggle() != null) {
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
