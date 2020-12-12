#include "opencv2/core.hpp"
#include <jni.h>
#include <opencv2/imgproc.hpp>

using namespace cv;
using namespace std;

extern "C"
JNIEXPORT void JNICALL Java_com_leyline_opencvandroid_openCV_NativeClass_grayScale(JNIEnv *env, jclass type, jlong addrRgba) {
    Mat &img = *(Mat *) addrRgba;
    cvtColor(img, img, COLOR_RGB2GRAY);
}
extern "C"
JNIEXPORT void JNICALL Java_com_leyline_opencvandroid_openCV_NativeClass_BGR2RGB(JNIEnv *env, jclass type, jlong addrRgba) {
    Mat &img = *(Mat *) addrRgba;
    cvtColor(img, img, COLOR_BGR2RGB);
}
