## Build

Output directory is `../res`.  
Binaries for all platforms are version-controlled and stored as resources in the JAR.
They are then extracted and loaded.

### CentOS 7

```
yum install gcc glibc-devel.i686 libgcc.i686
yum install java-11-openjdk-devel
./build_linux.sh
```

### Windows 10

Build Tools for Visual Studio 2019  
Workload: C++ build tools  
Optional: MSVC - VS C++ x64/x86 build tools, Windows 10 SDK  
Set `JAVA_HOME` to JDK 11  
`build_windows.bat`
