#include "opencv2/core.hpp"
#include <jni.h>
#include <opencv2/imgproc.hpp>

using namespace cv;
using namespace std;

extern "C"
JNIEXPORT void JNICALL
Java_com_leyline_computervision_opencv_NativeClass_grayScale(JNIEnv *env, jobject thiz, jlong addr_rgba) {
    Mat &img = *(Mat *) addr_rgba;
    cvtColor(img, img, COLOR_RGB2GRAY);
}extern "C"
JNIEXPORT void JNICALL
Java_com_leyline_computervision_opencv_NativeClass_bGR2RGB(JNIEnv *env, jobject thiz,
                                                           jlong addr_rgba) {
    Mat &img = *(Mat *) addr_rgba;
    cvtColor(img, img, COLOR_BGR2RGB);
}