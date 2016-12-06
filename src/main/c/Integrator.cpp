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
// precision for the numerical calculus. */
#define EPS 1.E-9
#define STEPSIZE 1.E-3

//
// Calculate f ’(x) = df/dx at point x.
//

double sq2(double x) {
	return x * x ;
}

double integrate(Function& f, double a, double b) {
	return -1;
}
//by default differentiate x^2
double integrate(double a, double b) {
	double i ;
	double sum = 0;

	for (i = a; i < b ; i+= STEPSIZE) {
		sum += (sq2(a) + sq2(b)) * STEPSIZE / 2;

	}
	return sum;

}
/*
 *  b
 * /
 *   Calculate / f(t) dt.
 * /
 * a
 */
