package org.example.Native;

import java.util.List;

public class MazeSolver {
  final static public String LIBRARY_NAME = "native";
  static {
    System.loadLibrary(LIBRARY_NAME);
  }

  public static native void sayHello();
  public static native String returnHello();

  // oh brother
  public static native boolean InitializeMaze(int[][] maze);
  public static native List<int[]> AStar();

  public static native List<int[]> SolveMaze(String strategy);
}
