package de.lab4inf.wrb;

import static org.junit.Assert.*;

import org.junit.Test;

public class Cpp_LibraryTest {
	private static double EPS = 1.E-2;

	@Test
	public void test() {
		if (Differentiator.I_AM_AT_HOME) {
			System.load("/home/stefan/git/wulffrunsbeta/src/main/libHPK_Praktikum4_Cpp");
		} else {
			System.load("/home/ms/s/so634597/git/wulffrunsbeta/src/main/libHPK_Praktikum4_Cpp");
		}

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

			double y = i.integrate(wrbs.getFunction("x_quadrat"), 0, x);
			assertEquals(x * x * x / (double) 3, y, EPS);
		}
	}

	@Test
	public void drawMeAFuckingTable() {
		Function sq = new Function() {
			@Override
			public double eval(double... args) {
				return args[0] * args[0];
			}
		};
		Function e = new Function() {
			@Override
			public double eval(double... args) {
				return  Math.pow(Math.E,args[0]);
			}
		};
		
		Function sin = new Function() {
			@Override
			public double eval(double... args) {
				return Math.sin(args[0]);
			}
		};
		
		
		
		System.out.println("x^2");
		drawTable(sq);
		System.out.println("e");
		drawTable(e);
		System.out.println("sin");
		drawTable(sin,0,Math.PI,Math.PI/8.0);
		

	}
	private void drawTable(Function f) {
		drawTable(f, 0, 1,0.25);
	}

	private void drawTable(Function f,double a, double b,double step) {
		Differentiator d = new Differentiator();
		Integrator i = new Integrator();
System.out.println("x     f(x)            f'               F+c");
		double dx, ix;

		for (double x = a; x <= b; x += step) {
			dx = d.differentiate(f, x);
			ix = i.integrate(f, 0, x);
			System.out.println(String.format(" %+7.5f | %+7.5f | %+7.5f  | %+7.5f ", x, f.eval(x), dx, ix));
		}
	}

}
