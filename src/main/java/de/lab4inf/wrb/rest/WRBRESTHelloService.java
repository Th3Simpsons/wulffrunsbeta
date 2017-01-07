/*
 * Project: WRB4
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

import java.util.Date;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

/**
 * The sayHello example as REST Serice.
 *
 * @author nwulff
 * @since  05.01.2017
 * @version $Id: WRBRESTHelloService.java,v 1.1 2017/01/06 17:18:00 nwulff Exp $
 */
@Path(AbstractWRBService.SERVICE)
public class WRBRESTHelloService extends AbstractWRBService {
    /**
     * REST service implementation of the sayHello RMI example. Plain text formated.
     * @param message received from the client
     * @return String with the actual time and thread plus message 
     */
    @GET
    @Path(HELLO_PATH)
    @Produces(TEXT_PLAIN)
    @Consumes(TEXT_PLAIN)
    public String sayHello(@DefaultValue("Hello World") @QueryParam("msg") String message) {
        Thread t = Thread.currentThread();
        Date d = new Date();
        log.info(format("GET %s PLAIN message=%s Thread: %s Time: %s", HELLO_PATH, message, t, d));
        String retValue = format("%s %s %s", t, d, message);
        log.info(format("RET %s", retValue));
        return retValue;
    }

    /**
     * REST service implementation of the sayHello RMI example. HTML formated.
     * @param message received from the client
     * @return String with the actual time and thread plus message 
     */
    @GET
    @Path(HELLO_PATH)
    @Produces(TEXT_HTML)
    @Consumes(TEXT_HTML)
    public String sayHelloHtml(@DefaultValue("Hello World") @QueryParam("msg") String message) {
        Thread t = Thread.currentThread();
        Date d = new Date();
        StringBuilder sb = new StringBuilder();
        sb.append("<!DOCTYPE html><html><head><title>Hello WS</title></head>");
        sb.append("<body><h1>HelloService</h1>");
        sb.append(format("Class: %s <br>", this.getClass().getSimpleName()));
        sb.append(format("Thread: %s <br>Time: %s<br>Message: %s", t, d, message));
        sb.append("</body></html>");
        String retValue = sb.toString();
        return retValue;
    }

}
