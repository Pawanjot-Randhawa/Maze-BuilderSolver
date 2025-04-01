#include <iostream>
#include <vector>
#include <thread>
#include <future>
#include <string>

#include <jni.h>

#include "../headers/Logger.h"
#include "../headers/org_example_Native_MazeSolver.h"
#include "../headers/Matrix.hpp"

#define WALL 1
#define GOAL 3
#define PATH 0
#define START 8


JNIEXPORT void JNICALL Java_org_example_Native_MazeSolver_sayHello
(JNIEnv *, jclass) {
   std::cout << "Hello world";  
}

JNIEXPORT jstring JNICALL Java_org_example_Native_MazeSolver_returnHello
(JNIEnv *env, jclass) {
  std::string hello{"returned from cpp code"};
  return env->NewStringUTF(hello.c_str());
}

// Allocates the Maze
JNIEXPORT jboolean JNICALL Java_org_example_Native_MazeSolver_InitializeMaze
  (JNIEnv *env, jclass, jobjectArray maze) {
    const int rows{env->GetArrayLength(maze)};
    const jobjectArray firstRow = static_cast<jobjectArray>(env->GetObjectArrayElement(maze, 0));
    const int columns = {env->GetArrayLength(firstRow)};
    
    Matrix<int>& matrix = Matrix<int>::GetInstance();
    matrix.Initialize(rows, columns, START, GOAL, WALL);

    for(int i = 0; i < rows; ++i) {
      jintArray currentRow = static_cast<jintArray>((env->GetObjectArrayElement(maze, i)));
      jint *rawInts = env->GetIntArrayElements(currentRow, NULL);

      for(int j = 0; j < columns; ++j) {
        matrix.Set(i, j, rawInts[j]);
      }

      env->ReleaseIntArrayElements(currentRow, rawInts, 0);
    }
    logger.Info("CHECK LOGGER");

    //matrix.Print();

    return true;  
}

JNIEXPORT jobject JNICALL Java_org_example_Native_MazeSolver_AStar
  (JNIEnv *env, jclass obj) 
{

  Matrix<int>& matrix = Matrix<int>::GetInstance();
  matrix.AStar();
  const auto path = matrix.GetPath();

  // Get java methods
  jclass arrayListClass = env->FindClass("java/util/ArrayList");
  jmethodID arrayListInit = env->GetMethodID(arrayListClass, "<init>", "()V"); // no args, void
  jmethodID arrayListAdd = env->GetMethodID(arrayListClass, "add", "(Ljava/lang/Object;)Z"); // z = return bool, take obj as arg


  // Create array list
  jobject resultList = env->NewObject(arrayListClass, arrayListInit);

  // Get the java int[] class
  jclass intArrayClass = env->FindClass("[I"); // [ = arr, I = primitive int]


  // convert to array list
  for (const auto& p : path) {
    jintArray intArray = env->NewIntArray(2);
    jint temp[2] = {p.first, p.second};
    env->SetIntArrayRegion(intArray, 0, 2, temp);
    
    env->CallBooleanMethod(resultList, arrayListAdd, intArray);
    env->DeleteLocalRef(intArray); // Cleanup local ref
  }

  return resultList; 

}

/*
PLAN:
1. Upon Init
  - Create a graph of the maze
2. 

*/