package de.nog;

import de.lab4inf.wrb.Function;

public class Integrator {
	public native double integrate(Function f, double a, double b);
	static {
		System.load("/home/ms/s/so634597/git/wulffrunsbeta/src/main/libHPK_Praktikum4_Cpp");
	}
}
