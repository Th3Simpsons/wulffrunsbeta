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
#include "de_nog_Integrator.h"
#include "JavaFunction.h"
#include "Integrator.h"
// precision for the numerical calculus. */
#define EPS 4.E-4
#define STARTSTEP 60;
#define MULTI 5
#define MAXREPEAT 4
//
// Calculate F(x) at point x.
//

double sq2(double x) {
	return x * x;
}

double integrate(Function& f, double a, double b) {
	double i, max_rep = MAXREPEAT;
	double diff = 0;
	double lastFuncVal = 0;
	double currentFuncVal = 0;
	double approxSum = 0, normSum = 0; //, rightsum = 0;
	if (b == a)
		return 0;

	//std::cout << "berechne integral... " << std::endl;
	//double width = (b - a) / (double) STARTSTEP;
	int n = STARTSTEP
	;
	do {
		approxSum = 0;
		lastFuncVal = f(a);
		if (lastFuncVal != f(a)) {
			throw "crazy shit right here";
		}
		for (i = 0; i < n; i++) {
			double xl = a + (double) (i) * (b - a) / (double) n;
			double xr = a + (double) (i + 1) * (b - a) / (double) n;

			currentFuncVal = f(xr);
			double midVal = f((xl + xr) / (double) 2);
			approxSum += (lastFuncVal) + 4 * midVal + currentFuncVal;
			normSum += (lastFuncVal) + midVal + currentFuncVal;
			lastFuncVal = currentFuncVal;
		}
		max_rep--;

		approxSum = (b - a) * approxSum / (double) 6 / (double) n;
		normSum = (b - a) * normSum / (double) 3 / (double) n;

		diff = approxSum < normSum ?
				(normSum / approxSum) : (approxSum / normSum);
		n = n * MULTI;
	} while (diff > 1 + EPS && max_rep > 0);
	if (0 >= max_rep) {
		printf("too many reps, precision=%16.14f\n", diff);
	} else {
		printf("ended with precision %f\n", diff);
	}
//	std::cout << "integral von x_q von 0 bis " << b << " ist " << sum			<< std::endl;
	return (approxSum);
}
//by default differentiate x^2
double integrateSQ(double a, double b) {
	return 2;

}
JNIEXPORT jdouble JNICALL Java_de_nog_Integrator_integrate(JNIEnv * env,
		jobject th, jobject func, jdouble a, jdouble b) {
//std::cout << "suche java funktion fuer integral" << std::endl;
	JavaFunction f = JavaFunction(env, func);
//std::cout << "und jetzt geh ich integrieren..." << std::endl;
	try {
		return integrate(f, a, b);
	} catch (const char *message) {
		jclass Exception = env->FindClass("java/lang/IllegalStateException");
		env->ThrowNew(Exception, "no convergence");
	}
	return -1;
}
