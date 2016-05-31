#include <jni.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "native.h"

#define SK "7f39a980096521740ca0a5f0a07bfed9"
#define SK_SIZE 17

char* substring(char*, int, int);
char* getIv();

jstring Java_com_zing_demo_ActJNI_getKey(JNIEnv * env, jobject this) {
    return (*env)->NewStringUTF(env, SK);
}

jstring Java_com_zing_demo_ActJNI_getIvKey(JNIEnv * env, jobject this) {
    char* iv = getIv();
    return (*env)->NewStringUTF(env, iv);
}

char* getIv() {
	if(strlen(SK) > SK_SIZE) {
        char* result = malloc(SK_SIZE);
        memset(result, '\0', SK_SIZE);
        strncpy(result, SK, SK_SIZE - 1);
        return result;
    }
    else
        return SK;
}