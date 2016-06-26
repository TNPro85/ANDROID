#include <jni.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "native.h"
#include "md5.h"

#include <android/log.h>
#define APPNAME "JNI"

#define SK "7f39a980096521740ca0a5f0a07bfed9"
#define SK_SIZE 17

jstring Java_com_zing_demo_ActJNI_getKey(JNIEnv * env, jobject this) {
    return (*env)->NewStringUTF(env, SK);
}

jstring Java_com_zing_demo_ActJNI_getIvKey(JNIEnv * env, jobject this) {
    char* iv = getIv();
    return (*env)->NewStringUTF(env, iv);
}

jstring Java_com_zing_demo_ActJNI_getSK(JNIEnv * env, jobject this, jobjectArray inputArr) {
    int arrSize = (*env)->GetArrayLength(env, inputArr);
    __android_log_print(ANDROID_LOG_VERBOSE, APPNAME, "arrSize: %d", arrSize);

    const char* arr[arrSize];
    int i;

    int resultLength = 0;
    char* result;

    for(i = 0; i < arrSize; i++) {
        jstring str = (jstring) (*env)->GetObjectArrayElement(env, inputArr, i);
        __android_log_print(ANDROID_LOG_VERBOSE, APPNAME, "str: %s", (*env)->GetStringUTFChars(env, str, 0));

        arr[i] = (*env)->GetStringUTFChars(env, str, JNI_FALSE);
        resultLength += strlen(arr[i]);
    }

    sort(arr, arrSize);

    resultLength += 1;
    result = malloc(resultLength);
    memset(result, '\0', resultLength);

    for(i = 0; i < arrSize; i++) {
        strcat(result, arr[i]);
    }

    md5(result, strlen(result));

    return (*env)->NewStringUTF(env, result);
}

static int myCompare (const void * a, const void * b) {
    return strcmp (*(const char **) a, *(const char **) b);
}

void sort(const char *arr[], int n) {
    qsort (arr, n, sizeof (const char *), myCompare);
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