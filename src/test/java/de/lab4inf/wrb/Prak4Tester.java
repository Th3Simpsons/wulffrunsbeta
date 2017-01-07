/*
 * Project: WRB
 *
 * Copyright (c) 2008-2013,  Prof. Dr. Nikolaus Wulff
 * University of Applied Sciences, Muenster, Germany
 * Lab for Computer Sciences (Lab4Inf).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package de.lab4inf.wrb;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Abstract base test of the Differentiator and the Integrator implementations.
 * To execute extend this class and implement the getScript method as in Lab 2.
 * 
 * @author nwulff
 * @since 28.11.2013
 * @version $Id: Prak4Tester.java,v 1.2 2014/12/01 09:41:57 nwulff Exp $
 */
public abstract class Prak4Tester {
    static final String           DIFFERENTIATOR       = "de.nog.Differentiator";
    static final String           INTEGRATOR           = "de.nog.Integrator";
    static final double           EPS                  = 5.E-7;                          // relaxed tolerance...
    static final int              CALLS_DIFFERENTIATOR = 32;                             // maximal calls for differentiation
    static final int              CALLS_INTEGRATOR     = 4096;                           // maximal calls for integration
    protected double              eps                  = EPS;
    private Script                script;
    protected int                 calls;
    private static Differentiator diff;
    private static Integrator     intg;

    /**
     * Construct the Differentiator and Integrator via Reflection.
     * This checks:</br>
     * a) the package and class names are as wanted.
     * b) the native C library could be instantiated.
     * 
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static final void checkNatives() throws Exception {
        Function fct = new DummyFunction();
        Class<?> clazz = null;
        String className = null;
        // construct the Differentiator class and instance
        try {
            className = DIFFERENTIATOR;
            clazz = Class.forName(className);
            diff = (Differentiator) clazz.newInstance();
        } catch (Throwable ignore) {
            ignore.printStackTrace();
        }
        assertNotNull("no Differentiator " + className, diff);

        // construct the Integrator class and instance
        try {
            clazz = null;
            className = INTEGRATOR;
            clazz = Class.forName(className);
            intg = (Integrator) clazz.newInstance();
        } catch (Throwable ignore) {
            ignore.printStackTrace();
        };
        assertNotNull("no Integrator " + className, intg);

        // dummy calls to see if native functions are found
        diff.differentiate(fct, 0);
        intg.integrate(fct, 0, 1);
    }

    /**
     * @throws java.lang.Exception
     */
    @Before
    public final void setUp() throws Exception {
        calls = 0;
        script = getScript();
        assertNotNull("no script implementation", script);
    }

    /**
     * Get the actual implementation for the script test.
     * 
     * @return script implementation
     */
    protected abstract Script getScript();

    /**
     * Calculate the relative or absolute tolerated error.
     * 
     * @param f the true function value to check for
     * @return the maximal tolerance
     */
    protected final double rEps(final double f) {
        double a = Math.abs(f), rEps = EPS;
        if (a > 1)
            rEps *= a;
        return rEps;
    }

    @Test(timeout = 1000)
    public void testDifferentiatePoly2() {
        Poly p = new Poly(1, 2, 3);
        Poly dP = new Poly(2, 6);
        double x, y, z;

        for (x = 0; x < 4; x += 0.25) {
            calls = 0;
            y = diff.differentiate(p, x);
            z = dP.eval(x);
            assertEquals(z, y, rEps(z));
            assertTrue("too many function calls " + calls,
                    calls <= CALLS_DIFFERENTIATOR);
        }
    }

    @Test(timeout = 1000)
    public void testDifferentiatePoly3() {
        Poly p = new Poly(1, 2, 3, 4);
        Poly dP = new Poly(2, 6, 12);
        double x, y, z;

        for (x = 0; x < 4; x += 0.25) {
            z = dP.eval(x);
            calls = 0;
            y = diff.differentiate(p, x);
            //y=2+3*2*x+4*3*x*x;
            assertEquals(z, y, rEps(z));
            assertTrue("too many function calls " + calls,
                    calls <= CALLS_DIFFERENTIATOR);
        }
    }

