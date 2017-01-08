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

import java.util.Date;
import java.util.Locale;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import de.lab4inf.wrb.Differentiator;
import de.lab4inf.wrb.Function;
import de.lab4inf.wrb.Integrator;
import de.lab4inf.wrb.WRBScript;

/**
 * WebService for WRB Function parsing and evaluation.
 *
 * @author nwulff
 * @since 08.12.2016
 * @version $Id: WRBRESTDifferentiationService.java,v 1.3 2017/01/06 17:18:00
 *          nwulff Exp $
 */
@Path(AbstractWRBService.SERVICE)
public class WRBRESTIntegrationService extends AbstractWRBService {

	private final Integrator integrator = new Integrator();

	/**
	 * Calculate the function derivative at point xmin and return the result as
	 * a double.
	 * 
	 * @param fctName
	 *            the name of the function
	 * @param definition
	 *            the script definition of the function f and the x value.
	 * @param fmt
	 *            the format specification for the return value
	 * @return String with the calculated double f'(x)
	 */
	@GET
	@Path(FCT_INTEGRAL_PATH)
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.TEXT_PLAIN)
	public String getFctIntegral(@QueryParam("fct") String fctName,
			@DefaultValue(DEF_FOR_INTEGRAL) @QueryParam("def") String definition,
			@DefaultValue(FMT) @QueryParam("fmt") String fmt) {
		log.info(format("GET Integral PLAIN fct=%s def=%s fmt=%s", fctName, definition, fmt));
		double a = 0, b = 1;
		// boolean returnArray = true;
		String retValue;
		try {
			WRBScript localScript = new WRBScript();
			localScript.parse(definition);
			// x = localScript.getVariable("x"); //Hier kann ncits zurück kommen
			try {
				a = localScript.getVariable("a");
			} catch (Exception e) {
				a = 0;
			}
			try {
				b = localScript.getVariable("b");
			} catch (Exception e) {
				b = 1;
			}

			Function fct = localScript.getFunction(fctName);
			log.info(format("Differential %s(%f,%f) ", fctName, a, b));
			double y = integrator.integrate(fct, a, b);
			retValue = format(Locale.US, fmt, y);

		} catch (Exception e) {
			log.severe(format("%s.%s: %s %f_/%f %s()", getClass().getSimpleName(), "getFctIntegral", e, a, b, fctName));
			retValue = e.toString();
		}
		log.info(format("RET %s", retValue));
		return retValue;
	}

	/**
	 * REST service implementation of the sayHello RMI example. HTML formated.
	 * 
	 * @param message
	 *            received from the client
	 * @return String with the actual time and thread plus message
	 */
	@GET
	@Path(FCT_INTEGRAL_PATH)
	@Produces(TEXT_HTML)
	@Consumes(TEXT_HTML)
	public String getIntegralHtml(@QueryParam("fct") String fctName,
			@DefaultValue(DEF_FOR_INTEGRAL) @QueryParam("def") String definition,
			@DefaultValue(FMT) @QueryParam("fmt") String fmt) {
		Thread t = Thread.currentThread();
		Date d = new Date();
		StringBuilder sb = new StringBuilder();
		sb.append("<!DOCTYPE html><html><head><title>Hello WS</title></head>");
		sb.append("<body><h1>WRB-Service</h1>");
		sb.append(format("Class: %s <br>", this.getClass().getSimpleName()));
		sb.append(format("Thread: %s <br>Time: %s<br>FunctionName: %s<br>def: %s", t, d, fctName,definition));
		sb.append("</body></html>");
		String retValue = sb.toString();
		return retValue;
	}

}
