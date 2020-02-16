#include <jni.h>

#ifndef _Included_eu_menzani_ringbuffer_threadbind_ThreadBind
#define _Included_eu_menzani_ringbuffer_threadbind_ThreadBind

JNIEXPORT int JNICALL Java_eu_menzani_ringbuffer_threadbind_ThreadBind_bindCurrentThread(JNIEnv *, jclass, int);

#endif
