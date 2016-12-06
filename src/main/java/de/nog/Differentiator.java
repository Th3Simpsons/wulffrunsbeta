package de.nog;

import de.lab4inf.wrb.Function;

public class Differentiator {

	public native double differentiate(Function f, double x);

	static {
		System.load("/home/stefan/git/wulffrunsbeta/src/main/libHPK_Praktikum4_Cpp");
	}
}
