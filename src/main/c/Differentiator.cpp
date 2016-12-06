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
// precision for the numerical calculus. */
#define EPS 1.E-9
#define MAXSTEPS 50

//
// Calculate f �(x) = df/dx at point x.
//
double sq1(double x) {
	return x * x;
}

double differentiate(Function& f, double x) {
	int i = 10;
	double d1, d2;
	double h = 0.001;
	for (i = MAXSTEPS; i > 0 && h > EPS; i--) {
		d1 = (f(x + h) - f(x)) / h;
		d2 = (f(x) - f(x - h)) / h;
		if (std::abs(d1) < EPS && std::abs(d2) < EPS) {
			std::cout << "early return at " << i << std::endl;
			return d2;
			return (d1 + d2) / 2;
		}
		h /= 4;
	}
	return (d1 + d2) / 2;
}
//by default differentiate x^2
double differentiate(double x) {
	int i = 10;
	double d1, d2;
	double h = 0.001;
	for (i = MAXSTEPS; i > 0 && h > EPS; i--) {
		d1 = (sq1(x + h) - sq1(x)) / h;
		d2 = (sq1(x) - sq1(x - h)) / h;
		if (std::abs(d1) < EPS && std::abs(d2) < EPS) {
			std::cout << "early return at " << i << std::endl;
			return d2;
			return (d1 + d2) / 2;
		}
		h /= 4;
	}
	return (d1 + d2) / 2;

}
/*
 *  b
 * /
 *   Calculate / f(t) dt.
 * /
 * a
 */
/*
 * Class:     de_nog_Differentiator
 * Method:    differentiate
 * Signature: (Lde/lab4inf/wrb/Function;D)D
 */
JNIEXPORT jdouble JNICALL Java_de_nog_Differentiator_differentiate(JNIEnv * env,
		jobject th, jobject func, jdouble x) {
	JavaFunction f =JavaFunction(env,func);
	return differentiate(f, x);
}