    @Test(timeout = 1000)
    public void testDifferentiatePoly4() {
        Poly p = new Poly(1, 2, 3, 4, 5);
        Poly dP = new Poly(2, 6, 12, 20);
        double x, y, z;

        for (x = 0; x < 4; x += 0.25) {
            z = dP.eval(x);
            calls = 0;
            y = diff.differentiate(p, x);
            assertEquals(z, y, rEps(z));
            assertTrue("too many function calls " + calls,
                    calls <= CALLS_DIFFERENTIATOR);
        }
    }

    @Test(timeout = 1000)
    public void testIntegratePoly1() {
        Poly p = new Poly(1, 2);
        Poly iP = new Poly(0, 1, 1);
        double a = 1, b = 5, x, y, z;

        for (x = a; x < b; x += 0.25) {
            z = iP.eval(x) - iP.eval(a);
            calls = 0;
            y = intg.integrate(p, a, x);
            assertEquals(z, y, rEps(z));
            assertTrue("too many function calls " + calls, calls <= 128);
        }
    }

    @Test(timeout = 1000)
    public void testIntegratePoly2() {
        Poly p = new Poly(1, 2, 3);
        Poly iP = new Poly(0, 1, 1, 1);
        double a = 1, b = 5, x, y, z;

        for (x = a; x < b; x += 0.25) {
            z = iP.eval(x) - iP.eval(a);
            calls = 0;
            y = intg.integrate(p, a, x);
            assertEquals(z, y, rEps(z));
            assertTrue("too many function calls " + calls, calls <= 128);
        }
    }

    @Test(timeout = 1000)
    public void testIntegratePoly3() {
        Poly p = new Poly(1, 1, 1, 1);
        Poly iP = new Poly(4711, 1, 0.5, 1. / 3, 0.25);
        double a = 1, b = 5, x, y, z;

        for (x = a; x < b; x += 0.25) {
            z = iP.eval(x) - iP.eval(a);
            calls = 0;
            y = intg.integrate(p, a, x);
            assertEquals(z, y, rEps(z));
            assertTrue("too many function calls " + calls,
                    calls <= CALLS_INTEGRATOR);
        }
    }

    @Test(timeout = 1000)
    public void testIntegratePoly4() {
        Poly p = new Poly(1, 2, 3, 4, 5);
        Poly iP = new Poly(0, 1, 1, 1, 1, 1);
        double a = 0, b = 3, x, y, z;

        for (x = a; x < b; x += 0.25) {
            z = iP.eval(x) - iP.eval(a);
            calls = 0;
            y = intg.integrate(p, a, x);
            assertEquals(z, y, rEps(z));
            assertTrue("too many function calls " + calls,
                    calls <= CALLS_INTEGRATOR);
        }
    }

    @Test(timeout = 1000)
    public void testDifferentiateThrowsException() {
        double y, x = 1;
        try {
            Function f = new Crazy();
            y = diff.differentiate(f, x);
            Assert.fail("not exception thrown");
        } catch (IllegalStateException error) {
            String msg = error.getMessage();
            assertTrue(msg, msg.contains("no convergence"));
        }
    }

    @Test(timeout = 1000)
    public void testIntegratorThrowsException() {
        double y, a = 0, b = 1;
        try {
            Function f = new Crazy();
            y = intg.integrate(f, a, b);
            Assert.fail("not exception thrown");
        } catch (IllegalStateException error) {
            String msg = error.getMessage();
            // error.printStackTrace();
            assertTrue(msg, msg.contains("no convergence"));
        }
    }

    @Test(timeout = 1000)
    public void testDifferentiateHyperbola() {
        double a = 0.1, b = 3, x, y, z;
        Function hyp = new Hyperbola();
        for (x = a; x < b; x += 0.25) {
            z = -1 / (x * x);
            calls = 0;
            y = diff.differentiate(hyp, x);
            assertEquals(z, y, rEps(z));
            assertTrue("too many function calls " + calls,
                    calls <= CALLS_DIFFERENTIATOR);
        }
    }

    @Test(timeout = 1000)
    public void testDifferentiateSineScript() throws Exception {
        double a = 4, b = 2, x, y, z;
        String def = String.format("b=%.0f; f(u)=sin(b*u);", b);
        script.parse(def);
        Function f = script.getFunction("f");
        for (x = b + 0.2; x < a; x += 0.25) {
            z = b * Math.cos(b * x);
            y = diff.differentiate(f, x);
            assertEquals(z, y, rEps(z));
        }
    }

