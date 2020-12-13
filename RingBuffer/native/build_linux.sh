#!/bin/sh
gcc -I/usr/lib/jvm/java-11-openjdk-11.0.9.11-0.el7_9.x86_64/include -I/usr/lib/jvm/java-11-openjdk-11.0.9.11-0.el7_9.x86_64/include/linux -o ../res/libringbuffer_64.so -shared -fPIC Native_linux.c
gcc -m32 -I/usr/lib/jvm/java-11-openjdk-11.0.9.11-0.el7_9.x86_64/include -I/usr/lib/jvm/java-11-openjdk-11.0.9.11-0.el7_9.x86_64/include/linux -o ../res/libringbuffer_32.so -shared -fPIC Native_linux.c
