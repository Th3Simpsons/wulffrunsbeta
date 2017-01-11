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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import de.lab4inf.wrb.Function;
import de.lab4inf.wrb.Script;

/**
 * Base implementation for a web serice test.
 *
 * @author nwulff
 * @since  08.12.2016
 * @version $Id: AbstractWRBClientTester.java,v 1.1 2017/01/07 11:03:49 nwulff Exp $
 */
public abstract class AbstractWRBClientTester {
    private static HttpServer server;
    protected double eps = 1.E-8;
    protected Client client;
    protected WebTarget target;
    protected Script script;
    protected Locale locale = Locale.US;

    @BeforeClass
    public static void setUpServer() throws Exception {
        // start the server
        // String baseUrl = "localhost:8080";
        String[] args = { "-h", "geronimo.fritz.box", "-p", "8080" };
        server = WRBServer.startServer(args);
    }

    @Before
    public void setUp() throws Exception {
        // create the script
        script = getWRBScript();
        // create the client
        client = ClientBuilder.newClient();
        target = client.target(WRBServer.BASE_URI);
    }

    /**
     * Return an instance implementing the WRB Script interfacce.
     * @return
     */
    protected abstract Script getWRBScript();

    @After
    public void tearDown() throws Exception {
        client.close();
    }

    @AfterClass
    public static void stopServer() throws Exception {
        if (null != server) {
            WRBServer.stopServer();
            server = null;
        }
    }

    public double roundFmt(double x, String fmt) {
        String sx = String.format(Locale.US, fmt, x);
        double fx = Double.parseDouble(sx);
        return fx;
    }

    /**
     * Checks that the numeric pairs are equal with eps precision.
     * @param expected first pair list
     * @param result   second pair list
     */
    protected <T extends Number> void assertEqualsPairs(List<T> expected, List<T> result) {
        int k = 0;
        assertTrue("size missmatch", expected.size() == result.size());
        for (T p : result) {
            T q = expected.get(k++);
            String debugmsg = format("%s != %s", q, p);
            assertEquals(debugmsg, q.doubleValue(), p.doubleValue(), eps);
        }
    }

    /**
     * Checks that the numeric pairs are equal with eps precision.
     * @param expected first pair list
     * @param result   second pair list
     */
    protected <T extends Number> void assertEqualsPairs(String msg, List<T> expected, List<T> result) {
        int k = 0;
        assertTrue("size missmatch", expected.size() == result.size());
        for (T p : result) {
            T q = expected.get(k++);
            String debugmsg = format("%s: %s != %s", msg, q, p);
            assertEquals(debugmsg, q.doubleValue(), p.doubleValue(), eps);
        }
    }

    protected List<Double> expectedValues(String fctName, Function fct, String fmt, double xmin, double xmax,
            double dx) {
        double x, y;
        List<Double> list = new ArrayList<>();
        for (x = xmin; x < xmax; x += dx) {
            y = fct.eval(x);
            x = roundFmt(x, fmt);
            y = roundFmt(y, fmt);
            list.add(x);
            list.add(y);
        }
        x = xmax;
        y = fct.eval(x);
        x = roundFmt(x, fmt);
        y = roundFmt(y, fmt);
        list.add(x);
        list.add(y);
        return list;
    }

