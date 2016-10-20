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
import org.antlr.v4.runtime.IntStream;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.atn.ATNConfigSet;
import org.antlr.v4.runtime.dfa.DFA;
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
		throw new IllegalArgumentException("Function " + name + "not found");
	}

	public double getVariable(String name) throws IllegalArgumentException {
		if (variables.containsKey(name))
			return variables.get(name);
		throw new IllegalArgumentException("Variable " + name + " not found");
	}

	public void setVariable(String name, double value) {
		variables.put(name, value);

	}

	public double parse(String definition) throws RecognitionException, IllegalArgumentException {
		CharStream stream = new ANTLRInputStream(definition);
		WRBLexer lexi = new WRBLexer(stream);
		CommonTokenStream tokens = new CommonTokenStream(lexi);
		WRBParser parser = new WRBParser(tokens);
		WRBObserver observer = new WRBObserver(this);
		parser.setBuildParseTree(true);
		ANTLRErrorListener listener = observer;
		parser.addErrorListener(listener);

		ParseTree tree = parser.start();

		ParseTreeWalker.DEFAULT.walk(observer, tree);
		if (observer.getShitThatHappenedWhileParsing() != null) {
			throw observer.getShitThatHappenedWhileParsing();
		}
		return observer.getLastValue();

	}

	public double parse(InputStream defStream) throws IllegalArgumentException, IOException {
		// TODO Auto-generated method stub
		return 0;
	}

}
