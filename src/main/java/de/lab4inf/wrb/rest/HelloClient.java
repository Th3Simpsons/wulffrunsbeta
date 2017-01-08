/*
 * Project: WRBRest
 *
 * Copyright (c) 2008-2017,  Prof. Dr. Nikolaus Wulff
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

import java.net.URLEncoder;

/**
 * Simple implementation of a REST Client.
 *
 * @author nwulff
 * @since 05.01.2017
 * @version $Id: HelloClient.java,v 1.1 2017/01/06 10:32:06 nwulff Exp $
 */
public class HelloClient extends AbstractWRBClient {
	/**
	 * @param hostUrl
	 */
	public HelloClient(String hostUrl) {
		super(hostUrl);
	}

	public String sayHello(String msg) throws Exception {
		msg = URLEncoder.encode(msg, "UTF-8");
		String path = format("WRBService/Hello?msg=%s", msg);
		return submitRequest(path);
	}

	public static void main(String[] args) {
		HelloClient testHello = new HelloClient("localhost:8080");
		try {
			System.out.println(testHello.sayHello("Gib mir ein Echo"));
		} catch (Exception e) {

			e.printStackTrace();
		}
	}
}
