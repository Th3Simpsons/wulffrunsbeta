/*
 * Project: WRB
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

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.lang.reflect.Constructor;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.logging.Logger;

/**
 * WRBClient to handle the WS conservation to the WRB web service.
 *
 * @author nwulff
 * @since 08.12.2016
 * @version $Id: AbstractWRBClient.java,v 1.1 2017/01/06 10:32:06 nwulff Exp $
 */
public abstract class AbstractWRBClient {
	public static final String SERVICE = "WRBService";
	public static final String TEXT_PLAIN = "text/plain";
	public static final String HELLO_PATH = "Hello";
	public static final String FCT_NAMES = "fctNames";
	public static final String FCT_VALUES = "evaluate";
	public static final String FCT_INTEGRAL = "integrate";
	public static final String FCT_DIFFERENTIAL = "differentiate";
	public static final String INTEGRATE = format("%s/%s", SERVICE, FCT_INTEGRAL);
	public static final String DIFFERENTIATE = format("%s/%s", SERVICE, FCT_DIFFERENTIAL);
	public static final String VALUES = format("%s/%s", SERVICE, FCT_VALUES);
	public static final String NAMES = format("%s/%s", SERVICE, FCT_NAMES);
	protected Logger log = Logger.getLogger(AbstractWRBClient.SERVICE);
	protected final String host;

	/**
	 * Constructor binding to the given host, using default range parameters.
	 * 
	 * @param hostUrl
	 *            the host to call
	 */
	public AbstractWRBClient(String hostUrl) {
		if (!hostUrl.endsWith("/")) {
			hostUrl = format("%s/", hostUrl);
		}
		if (hostUrl.startsWith("http:")) {
			host = hostUrl;
		} else {
			host = String.format("http://%s", hostUrl);
		}
	}

	protected final String submitRequest(String fctName, String path, String definition, String accept, String format)
			throws Exception {
		definition = URLEncoder.encode(definition, "UTF-8");
		format = URLEncoder.encode(format, "UTF-8");
		String service = format("%s?fct=%s&def=%s&fmt=%s \r\n", path, fctName, definition, format);
		return submitRequest(service, accept);
	}

	protected final String submitRequest(String service) throws Exception {
		return submitRequest(service, TEXT_PLAIN);
	}

	protected final String submitRequest(String service, String accept) throws Exception {
		StringBuffer ret = new StringBuffer();
		LineNumberReader reader = getResponse(service, accept);
		String line;
		while ((line = reader.readLine()) != null) {
			ret.append(line);
		}
		String responseMsg = ret.toString();
		return responseMsg;
	}

	protected final LineNumberReader getResponse(String path) throws IOException {
		return getResponse(path, TEXT_PLAIN);
	}

	protected final LineNumberReader getResponse(String path, String accept) throws IOException {
		String hostUrl = format("%s%s", host, path);
		URL url = new URL(hostUrl);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestProperty("User-Agent", getClass().getSimpleName());
		connection.setRequestProperty("Accept", accept);
		connection.setRequestProperty("Accept-Language", "en-US");
		connection.setRequestMethod("GET");
		connection.connect();
		int errCode = connection.getResponseCode();
		if (errCode == HttpURLConnection.HTTP_OK) {
			InputStream in = connection.getInputStream();
			LineNumberReader reader = new LineNumberReader(new InputStreamReader(in));
			return reader;
		}
		throw new IOException(format("returned %d:%s", errCode, connection.getResponseMessage()));
	}

	protected String escapeHTTP(String in) {
		String out = in;
		out = in.replace("%", "%25");
		out = out.replace(" ", "%20");
		out = out.replace("^", "%5E");
		out = out.replace("+", "%2B");
		out = out.replace("-", "%2D");
		out = out.replace("*", "%2A");
		out = out.replace("/", "%2F");
		
		
		
		return out;
	}

	protected final Exception toException(String res) throws Exception {
		int t = res.indexOf(':');
		String type = res.substring(0, t);
		String msg = res.substring(t + 1);
		@SuppressWarnings("unchecked")
		Class<Exception> clazz = (Class<Exception>) Class.forName(type);
		Constructor<Exception> ctor = clazz.getConstructor(String.class);
		Exception exp = ctor.newInstance(msg);
		return exp;
	}
}
