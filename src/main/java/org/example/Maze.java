package org.example;

public class Maze {
    private int[][] mazeArray;
    private int mazeWidth;
    private int mazeHeight;
    private int mazeID;
    private String name;
    private String dateMade;

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

    public int getMazeID() {
        return mazeID;
    }

    public void setMazeID(int mazeID) { this.mazeID = mazeID; }

    public String getName() {
        return name;
    }

    public void setName(String name) { this.name = name; }

    public String getDateMade() {
        return dateMade;
    }

    public void setDateMade(String dateMade) {this.dateMade = dateMade; }

    public int[][] getMazeArray() {
        return mazeArray;
    }

    public void setMazeArray(int[][] mazeArray) {
        this.mazeArray = mazeArray;
    }

    public void printMazeArray(){
        for (int i = 0; i < mazeArray.length; i++) {
            for (int j = 0; j < mazeArray[0].length; j++) {
                System.out.print(mazeArray[i][j] + " ");
            }
            System.out.println();
        }
    }
}
