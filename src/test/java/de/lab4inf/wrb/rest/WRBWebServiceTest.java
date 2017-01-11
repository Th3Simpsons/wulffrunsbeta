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

import static java.lang.String.format;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Locale;

import org.junit.Before;
import org.junit.Test;

import de.lab4inf.wrb.Function;
import de.lab4inf.wrb.Script;
import de.lab4inf.wrb.WRBScript;

/**
 * Test for the WRB function web service.
 *
 * @author nwulff
 * @since  08.12.2016
 * @version $Id: WRBWebServiceTest.java,v 1.2 2017/01/06 17:18:01 nwulff Exp $
 */
public class WRBWebServiceTest extends AbstractWRBServiceTester {
    double xMin = -1.0;
    double xMax = 2.0;
    double deltaX = 0.5;
    boolean useRnd = true;

    /**
     * Return an instance implementing the WRB Script interfacce.
     * @return
     */
    @Override
    protected Script getWRBScript() {
        return new WRBScript();
    }

    /**
     * @throws java.lang.Exception
     */
    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        if (useRnd) {
            xMin = Double.parseDouble(format(Locale.US, "%.3f", Math.random() - 0.5));
            xMax = Double.parseDouble(format(Locale.US, "%.3f", Math.random() + 0.5));
            deltaX = Double.parseDouble(format(Locale.US, "%f", (xMax - xMin) / 2));
            eps = 5.E-4;
        }
    }

    /**
     * Test method for {@link de.lab4inf.wrb.rest.WRBRESTService#getFctValues(java.lang.String, java.lang.String)}.
     */
    @Test(timeout = 1000)
    public void testGetSine() {
        String fctName = "sin";

        String definition = generateFunctionDefinition(fctName, xMin, xMax, deltaX);
        Function fct = getFunction(fctName, definition);
        String responseMsg = submitValueRequest(fctName, definition);
        String expected = expectedValue(fctName, fct, xMin, xMax, deltaX);
        assertEquals(responseMsg, expected, responseMsg);
    }

    /**
     * Test method for {@link de.lab4inf.wrb.rest.WRBRESTService#getFctValues(java.lang.String, java.lang.String)}.
     */
    @Test()
    public void testGetHyperbola() {
        String fctName = "h";

        String definition = generateFunctionDefinition(fctName, "b=1; c=3; h(a)=c/(b+a)", xMin, xMax, deltaX);
        Function fct = getFunction(fctName, definition);
        String responseMsg = submitValueRequest(fctName, definition);
        String expected = expectedValue(fctName, fct, xMin, xMax, deltaX);
        assertEquals(responseMsg, expected, responseMsg);
    }

    /**
     * Test method for {@link de.lab4inf.wrb.rest.WRBRESTService#getFctValues(java.lang.String, java.lang.String)}.
     */
    @Test(timeout = 1000)
    public void testGetCosineFormated() {
        String fmt = "%.6f";
        String fctName = "cos";

        String definition = generateFunctionDefinition(fctName, xMin, xMax, deltaX, fmt);
        Function fct = getFunction(fctName, definition);
        String responseMsg = submitValueRequest(fctName, definition, fmt);
        String expected = expectedValue(fctName, fct, fmt, xMin, xMax, deltaX);
        assertEquals(responseMsg, expected, responseMsg);
    }

    /**
     * Test method for {@link de.lab4inf.wrb.rest.WRBRESTService#getFctValues(java.lang.String, java.lang.String)}.
     */
    @Test(timeout = 1000)
    // @Ignore
    public void testGetTinyFormated() {
        String fmt = "%.2g";
        String fctName = "id";
        String fctBody = "id(x) = x;";
        double xmin = -1.E-5;
        double xmax = 1.E-5;
        double dx = 5.E-6;

        String definition = generateFunctionDefinition(fctName, fctBody, xmin, xmax, dx, fmt);
        Function fct = getFunction(fctName, definition);
        String responseMsg = submitValueRequest(fctName, definition, fmt);
        String expected = expectedValue(fctName, fct, fmt, xmin, xmax, dx);
        assertEquals(responseMsg, expected, responseMsg);
    }

    /**
     * Test method for {@link de.lab4inf.wrb.rest.WRBRESTService#getFctValues(java.lang.String, java.lang.String)}.
     */
    @Test(timeout = 1000)
    public void testFunctionNames() {
        target = target.path(format("%s/%s", AbstractWRBService.SERVICE, AbstractWRBService.FCT_NAMES_PATH));
        // System.out.printf("Target: %s \n", target);
        String responseMsg = target.request().get(String.class);
        // System.out.printf("Response: %s \n", responseMsg);
        assertTrue(responseMsg.length() > 0);
        assertTrue(responseMsg.contains("sin"));
    }

    /**
     * Test method for {@link de.lab4inf.wrb.rest.WRBRESTService#getFctIntegral(java.lang.String, java.lang.String)}.
     */
    @Test(timeout = 1000)
    public void testIntegrateSine() {
        String fctName = "sin";
        eps = 1.E-4;
        String definition = generateIntegralDefinition(fctName, xMin, xMax);
        String responseMsg = submitIntegralRequest(fctName, definition);
        double response = Double.parseDouble(responseMsg.replaceAll(",", "."));
        double expected = Math.cos(xMin) - Math.cos(xMax);
        assertEquals(expected, response, eps);
    }

    @Test(timeout = 1000)
    public void testIntegrateQuad() {
        String fctName = "f";
        String fctBody = "f(x)=x**2";
        eps = 1.E-4;
        String definition = generateIntegralDefinition(fctName, fctBody, xMin, xMax);
        String responseMsg = submitIntegralRequest(fctName, definition);
        double response = Double.parseDouble(responseMsg.replaceAll(",", "."));
        double expected = (Math.pow(xMax, 3) - Math.pow(xMin, 3)) / 3;
        assertEquals(expected, response, eps);
    }

    /**
     * Test method for {@link de.lab4inf.wrb.rest.WRBRESTService#getFctIntegral(java.lang.String, java.lang.String)}.
     */
    @Test(timeout = 1000)
    public void testDifferentiateSine() {
        String fctName = "sin";
        eps = 1.E-4;
        xMin = Math.random();
        String definition = generateDifferentialDefinition(fctName, xMin);
        String responseMsg = submitDifferentialRequest(fctName, definition);
        double response = Double.parseDouble(responseMsg.replaceAll(",", "."));
        double expected = Math.cos(xMin);
        assertEquals(expected, response, eps);
    }

}
