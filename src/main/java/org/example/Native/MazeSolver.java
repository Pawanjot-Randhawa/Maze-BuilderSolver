package org.example.Native;

public class MazeSolver {
  static {
    System.loadLibrary("native");
  }

  public static native void sayHello();
  public static native String returnHello();

  // oh brother
  public static native int[][] solveMaze(int[][] maze);
}
