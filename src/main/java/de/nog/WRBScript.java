package de.nog;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

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
	Map<String, Double> variables;
	Map<String, Function> functions;

	public WRBScript() {
		super();
		variables = new HashMap<String, Double>();
		functions = new HashMap<String, Function>();
	}

	public Function getFunction(String name) throws IllegalArgumentException {
		if (functions.containsKey(name))
			return functions.get(name);
		throw new IllegalArgumentException();
	}

	public double getVariable(String name) throws IllegalArgumentException {
		if (variables.containsKey(name))
			return variables.get(name);
		throw new IllegalArgumentException();
	}

	public void setVariable(String name, double value) {
		variables.put(name, value);

	}

	public double parse(String definition) throws IllegalArgumentException {
		CharStream stream = new ANTLRInputStream(definition);
		WRBLexer lexi = new WRBLexer(stream);
		CommonTokenStream tokens = new CommonTokenStream(lexi);
		WRBParser parsi = new WRBParser(tokens);
		WRBObserver obs = new WRBObserver(this);
		parsi.setBuildParseTree(true);
		ParseTree tree = parsi.start();
		ParseTreeWalker.DEFAULT.walk(obs, tree);

		return obs.getLastValue();

	}

	public double parse(InputStream defStream) throws IllegalArgumentException, IOException {
		// TODO Auto-generated method stub
		return 0;
	}

}
