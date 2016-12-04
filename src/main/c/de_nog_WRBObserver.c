#include <jni.h>

#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     de_nog_WRBObserver
 * Method:    differentiate
 * Signature: (Lde/lab4inf/wrb/Function;D)D
 */
JNIEXPORT jdouble JNICALL Java_de_nog_WRBObserver_differentiate
  (JNIEnv *, jobject, jobject, jdouble){return 1337;}

/*
 * Class:     de_nog_WRBObserver
 * Method:    integrate
 * Signature: (Lde/lab4inf/wrb/Function;DD)D
 */
JNIEXPORT jdouble JNICALL Java_de_nog_WRBObserver_integrate
  (JNIEnv *, jobject, jobject, jdouble, jdouble){return 123;}

#ifdef __cplusplus
}
#endif

