#!/bin/bash
set -e

echo "Java path: $JAVA_HOME"

nativeDIR="lib/src"
headersDIR="lib/headers"
srcDIR="src/main/java/org/example"

echo "Generating headers into $headersDIR"
javac -h "$headersDIR" $(find "$srcDIR/Native/" -name "*.java") "$srcDIR/Maze.java"

JAVA_INCLUDE_PATH="$JAVA_HOME/include"
JAVA_INCLUDE_LINUX_PATH="$JAVA_HOME/include/linux"

echo "Compiling C++ Code"
g++ -fPIC -I"$JAVA_INCLUDE_PATH" -I"$JAVA_INCLUDE_LINUX_PATH" "$nativeDIR/org_example_Native_MazeSolver.cpp" -c -o Solver.o
g++ -fPIC -I"$JAVA_INCLUDE_PATH" -I"$JAVA_INCLUDE_LINUX_PATH" "$nativeDIR/Logger.cpp" -c -o Log.o
g++ -fPIC -I"$JAVA_INCLUDE_PATH" -I"$JAVA_INCLUDE_LINUX_PATH" "$nativeDIR/Matrix.cpp" -c -o Matrix.o
g++ -fPIC -I"$JAVA_INCLUDE_PATH" -I"$JAVA_INCLUDE_WIN32_PATH" "$nativeDIR/AStar.cpp" -c -o AStar.o
g++ -fPIC -I"$JAVA_INCLUDE_PATH" -I"$JAVA_INCLUDE_WIN32_PATH" "$nativeDIR/Dijkstra.cpp" -c -o Dijkstra.o
g++ -fPIC -I"$JAVA_INCLUDE_PATH" -I"$JAVA_INCLUDE_WIN32_PATH" "$nativeDIR/BlockNdPath.cpp" -c -o BlockNdPath.o

echo "Linking C++ Code into libnative.so"
g++ -shared -o libnative.so Solver.o Log.o Matrix.o AStar.o Dijkstra.o BlockNdPath.o
