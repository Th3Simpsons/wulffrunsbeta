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
#define EPS 1.E-9
#define STEPSIZE 1.E-6

//
// Calculate F(x) at point x.
//

double sq2(double x) {
	return x * x;
}

double integrate(Function& f, double a, double b) {
	double i;

	double sum = 0;
	std::cout << "berechne integral... " << std::endl;
	for (i = a; i < b; i += STEPSIZE) {
		sum += ((f(i) + f(i + STEPSIZE)) * STEPSIZE) / 2;

	}
//	std::cout << "integral von x_q von 0 bis " << b << " ist " << sum			<< std::endl;
	return sum;
}
//by default differentiate x^2
double integrateSQ(double a, double b) {
	double i;
	double sum = 0;

	for (i = a; i < b; i += STEPSIZE) {
		sum = sum + ((sq2(i) + sq2(i+STEPSIZE)) * STEPSIZE / 2);

	}
	std::cout << "integral von x_q von 0 bis " << b << " ist " << sum
			<< std::endl;

	return sum;

}
JNIEXPORT jdouble JNICALL Java_de_nog_Integrator_integrate(JNIEnv * env,
		jobject th, jobject func, jdouble a, jdouble b) {
	std::cout << "suche java funktion fuer integral" << std::endl;
	JavaFunction f = JavaFunction(env, func);
	std::cout << "und jetzt geh ich integrieren..." << std::endl;

	return integrate(f, a, b);
}
