/*
 * Project: WRB
 *
 * Copyright (c) 2008-2013,  Prof. Dr. Nikolaus Wulff
 * University of Applied Sciences, Muenster, Germany
 * Lab for Computer Sciences (Lab4Inf).
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

package de.lab4inf.wrb;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

/**
 * Test of the Wulff RunsBeta-Script language.
 * @author  nwulff
 * @since   16.10.2013
 * @version $Id: VariablesWRBScriptTest.java,v 1.1 2014/10/27 16:49:27 nwulff Exp $
 */
public class VariablesWRBScriptTest {
    final double eps = 1.E-8;
    Script script;
    /**
     * @throws java.lang.Exception
     */
    @Before
    public final void setUp() throws Exception {
        script = getScript();
        assertNotNull("no script implementation",script);
    }
    /**
     * Get the actual implementation for the script test.
     * @return script implementation
     */
    protected Script getScript() {
        return new WRBScript();
    }
    
    /**
     * Test method for {@link de.de.lab4inf.wrb.Script#getVariable(java.lang.String)}.
     */
    @Test(expected=IllegalArgumentException.class)
    public final void testGetUnknownVariable() throws Exception {
        String key = "dummy";
        script.getVariable(key);
    }
    /**
     * Test method for {@link de.de.lab4inf.wrb.Script#setVariable(java.lang.String,double)}.
     * and {@link de.lab4inf.wrb.de.lab4inf.wrb.rest.WRBScript#getVariable(java.lang.String)}.
     */
    @Test
    public final void testSetGetVariable() throws Exception {
        double y,x=2.78;
        String key = "XYZ";
        script.setVariable(key, x);
        y = script.getVariable(key);
        assertEquals(x,y,eps);
    }
    
    @Test
    public final void testAssignWithSemi() throws Exception {
        String task = "x = 4; ";
        assertEquals(4, script.parse(task), eps);
    }

    @Test
    public final void testAssignWithOutSemi() throws Exception {
        String task = "x = 5 ";
        assertEquals(5, script.parse(task), eps);
    }

    @Test
    public final void testAssignSignedVar() throws Exception {
        String task = "x = 5; y= -x; ";
        assertEquals(-5, script.parse(task), eps);
        assertEquals(-5, script.getVariable("y"), eps);
    }


    @Test(expected=IllegalArgumentException.class)
    public final void testMultiAssignWithOutSemi() throws Exception {
        String task = "x = 5 y = x ";
        assertEquals(5, script.parse(task), eps);
    }
    
    @Test
    public final void testSetGetParseVariable() throws Exception {
        double y,x=3;
        String task = "x1=3;Y2=4";
        script.parse(task);
        y = script.getVariable("x1");
        assertEquals(x,y,eps);
        y = script.getVariable("Y2");
        assertEquals(4,y,eps);
    }
    @Test(expected=IllegalArgumentException.class)
    public final void testSetIllegalVariable() throws Exception {
        String task = "1a=3";
        script.parse(task);
    }
    @Test(expected=IllegalArgumentException.class)
    public final void testGetIllegalVariable() throws Exception {
        String task = "x=3; y =4x+3";
        script.parse(task);
    }    
}
