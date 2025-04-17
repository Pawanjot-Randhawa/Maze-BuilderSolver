package org.example.Native;

import java.util.List;

public class MazeSolver {
  static {
    System.loadLibrary("native");
  }

  public static native void sayHello();
  public static native String returnHello();

  // oh brother
  public static native boolean InitializeMaze(int[][] maze);
  public static native List<int[]> AStar();

  public static native List<int[]> SolveMaze(String strategy);
}
