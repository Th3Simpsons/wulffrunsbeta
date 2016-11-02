package de.lab4inf.wrb;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import de.nog.Script;
import de.nog.WRBScript;

public class SimpleHeirTest extends AbstractScriptTest {

	@Before
	public void setUp() throws Exception {
		super.setUp();
	}

	@Override
	protected Script getScript() {
		return new WRBScript();
	}

	

}
