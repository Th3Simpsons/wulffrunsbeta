package de.lab4inf.wrb;

import static org.junit.Assert.*;


import org.junit.Test;

public class SimpleHeirTest extends AbstractScriptTest {

	
	@Override
	protected Script getScript() {
		return new WRBScript();
	}
	
	@Test
    public final void testMixedFloatSofter() throws Exception {
        String task = "5.2*4";
        assertEquals(20.8, script.parse(task), eps);
    }

	@Test
    public final void testStrangeFunctionDefinitionError() throws Exception {
        String task = "f(x)=x^3+5;f(3)";
        assertEquals(32, script.parse(task), eps);
    }
	@Test
    public final void testScientific() throws Exception {
	    assertEquals(10000, script.parse("1.0e+04"), eps);
        assertEquals(1000, script.parse("1.0e+03"), eps);
        assertEquals(100, script.parse("1.0e+02"), eps);
        assertEquals(10, script.parse("1.0e+01"), eps);
        assertEquals(0.1, script.parse("1.0e-01"), eps);
        assertEquals(0.01, script.parse("1.0e-02"), eps);
        assertEquals(0.001, script.parse("1.0e-03"), eps);
        assertEquals(0.0001, script.parse("1.0e-04"), eps);
        
    }
	

}
