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

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import de.lab4inf.wrb.Script;

/**
 * Test for the WRB function web service.
 *
 * @author nwulff
 * @since  08.12.2016
 * @version $Id: WRBHelloServiceTest.java,v 1.1 2017/01/06 17:18:01 nwulff Exp $
 */
public class WRBHelloServiceTest extends AbstractWRBServiceTester {
    String msg;
    
    /**
     * Return an instance implementing the WRB Script interfacce. Not used 
     * for the HelloService test but needed for the function evaluations...
     * @return
     */
    @Override
    protected Script getWRBScript() {
        return null;
    }

    /**
     * @throws java.lang.Exception
     */
    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        msg = "Hello World from JUnit Test";
    }

    @Test
    public void testSayHello() throws Exception {
        String responseMsg = submitHelloRequest(msg);
        assertTrue(responseMsg, responseMsg.contains(msg));
        assertTrue(responseMsg, responseMsg.contains("Thread"));
    }
}
