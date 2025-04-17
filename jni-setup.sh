#!/bin/bash

# Exit if any command fails
set -e

echo "Java path: $JAVA_HOME"

nativeDIR="lib/src"
headersDIR="lib/headers"
srcDIR="src/main/java/org/example"

echo "Generating headers into $headersDIR"
javac -h "$headersDIR" "$srcDIR/native/"**.java "$srcDIR/Maze.java"

# Java Include Directories
JAVA_INCLUDE_PATH="$JAVA_HOME/include"
JAVA_INCLUDE_LINUX_PATH="$JAVA_HOME/include/linux"

echo "Compiling C++ Code"
g++ -c -I"$JAVA_INCLUDE_PATH" -I"$JAVA_INCLUDE_LINUX_PATH" "$nativeDIR/org_example_Native_MazeSolver.cpp" -o Solver.o
g++ -c -I"$JAVA_INCLUDE_PATH" -I"$JAVA_INCLUDE_LINUX_PATH" "$nativeDIR/Logger.cpp" -o Log.o
g++ -c -I"$JAVA_INCLUDE_PATH" -I"$JAVA_INCLUDE_LINUX_PATH" "$nativeDIR/Matrix.cpp" -o Matrix.o

echo "Linking C++ Code"
g++ -shared -o libnative.so Solver.o Log.o Matrix.o -Wl,--add-stdcall-alias
