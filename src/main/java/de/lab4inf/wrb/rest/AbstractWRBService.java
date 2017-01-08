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

import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.core.MediaType;

/**
 * Package protected abstract WebService for WRB Function parsing and
 * evaluation.
 *
 * @author nwulff
 * @since 08.12.2016
 * @version $Id: AbstractWRBService.java,v 1.2 2017/01/06 17:18:01 nwulff Exp $
 */
abstract class AbstractWRBService {
	public static final String SERVICE = "WRBService";
	public static final String HELLO_PATH = "Hello";
	public static final String FCT_NAMES_PATH = "fctNames";
	public static final String FCT_VALUES_PATH = "evaluate";
	public static final String FCT_INTEGRAL_PATH = "integrate";
	public static final String FCT_DIFFERENTIAL_PATH = "differentiate";
	public static final String TEXT_PLAIN = MediaType.TEXT_PLAIN;
	public static final String TEXT_HTML = MediaType.TEXT_HTML;
	public static final String APPLICATION_JSON = "application/vnd.api+json";
	public static final String DEF = "xmin=0; xmax=1; dx=0.2;";
	public static final String DEF_ONLY_X = "x=0;";
	public static final String DEF_FOR_INTEGRAL = "a=0; b=1;";
	public static final String FMT = "%.6f";
	protected final static Locale LOCALE = Locale.US;
	protected final Logger log = Logger.getLogger(SERVICE);

	AbstractWRBService() {
		log.setLevel(Level.WARNING);
	}

	/**
	 * Append the given double value to the StringBuilder instance using the
	 * format specifier.
	 * 
	 * @param sb
	 *            StringBuilder to use
	 * @param fmt
	 *            format specifier to use
	 * @param x
	 *            the x value to append
	 * @return the StringBuild filled with the given values.
	 */
	protected final StringBuilder append(final StringBuilder sb, String fmt, double x) {
		return sb.append(String.format(LOCALE, fmt, x));
	}

	/**
	 * Append the given double values to the StringBuilder instance using the
	 * format specifier.
	 * 
	 * @param sb
	 *            StringBuilder to use
	 * @param fmt
	 *            format specifier to use
	 * @param x
	 *            the x value to append
	 * @param y
	 *            the y value to append
	 * @return the StringBuild filled with the given values.
	 */
	protected final StringBuilder append(final StringBuilder sb, String fmt, double x, double y, boolean last) {
		StringBuilder b = append(sb, fmt, x);
		b.append(',');
		b = append(b, fmt, y);
		if (!last)
			b.append(',');
		return b;
	}

	/**
	 * Append the given double values to the StringBuilder instance using the
	 * format specifier.
	 * 
	 * @param sb
	 *            StringBuilder to use
	 * @param fmt
	 *            format specifier to use
	 * @param x
	 *            the x value to append
	 * @param y
	 *            the y value to append
	 * @return the StringBuild filled with the given values.
	 */
	protected final StringBuilder append(final StringBuilder sb, String fmt, double x, double y) {
		return append(sb, fmt, x, y, false);
	}
}
