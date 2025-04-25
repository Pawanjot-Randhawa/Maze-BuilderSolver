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
g++ -fPIC -I"$JAVA_INCLUDE_PATH" -I"$JAVA_INCLUDE_LINUX_PATH" "$nativeDIR/org_example_Native_MazeSolver.cpp" -c -o "$buildDIR/Solver.o"
g++ -fPIC -I"$JAVA_INCLUDE_PATH" -I"$JAVA_INCLUDE_LINUX_PATH" "$nativeDIR/Logger.cpp" -c -o "$buildDIR/Log.o"
g++ -fPIC -I"$JAVA_INCLUDE_PATH" -I"$JAVA_INCLUDE_LINUX_PATH" "$nativeDIR/Matrix.cpp" -c -o "$buildDIR/Matrix.o"
g++ -fPIC -I"$JAVA_INCLUDE_PATH" -I"$JAVA_INCLUDE_LINUX_PATH" "$nativeDIR/AStar.cpp" -c -o "$buildDIR/AStar.o"
g++ -fPIC -I"$JAVA_INCLUDE_PATH" -I"$JAVA_INCLUDE_LINUX_PATH" "$nativeDIR/Dijkstra.cpp" -c -o "$buildDIR/Dijkstra.o"
g++ -fPIC -I"$JAVA_INCLUDE_PATH" -I"$JAVA_INCLUDE_LINUX_PATH" "$nativeDIR/BlockNdPath.cpp" -c -o "$buildDIR/BlockNdPath.o"
g++ -fPIC -I"$JAVA_INCLUDE_PATH" -I"$JAVA_INCLUDE_LINUX_PATH" "$nativeDIR/BFS.cpp" -c -o "$buildDIR/BFS.o"
g++ -fPIC -I"$JAVA_INCLUDE_PATH" -I"$JAVA_INCLUDE_LINUX_PATH" "$nativeDIR/IDAStar.cpp" -c -o "$buildDIR/IDAStar.o"


echo "Linking C++ Code into libnative.so"
g++ -shared -o libnative.so build/*.o
