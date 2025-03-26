#include <iostream>
#include <vector>
#include <thread>
#include <future>
#include "../headers/Logger.h"

#include <jni.h>
#include "../headers/org_example_Native_MazeSolver.h"

JNIEXPORT void JNICALL Java_org_example_Native_MazeSolver_sayHello
(JNIEnv *, jclass) {
   std::cout << "Hello world";  
}

JNIEXPORT jstring JNICALL Java_org_example_Native_MazeSolver_returnHello
(JNIEnv *env, jclass) {
  std::string hello{"returned from cpp code"};
  return env->NewStringUTF(hello.c_str());
}

static int currentStep = 0;

struct Maze {
  int** maze;
  int rows{};
  int cols{};

  static Maze*& getInstance(const int& rows, const int& cols) {
    static Maze* instance;
    return instance;
  }

  ~Maze() {
    logger.Debug("~Maze() Deconstructor");

    for(int i = 0; i < this->rows; ++i) {
      delete[] maze[i];
    }
    delete[] maze;
  }

private:

  Maze(const int& rows, const int& cols) {
    this->allocate(rows, cols);
  }

  void allocate(const int& rows, const int& cols) {
    logger.Debug("allocate() Allocating maze");

    this->maze = new int*[rows];
    for(int i = 0; i < rows; ++i) {
      this->maze[i] = new int[cols];
    }
  }
};

JNIEXPORT jobjectArray JNICALL Java_org_example_Native_MazeSolver_solveMaze
(JNIEnv *env, jclass, jobjectArray maze) {
  logger.Info("hi");

  jobjectArray ans{};
  
  const int rows{env->GetArrayLength(maze)};
  const jobjectArray firstRow = static_cast<jobjectArray>(env->GetObjectArrayElement(maze, 0));
  const int columns = {env->GetArrayLength(firstRow)};

  Maze* m = Maze::getInstance(rows, columns);

  for(int i = 0; i < rows; ++i) {
    jobjectArray currentRow = (jobjectArray)((env->GetObjectArrayElement(maze, i)));
    for(int j = 0; j < columns; ++j) {
      
    } 
  }

  /*
  if(condition)

  */


  return ans;
}

//   JNIEXPORT jobject JNICALL Java_org_example_Native_MazeSolver_solveStep
//   (JNIEnv *env, jclass, jobject mazeObj) {
//     // Get the Maze class
//     // jclass mazeClass = env->GetObjectClass(mazeObj);
    
//     // // Get the 'mazeArray' field (assuming it's an int[][])
//     // jfieldID mazeArrayField = env->GetFieldID(mazeClass, "mazeArray", "[[I");
//     // jobjectArray mazeArray = (jobjectArray) env->GetObjectField(mazeObj, mazeArrayField);
    
//     // // Get the number of rows (height) and columns (width)
//     // jint mazeHeight = env->GetArrayLength(mazeArray);
//     // jint mazeWidth = env->GetArrayLength((jarray) env->GetObjectArrayElement(mazeArray, 0));

//     // // Create a new 2D array for the solved maze
//     // jobjectArray solvedMaze = env->NewObjectArray(mazeHeight, env->GetObjectClass(mazeArray), NULL);

//     // Example of a simple logic to solve the maze (just copy the array in this case)
//     // for (int i = 0; i < mazeHeight; i++) {
//     //     jintArray row = (jintArray) env->GetObjectArrayElement(mazeArray, i);
//     //     jint *rowElements = env->GetIntArrayElements(row, 0);

//     //     // For now, just copy the original maze to the solved maze
//     //     jintArray solvedRow = env->NewIntArray(mazeWidth);
//     //     env->SetIntArrayRegion(solvedRow, 0, mazeWidth, rowElements);
//     //     env->SetObjectArrayElement(solvedMaze, i, solvedRow);

//     //     env->ReleaseIntArrayElements(row, rowElements, 0);
//     // }

//     return {};
// }