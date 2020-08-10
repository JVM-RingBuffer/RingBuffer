call "C:\Program Files (x86)\Microsoft Visual Studio\2019\BuildTools\VC\Auxiliary\Build\vcvarsall.bat" x64
cl /I "%JAVA_HOME%\include" /I "%JAVA_HOME%\include\win32" /Fe"..\res\ThreadManipulation_64" /LD Threads_windows.c
call "C:\Program Files (x86)\Microsoft Visual Studio\2019\BuildTools\VC\Auxiliary\Build\vcvarsall.bat" x86
cl /I "%JAVA_HOME%\include" /I "%JAVA_HOME%\include\win32" /Fe"..\res\ThreadManipulation_32" /LD Threads_windows.c

del Threads_windows.obj
del ..\res\ThreadManipulation_32.exp
del ..\res\ThreadManipulation_32.lib
del ..\res\ThreadManipulation_64.exp
del ..\res\ThreadManipulation_64.lib