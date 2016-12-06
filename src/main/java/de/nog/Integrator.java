package de.nog;

import de.lab4inf.wrb.Function;

public class Integrator {
	public native double integrate(Function f, double a, double b);
}
