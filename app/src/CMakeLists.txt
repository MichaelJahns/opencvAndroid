cmake_minimum_required(VERSION 3.10)
project(Tutorial VERSION 1.0)
set(CMAKE_C_COMPILER /path-to-ndk/toolchains/aarch64-linux-android-4.9/prebuilt/darwin-x86_64/bin/aarch64-linux-android-gcc)

set(CMAKE_VERBOSE_MAKEFILE on)
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=gnu++11")
include_directories(${CMAKE_SOURCE_DIR}/main/jniIncludes)

add_library(
        native-lib
        SHARED
        main/cpp/native-lib.cpp)

add_library(lib_opencv SHARED IMPORTED)

set_target_properties(lib_opencv PROPERTIES IMPORTED_LOCATION ${CMAKE_SOURCE_DIR}/main/jniLibs/${ANDROID_ABI}/libopencv_java4.so)

find_library(log-lib
        log)

target_link_libraries(native-lib ${log-lib} lib_opencv)
