#!/bin/bash
set -e

echo "Java path: $JAVA_HOME"

nativeDIR="lib/src"
headersDIR="lib/headers"
srcDIR="src/main/java/org/example"
buildDIR="build"

echo "Generating headers into $headersDIR"
javac -h "$headersDIR" $(find "$srcDIR/Native/" -name "*.java") "$srcDIR/Maze.java"

JAVA_INCLUDE_PATH="$JAVA_HOME/include"
JAVA_INCLUDE_LINUX_PATH="$JAVA_HOME/include/linux"

echo "Compiling C++ Code"
g++ -fPIC -I"$JAVA_INCLUDE_PATH" -I"$JAVA_INCLUDE_LINUX_PATH" "$nativeDIR/org_example_Native_MazeSolver.cpp" -c -o "$nativeDIR/Solver.o"
g++ -fPIC -I"$JAVA_INCLUDE_PATH" -I"$JAVA_INCLUDE_LINUX_PATH" "$nativeDIR/Logger.cpp" -c -o "$nativeDIR/Log.o"
g++ -fPIC -I"$JAVA_INCLUDE_PATH" -I"$JAVA_INCLUDE_LINUX_PATH" "$nativeDIR/Matrix.cpp" -c -o "$nativeDIR/Matrix.o"
g++ -fPIC -I"$JAVA_INCLUDE_PATH" -I"$JAVA_INCLUDE_LINUX_PATH" "$nativeDIR/AStar.cpp" -c -o "$nativeDIR/AStar.o"
g++ -fPIC -I"$JAVA_INCLUDE_PATH" -I"$JAVA_INCLUDE_LINUX_PATH" "$nativeDIR/Dijkstra.cpp" -c -o "$nativeDIR/Dijkstra.o"
g++ -fPIC -I"$JAVA_INCLUDE_PATH" -I"$JAVA_INCLUDE_LINUX_PATH" "$nativeDIR/BlockNdPath.cpp" -c -o "$nativeDIR/BlockNdPath.o"
g++ -fPIC -I"$JAVA_INCLUDE_PATH" -I"$JAVA_INCLUDE_LINUX_PATH" "$nativeDIR/BFS.cpp" -c -o "$nativeDIR/BFS.o"
g++ -fPIC -I"$JAVA_INCLUDE_PATH" -I"$JAVA_INCLUDE_LINUX_PATH" "$nativeDIR/IDAStar.cpp" -c -o "$nativeDIR/IDAStar.o"


echo "Linking C++ Code into libnative.so"
g++ -shared -o libnative.so build/*.o
