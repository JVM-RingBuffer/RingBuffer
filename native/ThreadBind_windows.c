#include <jni.h>
#include <windows.h>
#include "ThreadBind.h"

JNIEXPORT int JNICALL Java_eu_menzani_ringbuffer_system_ThreadBind_bindCurrentThread(JNIEnv * env, jclass clazz, int cpu)
{
    if (SetThreadAffinityMask(GetCurrentThread(), 1 << cpu) == 0)
    {
        return GetLastError();
    }
    return 0;
}
