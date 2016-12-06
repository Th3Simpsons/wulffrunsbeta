package de.lab4inf.wrb;

import static org.junit.Assert.*;

import org.junit.Test;

import de.nog.Differentiator;
import de.nog.Integrator;
import de.nog.WRBScript;

public class Cpp_LibraryTest {
	private static double EPS = 1.E-2;

	@Test
	public void test() {
		System.load("/home/stefan/git/wulffrunsbeta/src/main/libHPK_Praktikum4_Cpp");
	}

	@Test
	public void test_differentiate() {
		WRBScript wrbs = new WRBScript();
		wrbs.observer.functions.put("x_quadrat", new Function() {

			@Override
			public double eval(double... args) {
				return args[0] * args[0];
			}
		});

		Differentiator d = new Differentiator();
		for (double x = 2; x < 10; x += 1) {

			double y = d.differentiate(wrbs.getFunction("x_quadrat"), x);
			assertEquals(2 * x, y, EPS);
		}
	}

	@Test
	public void test_integrate() {
		WRBScript wrbs = new WRBScript();
		wrbs.observer.functions.put("x_quadrat", new Function() {

			@Override
			public double eval(double... args) {
				return args[0] * args[0];
			}
		});

		Integrator i = new Integrator();
		for (double x = 1; x < 10; x += 1) {

			double y = i.integrate(wrbs.getFunction("x_quadrat"),0, x);
			assertEquals(x * x * x / (double) 3, y, EPS);
		}
	}

}
