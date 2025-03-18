package org.example.Models;

public class Maze {

    public native String sayHello();
    static {
        System.loadLibrary("Maze");
    }

    private int[][] mazeArray;
    private int mazeWidth;
    private int mazeHeight;

    public Maze()
    {

    }

    public Maze(int[][] mazeArray, int mazeWidth, int mazeHeight) {
        this.mazeArray = mazeArray;
        this.mazeWidth = mazeWidth;
        this.mazeHeight = mazeHeight;
    }

    public int getMazeHeight() {
        return mazeHeight;
    }

    public void setMazeHeight(int mazeHeight) {
        this.mazeHeight = mazeHeight;
    }

    public int getMazeWidth() {
        return mazeWidth;
    }

    public void setMazeWidth(int mazeWidth) {
        this.mazeWidth = mazeWidth;
    }

    public int[][] getMazeArray() {
        return mazeArray;
    }
}
