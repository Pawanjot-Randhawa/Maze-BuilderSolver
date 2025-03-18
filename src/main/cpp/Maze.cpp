#include <iostream>
#include <jni.h>
#include <string>
#include "../headers/org_example_Models_Maze.h"

// JNI function implementation
JNIEXPORT jstring JNICALL Java_org_example_Models_Maze_sayHello(JNIEnv* env, jobject obj) {
    // Return a Java string containing "Hi from C++!"
    return env->NewStringUTF("Hi from C++!");
}