/*
 * Project: WRBRest
 *
 * Copyright (c) 2008-2016,  Prof. Dr. Nikolaus Wulff
 * University of Applied Sciences, Muenster, Germany
 * Lab for computer sciences (Lab4Inf).
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

package de.lab4inf.wrb.rest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import de.lab4inf.wrb.Function;
import de.lab4inf.wrb.Script;
import de.lab4inf.wrb.WRBScript;

/**
 * Document me!
 *
 * @author nwulff
 * @since  08.12.2016
 * @version $Id: WRBClientTest.java,v 1.11 2017/01/07 11:03:49 nwulff Exp $
 * @param  <T> type if generic.
 */
public class WRBClientTest extends AbstractWRBClientTester {
    WRBClient wrbClient;

    /* (non-Javadoc)
     * @see de.lab4inf.wrb.rest.WebServiceTester#setUp()
     */
    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        wrbClient = new WRBClient(WRBServer.BASE_URI);
    }

    protected void setRange(double xmin, double xmax, double dx) {
        wrbClient.setXmin(xmin);
        wrbClient.setXmax(xmax);
        wrbClient.setDeltaX(dx);
    }

    /* (non-Javadoc)
     * @see de.lab4inf.wrb.rest.WebServiceTester#getWRBScript()
     */
    @Override
    protected Script getWRBScript() {
        return new WRBScript();
    }

    /**
     * Test method for {@link de.lab4inf.wrb.rest.WRBClient#getXYValue(java.lang.String, java.lang.String, java.lang.String)}.
     */
    @Test
    public void testGetScriptXYValue() throws Exception {
        String fmt = "%.2g";
        String fctName = "id";
        String fctBody = "id(x) = x;";
        double xmin = 0;
        double xmax = 1;
        double dx = 0.25;
        eps = 1.E-2;
        setRange(xmin, xmax, dx);
        String definition = generateFunctionDefinition(fctName, fctBody, xmin, xmax, dx, fmt);
        Function fct = getFunction(fctName, definition);
        List<Double> expected = expectedValues(fctName, fct, fmt, xmin, xmax, dx);
        List<Double> result = wrbClient.getXYValue(fctName, definition, fmt);
        assertEquals(expected, result);
    }

    /**
     * Test method for {@link de.lab4inf.wrb.rest.WRBClient#getXYValue(java.lang.String, java.lang.String, java.lang.String)}.
     */
    @Test
    public void testGetSineXYValue() throws Exception {
        String fmt = "%.5g";
        String fctName = "sin";
        String fctBody = "";
        double xmin = 0;
        double xmax = Math.PI;
        double dx = xmax / 6;
        eps = 5.E-5;

        setRange(xmin, xmax, dx);
        String definition = generateFunctionDefinition(fctName, fctBody, xmin, xmax, dx, fmt);
        Function fct = getFunction(fctName, definition);
        List<Double> expected = expectedValues(fctName, fct, fmt, xmin, xmax, dx);
        List<Double> result = wrbClient.getXYValue(fctName, definition, fmt);
        assertEqualsPairs(expected, result);
    }

    @Test
    public void testFunctionNames() throws Exception {
        String fmt = "%.5g";
        String fctName;
        String fctBody = "";
        double xmin = Math.PI / 64;
        double xmax = Math.PI / 4;
        double dx = xmax / 3;
        eps = 5.E-4;
        Set<String> names = wrbClient.getFunctionNames();
        assertTrue(names.size() > 0);
        for (String name : names) {
            fctName = name;
            if (name.contains("pow"))
                break;
            // System.out.printf("FCT %s ", name);
            String definition = generateFunctionDefinition(fctName, fctBody, xmin, xmax, dx, fmt);
            // System.out.printf("DEF %s \n", definition);
            Function fct = getFunction(fctName, definition);
            List<Double> expected = expectedValues(fctName, fct, fmt, xmin, xmax, dx);
            List<Double> result = wrbClient.getXYValue(fctName, definition, fmt);
            // System.out.printf("EXP %s \n", expected);
            // System.out.printf("RET %s \n", result);
            assertEqualsPairs(expected, result);
        }
    }

    @Test
    public void testAddFunction() throws Exception {
        double xmin = -1;
        double xmax = 1;
        double dx = 0.25;
        String fctName = "parabel";
        String body = "parabel(q)=q*q-1";
        wrbClient.addFunction(fctName, body);
        setRange(xmin, xmax, dx);
        List<Double> expected = new ArrayList<>();
        for (double x = xmin; x <= xmax; x += dx) {
            expected.add(x);
            expected.add(x * x - 1);
        }
        List<Double> result = wrbClient.getValues(fctName);
        assertEqualsPairs(expected, result);
    }

    @Test
    public void testCircle() throws Exception {
        double xmin = -1;
        double xmax = 1;
        double dx = 0.25;
        String fctName = "circ";
        String body = "circ(q)=sin(q)**2+cos(q)**2";
        wrbClient.addFunction(fctName, body);
        setRange(xmin, xmax, dx);
        List<Double> expected = new ArrayList<>();
        for (double x = xmin; x <= xmax; x += dx) {
            expected.add(x);
            expected.add(1.0);
        }
        List<Double> result = wrbClient.getValues(fctName);
        assertEqualsPairs(expected, result);
    }

    @Test
    public void testIdSin() throws Exception {
        double xmin = -1;
        double xmax = 1;
        double dx = 0.25;
        String fctName = "id";
        String body = "id(y)=asin(sin(y))";
        wrbClient.addFunction(fctName, body);
        setRange(xmin, xmax, dx);
        List<Double> expected = new ArrayList<>();
        for (double x = xmin; x <= xmax; x += dx) {
            expected.add(x);
            expected.add(x);
        }
        List<Double> result = wrbClient.getValues(fctName);
        assertEqualsPairs(expected, result);
    }

    @Test
    public void testDiffParabel() throws Exception {
        String fctName = "parabel";
        String body = "parabel(q)=q*q-1";
        wrbClient.addFunction(fctName, body);
        eps = 1.E-4;
        double x = Math.random();
        double expected = 2 * x;
        double result = wrbClient.differentiate(fctName, x);
        assertEquals(expected, result, eps);
    }

    @Test
    public void testDiffSine() throws Exception {
        String fctName = "sin";
        eps = 1.E-4;
        double x = Math.random();
        double expected = Math.cos(x);
        double result = wrbClient.differentiate(fctName, x);
        assertEquals(expected, result, eps);
    }

    @Test
    public void testIntegrateParabel() throws Exception {
        String fctName = "parabel";
        String body = "parabel(q)=q*q-1";
        wrbClient.addFunction(fctName, body);
        eps = 1.E-4;
        double scale = 1.E5;
        double x = Math.random();
        x = Math.round(scale * x) / scale;
        double a = x - 1;
        double b = x + 1;
        double expected = Math.pow(b, 3) / 3 - b;
        expected -= Math.pow(a, 3) / 3 - a;
        double result = wrbClient.integrate(fctName, a, b);
        assertEquals(expected, result, eps);
    }

    @Test
    public void testIntegrateSine() throws Exception {
        String fctName = "sin";
        eps = 1.E-4;
        double scale = 1.E5;
        double x = Math.round(scale * Math.random()) / scale;
        double a = x - 1;
        double b = x + 1;
        double expected = Math.cos(a) - Math.cos(b);
        double result = wrbClient.integrate(fctName, a, b);
        assertEquals(expected, result, eps);
    }

}