    public String expectedValue(String fctName, Function fct, String fmt, double xmin, double xmax, double dx) {
        List<Double> values = expectedValues(fctName, fct, fmt, xmin, xmax, dx);
        StringBuilder sb = new StringBuilder();
        sb.append(fctName);
        sb.append(": [");
        for (Double p : values) {
            sb.append(format(Locale.US, fmt, roundFmt(p, fmt)));
            sb.append(',');
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append("]");
        return sb.toString();
    }

    protected String expectedValue(String fctName, Function fct, double xmin, double xmax, double dx) {
        return expectedValue(fctName, fct, AbstractWRBService.FMT, xmin, xmax, dx);
    }

    protected String generateFunctionDefinition(String fctName, String fctBody, double xmin, double xmax, double dx,
            String fmt) {
        String smin = format(locale, "%s=" + fmt, "xmin", xmin);
        String smax = format(locale, "%s=" + fmt, "xmax", xmax);
        String sdx = format(locale, "%s=" + fmt, "dx", dx);
        String definition = format("%s;%s;%s;%s", smin, smax, sdx, fctBody);
        return definition;
    }

    protected String generateDifferentialDefinition(String fctName, String fctBody, double x, String fmt) {
        String sx = format(locale, "%s=" + fmt, "x", x);
        String definition = format("%s; %s", sx, fctBody);
        return definition;
    }

    protected String generateDifferentialDefinition(String fctName, String fctBody, double x) {
        return generateDifferentialDefinition(fctName, fctBody, x, AbstractWRBService.FMT);
    }

    protected String generateDifferentialDefinition(String fctName, double xmin) {
        return generateDifferentialDefinition(fctName, "", xmin);
    }

    protected String generateIntegralDefinition(String fctName, String fctBody, double a, double b, String fmt) {
        String smin = format(locale, "%s=" + fmt, "xmin", a);
        String smax = format(locale, "%s=" + fmt, "xmax", b);
        String definition = format("%s; %s; %s", smin, smax, fctBody);
        return definition;
    }

    protected String generateIntegralDefinition(String fctName, String fctBody, double xmin, double xmax) {
        return generateIntegralDefinition(fctName, fctBody, xmin, xmax, AbstractWRBService.FMT);
    }

    protected String generateIntegralDefinition(String fctName, double xmin, double xmax) {
        return generateIntegralDefinition(fctName, "", xmin, xmax);
    }

    protected String generateFunctionDefinition(String fctName, String fctBody, double xmin, double xmax, double dx) {
        return generateFunctionDefinition(fctName, fctBody, xmin, xmax, dx, AbstractWRBService.FMT);
    }

    protected String generateFunctionDefinition(String fctName, double xmin, double xmax, double dx) {
        return generateFunctionDefinition(fctName, "", xmin, xmax, dx);
    }

    protected String generateFunctionDefinition(String fctName, double xmin, double xmax, double dx, String fmt) {
        return generateFunctionDefinition(fctName, "", xmin, xmax, dx, fmt);
    }

    protected String generateFunctionDefinition(String fctName, String fctBody) {
        return generateFunctionDefinition(fctName, fctBody, 0, 1, 0.1);
    }

    protected String generateFunctionDefinition(String fctName) {
        return generateFunctionDefinition(fctName, "");
    }

    protected Function getFunction(String fctName, String definition) {
        //System.out.printf("definition: %s \n", definition);
        try {
            script.parse(definition);
            Function fct = script.getFunction(fctName);
            return fct;
        } catch (Exception e) {
            throw new IllegalArgumentException(fctName, e);
        }
    }

    private String submitRequest(String fctName, String definition, String path, String fmt) {
        target = target.queryParam("def", definition);
        target = target.queryParam("fct", fctName);
        target = target.queryParam("fmt", fmt);
        target = target.path(format("%s/%s", AbstractWRBService.SERVICE, path));
        // System.out.printf("Target: %s \n", target);
        String responseMsg = target.request().accept(AbstractWRBService.TEXT_PLAIN).get(String.class);
        return responseMsg;
    }

    protected String submitValueRequest(String fctName, String definition, String fmt) {
        return submitRequest(fctName, definition, AbstractWRBService.FCT_VALUES_PATH, fmt);
    }

    protected String submitValueRequest(String fctName, String definition) {
        return submitValueRequest(fctName, definition, AbstractWRBService.FMT);
    }

    protected String submitIntegralRequest(String fctName, String definition, String fmt) {
        return submitRequest(fctName, definition, AbstractWRBService.FCT_INTEGRAL_PATH, fmt);
    }

    protected String submitIntegralRequest(String fctName, String definition) {
        return submitIntegralRequest(fctName, definition, AbstractWRBService.FMT);
    }

    protected String submitDifferentialRequest(String fctName, String definition, String fmt) {
        return submitRequest(fctName, definition, AbstractWRBService.FCT_DIFFERENTIAL_PATH, fmt);
    }

    protected String submitDifferentialRequest(String fctName, String definition) {
        return submitDifferentialRequest(fctName, definition, AbstractWRBService.FMT);
    }

}
