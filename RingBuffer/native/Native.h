#include <jni.h>

#ifndef _Included_org_ringbuffer_Native
#define _Included_org_ringbuffer_Native

#ifdef __cplusplus
extern "C" {
#endif

JNIEXPORT void JNICALL Java_org_ringbuffer_system_WindowsSleep_prepare
  (JNIEnv *, jclass);

JNIEXPORT void JNICALL Java_org_ringbuffer_system_WindowsSleep_sleepHalfAMillisecond
  (JNIEnv *, jclass);

JNIEXPORT jint JNICALL Java_org_ringbuffer_system_Threads_bindCurrentThread
  (JNIEnv *, jclass, jint);

JNIEXPORT jint JNICALL Java_org_ringbuffer_system_Threads_setCurrentThreadPriority
  (JNIEnv *, jclass);

#ifdef __cplusplus
}
#endif

#endif
