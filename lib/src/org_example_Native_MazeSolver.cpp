#include <iostream>
#include <vector>
#include <thread>
#include <future>
#include <string>

#include <jni.h>

#include "../headers/Logger.hpp"
#include "../headers/org_example_Native_MazeSolver.h"
#include "../headers/Matrix.hpp"

#define WALL 8
#define GOAL 1
#define PATH 0
#define START 2
#define BLOCKED 7

std::string jstring2string(JNIEnv* env, jstring jStr);

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
    logger.Info("Initialzing Maze");
    std::cout << "HELLO CAN YOU SEE ME??\n\n";
    
    const int rows{env->GetArrayLength(maze)};
    const jobjectArray firstRow = static_cast<jobjectArray>(env->GetObjectArrayElement(maze, 0));
    const int columns = {env->GetArrayLength(firstRow)};
    
    Matrix<int>& matrix = Matrix<int>::GetInstance();
    matrix.Initialize(rows, columns, START, GOAL, WALL, PATH, BLOCKED);

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

    logger.Info("Solving Maze");

    Matrix<int>& matrix = Matrix<int>::GetInstance();
    matrix.AStar();
    const auto path = matrix.GetPath();

    std::string str{};
    for(const auto& p : path) {
    str += std::to_string(p.first) + " " + std::to_string(p.second) + "\n"; 
    }
    std::cout << str << "\n";
    logger.Info(str.c_str());

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

    logger.Info("Solved Maze");
    return resultList; 

}

JNIEXPORT jobject JNICALL Java_org_example_Native_MazeSolver_SolveMaze
  (JNIEnv *env, jclass obj, jstring str) {
    
    logger.Info("Solving Maze");
    Matrix<int>& matrix = Matrix<int>::GetInstance();
    matrix.SolveWith(
        jstring2string(env, str)
    );
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

    logger.Info("Solved Maze");
    return resultList; 
}

std::string jstring2string(JNIEnv* env, jstring jStr) {
    if(!jStr) {
        return "";
    }

    const jclass stringClass = env->GetObjectClass(jStr);
    const jmethodID getBytes = env->GetMethodID(stringClass, "getBytes", "(Ljava/lang/String;)[B");
    const jbyteArray stringJbytes = (jbyteArray) env->CallObjectMethod(jStr, getBytes, env->NewStringUTF("UTF-8"));

    size_t length = (size_t) env->GetArrayLength(stringJbytes);
    jbyte* pBytes = env->GetByteArrayElements(stringJbytes, NULL);

    std::string ret = std::string((char *)pBytes, length);
    env->ReleaseByteArrayElements(stringJbytes, pBytes, JNI_ABORT);

    env->DeleteLocalRef(stringJbytes);
    env->DeleteLocalRef(stringClass);
    return ret;
}