package de.lab4inf.wrb;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import de.lab4inf.wrb.rest.Main;

public class PrintWulffTest {

	@Test
	public void test() {
		try {
			System.out.println( Main.readFile("src/main/Wulff.txt"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			fail();
		}
	}

}
