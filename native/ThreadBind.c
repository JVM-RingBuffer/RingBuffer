#define _GNU_SOURCE

#include <jni.h>
#include <sched.h>
#include <errno.h>
#include "ThreadBind.h"

JNIEXPORT int JNICALL Java_eu_menzani_ringbuffer_system_ThreadBind_bindCurrentThread(JNIEnv * env, jclass clazz, int cpu)
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
