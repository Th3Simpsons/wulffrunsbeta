package de.lab4inf.wrb;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import de.lab4inf.wrb.rest.HelloClient;
import de.lab4inf.wrb.rest.WRBClient;
import de.lab4inf.wrb.rest.WRBServer;

public class WRBServerTest {

	static HelloClient helloClient;
	static WRBClient wrbClient;
	private static final boolean verbose = false;

	@BeforeClass
	public static void initializeServer() {

		String[] args = {};
		WRBServer.server = WRBServer.startServer(args);
		System.out.printf("JUnit: Server started with WADL at %sapplication.wadl\n", WRBServer.BASE_URI);
		helloClient = new HelloClient("localhost:8080");
		wrbClient = new WRBClient("localhost:8080");

	}

	@Test
	public void testSayHello() throws Exception {
		String response = helloClient.sayHello("Gib mir ein Echo");
		if (!response.contains("Gib mir ein Echo") || !response.contains("Thread")) {
			fail("Say hello response is bad. Response=" + response);
		}

	}

	@Test
	public void differentiateTestSingleArgument() throws Exception {
		String response = wrbClient.getDifferentiation("sin");
		if (response.contains("Illegal") || response.contains("Exception")) {
			fail("Response is bad. Response=" + response);
		} else {
			if (verbose)
				System.out.println("success: " + response);
		}

	}

	@Test
	public void differentiateTestTwoArguments() throws Exception {
		String response = wrbClient.getDifferentiation("sin", "x=0");
		if (response.contains("Illegal") || response.contains("Exception") || !response.contains("1.00")) {
			fail("Response is bad. Response=" + response);
		} else {
			if (verbose)
				System.out.println("success: " + response);
		}
	}

	@Test
	public void differentiateTestTwoArgumentsArray() throws Exception {
		String response = wrbClient.getDifferentiation("sin", "xmin=0; xmax=1; dx=0.5");
		if (response.contains("Illegal") || response.contains("Exception") || !response.contains("1.00")) {
			fail("Response is bad. Response=" + response);
		} else {
			if (verbose)
				System.out.println("success: " + response);
		}
	}

	@Test
	public void differentiateTestThreeArgumentsArray() throws Exception {
		String response = wrbClient.getDifferentiation("sin", "xmin=0; xmax=2*3.1415926; dx=3.1415926", "%.2f");
		if (!"{1.00,-1.00,1.00}".equals(response)) {
			fail("Response is bad. Response=" + response);
		} else {
			if (verbose)
				System.out.println("success: " + response);
		}
	}

	@Test
	public void differentiateTestDefineOwnFunction() throws Exception {
		String response = wrbClient.getDifferentiation("f", "f(x) = x^3; xmin=0; xmax=2; dx=1", "%.2f");
		if (!"{0.00,3.00,12.00}".equals(response)) {
			fail("Response is bad. Response=" + response);
		} else {
			if (verbose)
				System.out.println("success: " + response);
		}
	}

	
	@Test
	public void integrateTestDefineOwnFunction() throws Exception {
		String response = wrbClient.getIntegration("f", "f(x) = x^3; a=0; b=2;", "%.2f");
		if (!"4.00".equals(response)) {
			fail("Response is bad. Response=" + response);
		} else {
			if (verbose)
				System.out.println("success: " + response);
		}
	}
	
	@Test
	public void integrateTestDefineOwnFunction2() throws Exception {
		String response = wrbClient.getIntegration("f", "f(x)=x^3; a=0; b=3;", "%.2f");
		if (!"20.25".equals(response)) {
			fail("Response is bad. Response=" + response);
		} else {
			if (verbose)
				System.out.println("success: " + response);
		}
	}
	@Test
	public void integrateTestDefineOwnFunction3() throws Exception {
		String response = wrbClient.getIntegration("f", "a=0; b=3; f(x)=x*x*x+5", "%.2f");
		if (!"35.25".equals(response)) {
			fail("Response is bad. Response=" + response);
		} else {
			if (verbose)
				System.out.println("success: " + response);
		}
	}
	@AfterClass
	public static void tearDownServer() {
		if(verbose)System.out.println("Test ended: Tear down");
		WRBServer.stopServer();
	}
}
