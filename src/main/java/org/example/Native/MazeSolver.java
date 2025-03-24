package org.example.Native;

public class MazeSolver {
  static {
    System.loadLibrary("native");
  }

  public static native void sayHello();
  public static native String returnHello();
}
