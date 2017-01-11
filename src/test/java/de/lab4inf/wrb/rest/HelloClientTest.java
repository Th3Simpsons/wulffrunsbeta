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

import static org.junit.Assert.assertTrue;

import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Test of the REST HelloClient.
 *
 * @author nwulff
 * @since  05.01.2017
 * @version $Id: HelloClientTest.java,v 1.2 2017/01/07 11:03:49 nwulff Exp $
 */
public class HelloClientTest {
    HelloClient client;
    static HttpServer server;
    @BeforeClass
    public static void setUpServer() throws Exception {
        // start the server
        String[] args = { "-h", "localhost", "-p", "8080" };
        // String[] args = { "-h", "geronimo.fritz.box", "-p", "8080" };
        server = WRBServer.startServer(args);
    }

    @AfterClass
    public static void stopServer() throws Exception {
        if (null!=server) {
            WRBServer.stopServer();
            server = null;
        }
    }

    @Before
    public void setUp() throws Exception {
        String hostUrl = WRBServer.BASE_URI;

        client = new HelloClient(hostUrl);
    }


    /**
     * Test method for {@link de.lab4inf.wrb.rest.HelloClient#sayHello(java.lang.String)}.
     */
    @Test
    public void testSayHello() throws Exception {
        String msg = "Hello World";
        String ret = client.sayHello(msg);
        assertTrue(ret, ret.contains(msg));
    }


}
