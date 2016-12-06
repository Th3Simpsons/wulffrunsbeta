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
		return env->CallDoubleMethod(instance, fct, x);
	}
}
;
#endif
