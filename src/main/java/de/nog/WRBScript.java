package de.nog;

import java.io.IOException;
import java.io.InputStream;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import de.nog.antlr.WRBLexer;
import de.nog.antlr.WRBParser;

//import main.antlr.WRBParser;

//von mir erstellt damit der compiler happy ist
public class WRBScript implements Script {

	public WRBObserver observer;

	public WRBScript() {
		super();
		observer = new WRBObserver(this);
		
		addStandardfunctions();
		
	}
	private void addStandardfunctions(){
		observer.functions.put("sin", new Function() {			
			@Override
			public double eval(double... args) {
				return Math.sin(args[0]);
			}
		});
		
		observer.functions.put("cos", new Function() {			
			@Override
			public double eval(double... args) {
				return Math.cos(args[0]);
			}
		});
		
		
		observer.functions.put("tan", new Function() {			
			@Override
			public double eval(double... args) {
				return Math.tan(args[0]);
			}
		});
		
		observer.functions.put("asin", new Function() {			
			@Override
			public double eval(double... args) {
				return Math.asin(args[0]);
			}
		});
		observer.functions.put("acos", new Function() {			
			@Override
			public double eval(double... args) {
				return Math.acos(args[0]);
			}
		});
		
	}

	public Function getFunction(String name) throws IllegalArgumentException {
		Function f = observer.functions.get(name);
		if (f != null)
			return f;
		throw new IllegalArgumentException("Function " + name + " not found");
	}

	public double getVariable(String name) throws IllegalArgumentException {
		Double ret = observer.variables.get(name);
		if (ret != null)
			return observer.variables.get(name);
		throw new IllegalArgumentException("Variable " + name + " not found");
	}

	public void setVariable(String name, double value) {
		observer.variables.put(name, value);

	}

	public double parse(String definition) throws IllegalArgumentException {
		CharStream stream = new ANTLRInputStream(definition);
		WRBLexer lexi = new WRBLexer(stream);
		CommonTokenStream tokens = new CommonTokenStream(lexi);
		WRBParser parser = new WRBParser(tokens);
		// WRBObserver observer = new WRBObserver(this);
		parser.setBuildParseTree(true);
		ANTLRErrorListener listener = observer;
		parser.addErrorListener(listener);

		ParseTree tree = parser.start();

		ParseTreeWalker.DEFAULT.walk(observer, tree);
		if (observer.getShitThatHappenedWhileParsing() != null) {

			throw new IllegalArgumentException(observer.getShitThatHappenedWhileParsing());
		}
		return observer.getLastValue();

	}

	public double parse(InputStream defStream) throws IllegalArgumentException, IOException {
		// TODO Auto-generated method stub
		return 0;
	}

}
