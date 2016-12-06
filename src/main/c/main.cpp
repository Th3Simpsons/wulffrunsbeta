/*
 * main.cpp
 *
 *  Created on: 06.12.2016
 *      Author: stefan
 */

#include <jni.h>
#include "Integrator.h"

int main() {
	printf("start\n");
	for (double b = 1; b < 10; b += 1)
	{
		integrateSQ(0,b);
	}

	return 0;
}

