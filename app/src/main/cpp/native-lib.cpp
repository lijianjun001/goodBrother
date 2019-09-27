#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring JNICALL
Java_com_antelope_goodbrother_manager_KeyManager_getKey(
        JNIEnv *env,
        jobject instance) {
    std::string key = "6fd87651ae1628db8efd0b6f1dd799a";
    return env->NewStringUTF(key.c_str());
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_antelope_goodbrother_manager_KeyManager_getInitVector(JNIEnv *env, jobject instance) {
    std::string initVector = "uTrFU6GWmdIm_Rje";
    return env->NewStringUTF(initVector.c_str());
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_antelope_goodbrother_manager_KeyManager_getWxKey(JNIEnv *env, jobject instance) {
    std::string key = "";
    return env->NewStringUTF(key.c_str());
}
