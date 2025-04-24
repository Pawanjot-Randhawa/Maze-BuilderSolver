Write-Host "Java path: $env:JAVA_HOME" 
$nativeDIR = "lib/src/"
$headersDIR = "lib/headers"
$srcDIR="src/main/java/org/example/"
$buildDIR="build"

Write-Host "Generating headers into lib/headers"
javac -h $headersDIR/ $srcDIR/native/**.java $srcDIR/Maze.java

# Java Include Directories
$JAVA_INCLUDE_PATH = "$env:JAVA_HOME\include"
$JAVA_INCLUDE_WIN32_PATH = "$env:JAVA_HOME\include\win32"

# Compile the C++ source code
Write-Host "Compiling C++ Code" 

g++ -c -I"$JAVA_INCLUDE_PATH" -I"$JAVA_INCLUDE_WIN32_PATH" $nativeDIR\org_example_Native_MazeSolver.cpp -o $buildDIR\Solver.o
g++ -c -I"$JAVA_INCLUDE_PATH" -I"$JAVA_INCLUDE_WIN32_PATH" $nativeDIR\Logger.cpp -o $buildDIR\Log.o
g++ -c -I"$JAVA_INCLUDE_PATH" -I"$JAVA_INCLUDE_WIN32_PATH" $nativeDIR\Matrix.cpp -o $buildDIR\Matrix.o
g++ -c -I"$JAVA_INCLUDE_PATH" -I"$JAVA_INCLUDE_WIN32_PATH" $nativeDIR\AStar.cpp -o $buildDIR\AStar.o
g++ -c -I"$JAVA_INCLUDE_PATH" -I"$JAVA_INCLUDE_WIN32_PATH" $nativeDIR\Dijkstra.cpp -o $buildDIR\Dijkstra.o
g++ -c -I"$JAVA_INCLUDE_PATH" -I"$JAVA_INCLUDE_WIN32_PATH" $nativeDIR\BlockNdPath.cpp -o $buildDIR\BlockNdPath.o
g++ -c -I"$JAVA_INCLUDE_PATH" -I"$JAVA_INCLUDE_WIN32_PATH" $nativeDIR\BFS.cpp -o $buildDIR\BFS.o



# Linking C++ Code
Write-Host "Linking C++ Code"
g++ --% -shared -o native.dll build\*.o -Wl,--add-stdcall-alias