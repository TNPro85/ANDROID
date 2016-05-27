#include <jni.h>
#include <string.h>

#define SK "7f39a980096521740ca0a5f0a07bfed9"
#define KSIZE 16

jstring Java_com_zing_demo_ActJNI_getKey(JNIEnv * env, jobject this) {
    return (*env)->NewStringUTF(env, SK);
}

jstring Java_com_zing_demo_ActJNI_getIvKey(JNIEnv * env, jobject this) {
    return (*env)->NewStringUTF(env, getIv());
}

char *getIv() {
    char *result = malloc(KSIZE);
    int keyLength = strlen(SK);
    while(strlen(result) < KSIZE) {
        strcat(result, SK);
    }
    return substring(result, 0, KSIZE);
}

/*C substring function: It returns a pointer to the substring */
char *substring(char *string, int position, int length)
{
   char *pointer;
   int c;

   pointer = malloc(length+1);

   if (pointer == NULL) {
      printf("Unable to allocate memory.\n");
      exit(1);
   }

   for (c = 0 ; c < length ; c++) {
      *(pointer+c) = *(string+position-1);
      string++;
   }

   *(pointer+c) = '\0';

   return pointer;
}