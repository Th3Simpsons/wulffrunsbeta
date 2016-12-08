/*
 * Diffentiator.h
 *
 *  Created on: 05.12.2016
 *      Author: lipow
 */
#ifndef DIFFERENTIATOR_H_
#define DIFFERENTIATOR_H_

#include "Function.h"
#ifdef __cplusplus
extern "C" {
#endif

double differentiate(Function& f, double x);
double differentiateSQ(double x);

#ifdef __cplusplus
}
#endif

#endif /* DIFFERENTIATOR_H_ */
