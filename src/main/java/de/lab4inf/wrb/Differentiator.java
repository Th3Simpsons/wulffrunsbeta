package de.lab4inf.wrb;

public class Differentiator {

	public native double differentiate(Function f, double x);

	static boolean I_AM_AT_HOME = false;
	static {

		if (I_AM_AT_HOME) {
			System.load("/home/stefan/git/wulffrunsbeta/src/main/libHPK_Praktikum4_Cpp");
		} else {
			System.load("/home/ms/s/so634597/git/wulffrunsbeta/src/main/libHPK_Praktikum4_Cpp");
		}

	}
}
