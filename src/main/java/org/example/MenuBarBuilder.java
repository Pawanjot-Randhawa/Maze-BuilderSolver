package org.example;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

public class MenuBarBuilder {
    private ViewFactory viewFactory;
    private MenuBar menuBar;
    private Menu file;

    public MenuBarBuilder(ViewFactory viewFactory) {
        this.viewFactory = viewFactory;
        this.menuBar = new MenuBar();
        this.file = new Menu("File");

    }

    //default items both bars will have
    public void buildDefaultItems(){
        MenuItem algorithm = new MenuItem("Load");
        MenuItem save = new MenuItem("Save");
        MenuItem exit = new MenuItem("Exit");
        exit.setOnAction(e -> {
            viewFactory.showMainMenuView();
        });

        file.getItems().addAll(algorithm, save, exit);

    }

    public MenuBar buildForBuilder(MazeBuilderController controller) {
        buildDefaultItems();

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
        buildDefaultItems();

        menuBar.getMenus().addAll(file);
        return menuBar;

    }


}
