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

	

}
