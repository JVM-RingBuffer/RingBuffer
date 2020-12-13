#define _GNU_SOURCE

#include <jni.h>
#include <time.h>
#include <sched.h>
#include <errno.h>
#include "Native.h"

JNIEXPORT void JNICALL Java_org_ringbuffer_system_LinuxSleep_sleep
  (JNIEnv *env, jclass clazz, jint nanoseconds)
{
    struct timespec time;
    time.tv_sec = 0;
    time.tv_nsec = nanoseconds;
    nanosleep(&time, NULL);
}

JNIEXPORT jint JNICALL Java_org_ringbuffer_system_Threads_bindCurrentThread
  (JNIEnv *env, jclass clazz, jint cpu)
{
    cpu_set_t mask;
    CPU_ZERO(&mask);
    CPU_SET(cpu, &mask);
    if (sched_setaffinity(0, sizeof(mask), &mask) == -1)
    {
        return errno;
    }
    return 0;
}

JNIEXPORT jint JNICALL Java_org_ringbuffer_system_Threads_setCurrentThreadPriority
  (JNIEnv *env, jclass clazz)
{
    struct sched_param param;
    param.sched_priority = sched_get_priority_max(SCHED_FIFO);
    if (sched_setscheduler(0, SCHED_FIFO | SCHED_RESET_ON_FORK, &param) == -1)
    {
        return errno;
    }
    return 0;
}
