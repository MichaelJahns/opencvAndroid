#include "opencv2/core.hpp"
#include <jni.h>
#include <opencv2/imgproc.hpp>
#include <jni.h>

using namespace cv;
using namespace std;

extern "C"
JNIEXPORT void JNICALL Java_com_leyline_opencvandroid_openCV_NativeClass_testFunction(JNIEnv *env, jclass type, jlong addrRgba) {
Mat &img = *(Mat *) addrRgba;
cvtCOlor
}