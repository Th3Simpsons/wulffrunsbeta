/*
 * Differentiator.cpp
 *
 *  Created on: 05.12.2016
 *      Author: lipow
 */

#include "Function.h"
#include <iostream>
#include <stdlib.h>
#include <cmath>
#include "de_nog_Differentiator.h"
#include "JavaFunction.h"
#include "Differentiator.h"
// precision for the numerical calculus. */
#define EPS 1.E-9
#define MAXSTEPS 50

//
// Calculate f'(x) = df/dx at point x.
//
//double sq1(double x) {
//	return x * x;
//}

double differentiate(Function& f, double x) {
	int i = 10;
	double d1 = 0, d2 = 0;
	double ld1, ld2;
	double h = 1.E-6;
	double fx = f(x);
	if (fx != f(x)) {
		throw "Stop fuckin' with me";
	}

	for (i = MAXSTEPS; i > 0 && h > EPS; i--) {
		ld1 = d1;
		ld2 = d2;

		d1 = (f(x + h) - fx) / h;
		d2 = (fx - f(x - h)) / h;
		if (i != MAXSTEPS && (ld1 * ld1 < 0 || ld2 * d2 < 0)) {
			//printf("delta isse crazy!!");
			throw "crazy shit";
		}

		if (std::abs(d1) < EPS && std::abs(d2) < EPS) {
			//	std::cout << "early return at " << i << std::endl;
			return (d1 + d2) / 2;
		}

		h /= 8;
	}
	//std::cout << "ableitung von x_q bei " << x <<" ist " <<  (d1 + d2) / 2 << std::endl;
	return (d1 + d2) / 2;
}
//by default differentiate x^2
//double differentiateSQ(double x) {
//	return 0;

//}
/*
 * Class:     de_nog_Differentiator
 * Method:    differentiate
 * Signature: (Lde/lab4inf/wrb/Function;D)D
 */
JNIEXPORT jdouble JNICALL Java_de_nog_Differentiator_differentiate(JNIEnv * env,
		jobject th, jobject func, jdouble x) {
	JavaFunction f = JavaFunction(env, func);
	try {
		return differentiate(f, x);
	} catch (const char *message) {
		jclass Exception = env->FindClass("java/lang/IllegalStateException");
		env->ThrowNew(Exception, "no convergence");
	}
	return -1;
}
