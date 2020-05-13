#include <jni.h>
#include <windows.h>
#include "Threads.h"

JNIEXPORT jint JNICALL Java_eu_menzani_ringbuffer_system_Threads_bindCurrentThread
  (JNIEnv *env, jclass clazz, jint cpu)
{
    if (SetThreadAffinityMask(GetCurrentThread(), 1 << cpu) == 0)
    {
        return GetLastError();
    }
    return 0;
}

JNIEXPORT jint JNICALL Java_eu_menzani_ringbuffer_system_Threads_setCurrentThreadPriority
  (JNIEnv *env, jclass clazz)
{
    if (SetThreadPriority(GetCurrentThread(), THREAD_PRIORITY_TIME_CRITICAL) == 0)
    {
        return GetLastError();
    }
    return 0;
}
