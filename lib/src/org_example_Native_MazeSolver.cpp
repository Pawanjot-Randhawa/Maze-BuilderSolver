#include <iostream>
#include <string>
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