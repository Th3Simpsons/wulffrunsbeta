/*
 * main.cpp
 *
 *  Created on: 06.12.2016
 *      Author: stefan
 */

#include <jni.h>
#include "Integrator.h"
#include "Differentiator.h"
#include <math.h>
#define EPS 1.E-3
#define MAXSTEPS 50
double sq1(double x) {
	return x * x;
}

double x2(double x) {
	return x * x;
}

double expo(double x) {
	return exp(x);
}

double sinus(double x) {
	return sin(x);
}

int main() {
	printf("diff x2\n");
	Function fct = Function((FctPointer) x2, "");
	printf("  x         ist        soll        diff \n");
	for (double b = 1; b < 10; b += 1) {
		double ist, soll;
		ist = differentiate(fct, b);
		soll = b * 2.0;
		double diff = ist - soll;
		if (diff < 0)
			diff *= -1;
		printf(" %+9.5f | %+9.5f | %+9.5f | %+9.5f  \n", b, ist, soll, diff);
		if (diff > 1.E-4) {
			printf("zu ungenau \n");

		}
	}

	printf("integrate \n");
	printf("  x        ist        soll        diff \n");
	for (double b = 1; b < 10; b += 1) {

		double ist, soll;
		ist = integrate(fct, 0, b);
		soll = b * b * b / 3.0;

		double diff = ist - soll;
		if (diff < 0)
			diff *= -1;
		printf(" %+9.5f | %+9.5f | %+9.5f | %+9.5f  \n", b, ist, soll, diff);
		if (diff > 1.E-2) {
			printf("zu ungenau \n");

		}
	}

	printf("diff exp\n");
	Function fct2 = Function((FctPointer) expo, "");
	printf("  x         ist        soll        diff \n");
	for (double b = 1; b < 10; b += 1) {
		double ist, soll;
		ist = differentiate(fct2, b);
		soll = exp(b);
		double diff = ist - soll;
		if (diff < 0)
			diff *= -1;
		printf(" %+9.5f | %+9.5f | %+9.5f | %+9.5f  \n", b, ist, soll, diff);
		if (diff > 1.E-2) {
			printf("zu ungenau \n");

		}
	}

	printf("integrate exp \n");
	printf("  x        ist        soll        diff \n");
	for (double b = 1; b < 10; b += 1) {

		double ist, soll;
		ist = integrate(fct2, 0, b);
		soll = exp(b) - 1;

		double diff = ist - soll;
		if (diff < 0)
			diff *= -1;
		printf(" %+9.5f | %+9.5f | %+9.5f | %+9.5f  \n", b, ist, soll, diff);
		if (diff > 1.E-4) {
			printf("zu ungenau \n");

		}
	}

	printf("diff\n");
	Function fct3 = Function((FctPointer) sinus, "");
	printf("  x         ist        soll        diff \n");
	for (double b = 0; b <= 3.3615; b += 3.1415/8.0) {
		double ist, soll;
		ist = differentiate(fct3, b);
		soll = cos(b);
		double diff = ist - soll;
		if (diff < 0)
			diff *= -1;
		printf(" %+9.5f | %+9.5f | %+9.5f | %+9.5f  \n", b, ist, soll, diff);
		if (diff > 1.E-4) {
			printf("zu ungenau \n");

		}
	}

	printf("integrate \n");
	printf("  x        ist        soll        diff \n");
	for (double b = 0; b < 3.3615; b += 3.1415/8.0) {

		double ist, soll;
		ist = integrate(fct3, 0, b);
		soll = 1 - cos(b);

		double diff = ist - soll;
		if (diff < 0)
			diff *= -1;
		printf(" %+9.5f | %+9.5f | %+9.5f | %+9.5f  \n", b, ist, soll, diff);
		if (diff > 1.E-4) {
			printf("zu ungenau \n");

		}
	}

	return 0;
}

