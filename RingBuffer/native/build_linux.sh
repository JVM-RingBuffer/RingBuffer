#!/bin/sh
gcc -I/usr/lib/jvm/java-11-openjdk-11.0.7.10-4.el7_8.x86_64/include -I/usr/lib/jvm/java-11-openjdk-11.0.7.10-4.el7_8.x86_64/include/linux -o ../res/libringbuffer_64.so -shared -fPIC Native_linux.c
gcc -m32 -I/usr/lib/jvm/java-11-openjdk-11.0.7.10-4.el7_8.x86_64/include -I/usr/lib/jvm/java-11-openjdk-11.0.7.10-4.el7_8.x86_64/include/linux -o ../res/libringbuffer_32.so -shared -fPIC Native_linux.c
