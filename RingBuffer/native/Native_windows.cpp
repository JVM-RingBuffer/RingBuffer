#include <jni.h>
#include <windows.h>
#include "Native.h"

static NTSTATUS(__stdcall *NtDelayExecution)(BOOL Alertable, PLARGE_INTEGER DelayInterval) = (NTSTATUS(__stdcall*)(BOOL, PLARGE_INTEGER)) GetProcAddress(GetModuleHandle("ntdll.dll"), "NtDelayExecution");
static NTSTATUS(__stdcall *ZwSetTimerResolution)(IN ULONG RequestedResolution, IN BOOLEAN Set, OUT PULONG ActualResolution) = (NTSTATUS(__stdcall*)(ULONG, BOOLEAN, PULONG)) GetProcAddress(GetModuleHandle("ntdll.dll"), "ZwSetTimerResolution");

static LARGE_INTEGER minimumInterval;

JNIEXPORT void JNICALL Java_org_ringbuffer_system_WindowsSleep_prepare
  (JNIEnv *env, jclass clazz)
{
    ULONG actualResolution;
    ZwSetTimerResolution(1, true, &actualResolution);

    minimumInterval.QuadPart = -1;
}

JNIEXPORT void JNICALL Java_org_ringbuffer_system_WindowsSleep_sleepHalfAMillisecond
  (JNIEnv *env, jclass clazz)
{
    NtDelayExecution(false, &minimumInterval);
}

JNIEXPORT jint JNICALL Java_org_ringbuffer_system_Threads_bindCurrentThread
  (JNIEnv *env, jclass clazz, jint cpu)
{
    if (SetThreadAffinityMask(GetCurrentThread(), 1 << cpu) == 0)
    {
        return GetLastError();
    }
    return 0;
}

JNIEXPORT jint JNICALL Java_org_ringbuffer_system_Threads_setCurrentThreadPriority
  (JNIEnv *env, jclass clazz)
{
    if (SetThreadPriority(GetCurrentThread(), THREAD_PRIORITY_TIME_CRITICAL) == 0)
    {
        return GetLastError();
    }
    return 0;
}
