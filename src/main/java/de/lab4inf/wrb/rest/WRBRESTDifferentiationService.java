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
public class WRBRESTDifferentiationService extends AbstractWRBService {
	private final Differentiator differentiator = new Differentiator();
	
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
	@Path(FCT_DIFFERENTIAL_PATH)
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.TEXT_PLAIN)
	public String getFctDifferential(@QueryParam("fct") String fctName,
			@DefaultValue(DEF_ONLY_X) @QueryParam("def") String definition,
			@DefaultValue(FMT) @QueryParam("fmt") String fmt) {
		log.info(format("GET Differential PLAIN fct=%s def=%s fmt=%s", fctName, definition, fmt));
		double x = Double.NaN, xmin = 0, xmax = 1, dx = 1;
		boolean returnArray = true;
		String retValue;
		try {
			WRBScript localScript = new WRBScript();
			localScript.parse(definition);
			// x = localScript.getVariable("x"); //Hier kann ncits zurück kommen
			try {
				xmin = localScript.getVariable("xmin");
				xmax = localScript.getVariable("xmax");
				dx = localScript.getVariable("dx");
				if (dx == 0) {
					throw new IllegalArgumentException();
				}
			} catch (Exception e) {
				returnArray = false;
			}

			if (returnArray) {
				retValue = "{";
				for (x = xmin; x <= xmax; x += dx) {
					Function fct = localScript.getFunction(fctName);
					double y = differentiator.differentiate(fct, x);
					retValue += format(Locale.US, fmt, y) + ((x + dx <= xmax) ? "," : "");
				}

				retValue += "}";

			} else {
				x = localScript.getVariable("x"); //
				Function fct = localScript.getFunction(fctName);
				log.info(format("Differential %s(%f) ", fctName, x));
				double y = differentiator.differentiate(fct, x);
				retValue = format(Locale.US, fmt, y);
			}
		} catch (Exception e) {
			log.severe(format("%s.%s: %s %s'(%f)", getClass().getSimpleName(), "getFctDifferential", e, fctName, x));
			retValue = e.toString();
		}
		log.info(format("RET %s", retValue));
		return retValue;
	}

	@GET
	@Path(FCT_DIFFERENTIAL_PATH)
	@Produces(TEXT_HTML)
	@Consumes(TEXT_HTML)
	public String getDifferentialHtml(@QueryParam("fct") String fctName,
	@DefaultValue(DEF_ONLY_X) @QueryParam("def") String definition,
	@DefaultValue(FMT) @QueryParam("fmt") String fmt) {
		Thread t = Thread.currentThread();
		Date d = new Date();
		StringBuilder sb = new StringBuilder();
		sb.append("<!DOCTYPE html><html><head><title>Hello WS</title></head>");
		sb.append("<body><h1>WRB-Service</h1>");
		sb.append(format("Class: %s <br>", this.getClass().getSimpleName()));
		sb.append(format("Thread: %s <br>Time: %s<br>FunctionName: %s<br>def: %s<br>", t, d, fctName,definition));
		sb.append("Result : " + getDifferentialHtml(fctName, definition, fmt));
		sb.append("</body></html>");
		String retValue = sb.toString();
		return retValue;
	}

}
