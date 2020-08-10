#include <jni.h>

#ifndef _Included_org_ringbuffer_system_Threads
#define _Included_org_ringbuffer_system_Threads

JNIEXPORT jint JNICALL Java_org_ringbuffer_system_Threads_bindCurrentThread
  (JNIEnv *, jclass, jint);

JNIEXPORT jint JNICALL Java_org_ringbuffer_system_Threads_setCurrentThreadPriority
  (JNIEnv *, jclass);

#endif
