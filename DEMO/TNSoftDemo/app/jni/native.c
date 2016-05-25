#include <jni.h>
#include <string.h>

jstring Java_com_zing_demo_ActJNI_getKey(JNIEnv * env, jobject this) {
    return (*env)->NewStringUTF(env, "7f39a980096521740ca0a5f0a07bfed9");
}