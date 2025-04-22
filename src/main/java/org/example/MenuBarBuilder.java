package org.example;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

public class MenuBarBuilder {
    private ViewFactory viewFactory;

    public MenuBarBuilder() {
        //this.viewFactory = viewFactory;


    }

    public MenuBar build() {
        MenuBar menuBar = new MenuBar();

        Menu file = new Menu("File");
        MenuItem algorithm = new MenuItem("Load");
        MenuItem save = new MenuItem("Save");
        MenuItem exit = new MenuItem("Exit");

        file.getItems().addAll(algorithm, save, exit);

        Menu edit = new Menu("Edit");
        MenuItem Clear = new MenuItem("Clear");

        edit.getItems().addAll(Clear);

        Menu help = new Menu("Help");
        MenuItem about = new MenuItem("About");
        MenuItem howto = new MenuItem("How to Use");

        help.getItems().addAll(about, howto);

        menuBar.getMenus().addAll(file, edit, help);

        return menuBar;
    }


}
