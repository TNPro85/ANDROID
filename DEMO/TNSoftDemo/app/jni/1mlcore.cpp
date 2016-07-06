#include <jni.h>
#include <string.h>
#include <stdlib.h>
#include "1mlcore.h"
#include "md5.h"

#include <android/log.h>
#define APPNAME "JNI"

extern "C" JNIEXPORT jstring
JNICALL Java_com_zing_demo_ActJNI_getSK(JNIEnv * env, jobject object, jobjectArray inputArr) {
    int arrSize = env->GetArrayLength(inputArr);
    __android_log_print(ANDROID_LOG_VERBOSE, APPNAME, "arrSize: %d", arrSize);

    const char* arr[arrSize];
    int i;

    int resultLength = 0;
    char* result;

    for(i = 0; i < arrSize; i++) {
        jstring str = (jstring)env->GetObjectArrayElement(inputArr, i);
        __android_log_print(ANDROID_LOG_VERBOSE, APPNAME, "str: %s", env->GetStringUTFChars(str, 0));

        arr[i] = env->GetStringUTFChars(str, JNI_FALSE);
        resultLength += strlen(arr[i]);
    }

    sort(arr, arrSize);

    int kSize = 7;
    const char* k[kSize] = {"1", "m", "a", "n", "l", "a", "b"};
    sort(k, kSize);

    resultLength += (kSize + 1);

    result = (char*) malloc(resultLength);
    memset(result, '\0', resultLength);

    for(i = 0; i < arrSize; i++) {
        strcat(result, arr[i]);
    }

    for(i = 0; i < kSize; i++) {
        strcat(result, k[i]);
    }

    //result = digestString(result);
    //__android_log_print(ANDROID_LOG_VERBOSE, APPNAME, "md5: %s", result);

    return env->NewStringUTF(digestString(result));
}

static int myCompare (const void * a, const void * b) {
    return strcmp (*(const char **) a, *(const char **) b);
}

void sort(const char *arr[], int n) {
    qsort (arr, n, sizeof (const char *), myCompare);
}