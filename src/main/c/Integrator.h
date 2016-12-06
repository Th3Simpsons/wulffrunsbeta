/*
 * Diffentiator.h
 *
 *  Created on: 05.12.2016
 *      Author: lipow
 */
#ifndef INTEGRATOR_H_
#define INTEGRATOR_H_

#include "Function.h"
#ifdef __cplusplus
extern "C" {
#endif

double integrate(Function& f, double a, double b);
double integrateSQ(double a, double b);

#ifdef __cplusplus
}
#endif
#endif /* INTEGRATOR_H_ */
