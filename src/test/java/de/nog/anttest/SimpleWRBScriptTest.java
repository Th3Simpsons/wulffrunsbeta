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

package de.nog.anttest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import de.lab4inf.wrb.WRBScript;


/**
 * Test of the Wulff RunsBeta-Script language.
 * 
 * @author nwulff
 * @since 16.10.2013
 * @version $Id: SimpleWRBScriptTest.java,v 1.1 2014/10/27 16:49:27 nwulff Exp $
 */
public class SimpleWRBScriptTest {
	final double eps = 1.E-8;
	WRBScript script;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public final void setUp() throws Exception {
		script = new WRBScript();
		assertNotNull("no script implementation", script);
	}

	/**
	 * Get the actual implementation for the script test.
	 * 
	 * @return script implementation
	 */
	protected WRBScript getScript() {
		return new WRBScript();
	}

	/**
	 * Test if not null script.
	 */
	@Test
	public final void testScriptNotNull() throws Exception {
		assertNotNull(script);
	}

	/**
	 * Test method for {@link de.lab4inf.wrb.Script#getVariable(java.lang.String)}.
	 */
	@Test(expected = IllegalArgumentException.class)
	public final void testGetUnknownVariable() throws Exception {
		String key = "dummy";
		script.getVariable(key);
	}

	/**
	 * Test method for
	 * {@link de.lab4inf.wrb.Script#setVariable(java.lang.String,double)}. and
	 * {@link de.lab4inf.wrb.de.lab4inf.wrb.rest.WRBScript#getVariable(java.lang.String)}.
	 */
	@Test
	public final void testSetGetVariable() throws Exception {
		double y, x = 2.78;
		String key = "XYZ";
		script.setVariable(key, x);
		y = script.getVariable(key);
		assertEquals(x, y, eps);
	}

	/**
	 * Test method for {@link de.lab4inf.wrb.Script#parse(java.lang.String)}. Testing
	 * some very simple operation. More to come...
	 */

	@Test
	public final void testPlus() throws Exception {
		String task = "2+3";
		assertEquals(5.0, script.parse(task), eps);
	}

	@Test
	public final void testMinus() throws Exception {
		String task = "2 - 6";
		assertEquals(-4.0, script.parse(task), eps);
	}

	@Test
	public final void testConstant() throws Exception {
		String task = "0815; 4711;";
		assertEquals(4711.0, script.parse(task), eps);
	}

	@Test
	public final void testSigned() throws Exception {
		String task = "-2 + 6";
		assertEquals(4.0, script.parse(task), eps);
	}

	@Test
	public void testSignedSecondArg() throws Exception {
		String task = "2 + -6";
		assertEquals(-4.0, script.parse(task), eps);
	}

	@Test
	public final void testMixedFloat() throws Exception {
		String task = "2.0/3 - 5.2*4";
		assertEquals(2. / 3.0 - 5.2 * 4, script.parse(task), eps);
	}

	@Test
	public final void testLongAdd() throws Exception {
		String task = "2.0 + 3 + 4.0 + 5";
		assertEquals(14, script.parse(task), eps);
	}

	@Test
	public final void testLongMult() throws Exception {
		String task = "2 * 3.0 * 4 * 5.000";
		assertEquals(120, script.parse(task), eps);
	}

	@Test
	public final void testLongMixed() throws Exception {
		String task = "2.0 * 3 * 4.0 + 5 + 6.0 / 3 ";
		assertEquals(31, script.parse(task), eps);
	}

	@Test
	public void testParseBracket() throws Exception {
		String task = " 2*(4.0 + 3)";
		assertEquals(14, script.parse(task), eps);
	}

	// Meine Tests
	@Test(expected = IllegalArgumentException.class)
	public final void testBracketsNotClosed() throws Exception {
		String task = "(7 *(2 + 3 ";
		assertEquals(35, script.parse(task), eps);
	}

	@Test(expected = IllegalArgumentException.class)
	public final void testBracketsFuckedUp() throws Exception {
		String task = "(7 *)2 + 3 ";
		script.parse(task);
	}

	@Test
	public final void testNoStatement() throws Exception {
		String task = ";";
		//should just not break down
		script.parse(task);
	}

	@Test
	public final void testPower() throws Exception {
		String task = "2^(4)";
		assertEquals(16, script.parse(task), eps);
	}

	@Test(expected = IllegalArgumentException.class)
	public final void testPowerFuckedBracket() throws Exception {
		String task = "2^(4";
		assertEquals(16, script.parse(task), eps);
	}

	@Test
	public final void testSetGetVariable2() throws Exception {
		script.setVariable("x", 3);
		assertEquals(3, script.getVariable("x"), eps);
	}
	
	@Test
	public final void testSetVariable() throws Exception {
		String task = "x=3";
		
		assertEquals(3, script.parse(task), eps);
		assertEquals(3, script.getVariable("x"), eps);
	}
	
	
	@Test
	public final void testVariableSetAndAdd() throws Exception {
		String task = "x=1;y=2;z=x+y;";
		script.parse(task);
		assertEquals(3,script.parse(task),eps);
	
	}
	
	
	@Test
	public final void testFunction() throws Exception {
		String task = "f(x)=42*x;n=f(2);";
		script.parse(task);
		assertNotNull(script.getFunction("f"));
		assertEquals(84 ,script.getVariable("n"), eps );
	
	}
	@Test
	public final void testFunction2() throws Exception {
		String task = "f(x)=11*x;f(3);";
		double d = script.parse(task);
		assertNotNull(script.getFunction("f"));
		assertEquals(33 ,d, eps );	
	}
	
	@Test
	public final void testFunction2Variables() throws Exception {
		String task = "f2(x,y)=x*y;f2(3,4);";
		double d = script.parse(task);
		assertNotNull(script.getFunction("f2"));
		assertEquals(12 ,d, eps );	
	}
	
	@Test
	public final void testFunction5Variables() throws Exception {
		String task = "heftig(a,b,c,d,e)=a*b*c*d*e;heftig(1,2,3,4,5);";
		double d = script.parse(task);
		assertNotNull(script.getFunction("heftig"));
		assertEquals(120 ,d, eps );	
	}
	
	@Test
	public final void testFunctionSinus() throws Exception {
		String task = "cos(3.1415);";
		double d = script.parse(task);
		assertNotNull(script.getFunction("cos"));
		assertEquals(-1 ,d, eps );	
	}
	
	
	/*
	 * @Test public final void testAssignVariables() throws Exception { String
	 * task = "x = 4;y = 5;"; script.parse(task); script.setVariable("x", 4);
	 * script.setVariable("y", 5);
	 * 
	 * assertEquals(4, script.getVariable("x"), eps); assertEquals(5,
	 * script.getVariable("y"), eps); }
	 */

}
