#ifndef JAVAFUNCTION_H_
#define JAVAFUNCTION_H_
#include <jni.h>
#include "Function.h"
/*
 A C++ Function suitable for JNI calls.
 */
class JavaFunction: public Function {
private:
	JNIEnv *env;
	jobject instance;
	jmethodID fct;
public:
	/*
	 Constructor to wrap a Java Function implementation.
	 */
	JavaFunction(JNIEnv *env, jobject instance){
		this->env = env;
		this->instance=instance;
		jclass c = env->GetObjectClass(instance);
		this->fct = env->GetMethodID(c,"eval","([D)D");
	}
	~JavaFunction(){
		//printf("destruktor javafunc wurde gerufen");
	};
// overloaded  operator  to  execute    double  y  = f(x)
//  for  java  function  implementations.
	double operator()(double x) const {

		jdoubleArray param;
		param = env->NewDoubleArray(1);
		jdouble tmp[1];
		tmp[0] = x;
		env->SetDoubleArrayRegion(param, 0, 1, tmp);


		return env->CallDoubleMethod(instance, fct, param);
	}

}
;
#endif