    @Test(timeout = 1000)
    public void testIntegrateHyperbola() {
        double a = 1, b = 3, x, y, z;
        Function hyp = new Hyperbola();
        for (x = a; x < b; x += 0.25) {
            z = Math.log(x / a);
            calls = 0;
            y = intg.integrate(hyp, a, x);
            assertEquals(z, y, rEps(z));
            assertTrue("too many function calls " + calls,
                    calls <= CALLS_INTEGRATOR);
        }
    }

    @Test(timeout = 1000)
    public void testIntegrateHyperbolaScript() throws Exception {
        double a = 4, b = 2, x, y, z;
        String def = String.format("b=%.0f; g(u)=1/(u-b);", b);
        script.parse(def);
        Function hyp = script.getFunction("g");
        for (x = b + 0.2; x < a; x += 0.25) {
            z = Math.log((x - b) / (a - b));
            y = intg.integrate(hyp, a, x);
            assertEquals(z, y, rEps(z));
        }
    }

    @Test(timeout = 1000)
    public void testIntegrateSineScript() throws Exception {
        double a = 4, b = 2, cosab, x, y, z;
        String def = String.format("b=%.0f; f(u)=sin(b*u);", b);
        script.parse(def);
        Function f = script.getFunction("f");
        cosab = Math.cos(a * b);
        for (x = b + 0.2; x < a; x += 0.25) {
            z = (cosab - Math.cos(b * x)) / b;
            y = intg.integrate(f, a, x);
            assertEquals(z, y, rEps(z));
        }
    }

    @Test(timeout = 1000)
    public void testDifferentiateScriptFct() throws Exception {
        double a = 0.1, b = 3, x, y, z;
        String def = "a=-1;y(z)=z**4 +a*z**2 + 1/z; g(v)=y(v)";
        script.parse(def);
        Function fct = script.getFunction("g");
        for (x = a; x < b; x += 0.25) {
            z = (4 * x * x - 2) * x - 1 / (x * x);
            y = diff.differentiate(fct, x);
            assertEquals(z, y, rEps(z));
        }
    }

    @Test(timeout = 1000)
    public void testIntegrateScriptFct() throws Exception {
        double a = 0.1, b = 3, za, x, y, z;
        String def = "c=1; foo(u)=u**4 - u**2 + c/u; y(x)=x*x";
        script.parse(def);
        Function hyp = script.getFunction("foo");
        za = Math.pow(a, 5) / 5 - Math.pow(a, 3) / 3 + Math.log(a);
        for (x = a; x < b; x += 0.25) {
            z = (x * x * x / 5 - x / 3) * x * x + Math.log(x) - za;
            y = intg.integrate(hyp, a, x);
            assertEquals(z, y, rEps(z));
        }
    }

    /**
     * Base class for test functions, incrementing the internal calls counter.
     */
    abstract class JUnitTestFunction implements Function {

        /* (non-Javadoc)
         * @see de.lab4inf.wrb.Function#eval(double[])
         */
        @Override
        public double eval(double... args) {
            calls++;
            return value(args[0]);
        }

        protected abstract double value(final double x);
    }

    /**
     * Hyperbola function y=1/x.
     */
    class Hyperbola extends JUnitTestFunction {
        //@Override
        @Override
        public double value(double x) {
            return 1 / x;
        }

    }

    /**
     * Simple polynomial function.
     */
    class Poly extends JUnitTestFunction {
        private final double[] a;
        private final int      n;

        public Poly(final double... coeff) {
            a = coeff;
            n = a.length;
        }

        //@Override
        @Override
        public double value(double x) {
            double y = a[n - 1];
            for (int k = n - 2; k >= 0; k--) {
                y = y * x + a[k];
            }
            return y;
        }
    }

    /**
     * Dummy function just to see if Differentiator and Integrator are loaded.
     */
    static class DummyFunction implements Function {
        /* (non-Javadoc)
         * @see de.lab4inf.wrb.Function#eval(double[])
         */
        @Override
        public double eval(double... args) {
            return 0;
        }
    }

    /**
     * Crazy random function to see if Differentiator and Integrator throw
     * exceptions.
     */
    class Crazy extends JUnitTestFunction {
        /* (non-Javadoc)
         * @see de.lab4inf.wrb.Prak4Tester.JUnitTestFunction#value(double)
         */
        @Override
        protected double value(double x) {
            return Math.random();
        }
    }
}
