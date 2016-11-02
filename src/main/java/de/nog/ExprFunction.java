package de.nog;

import java.security.KeyStore.Entry;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Map;

import org.antlr.v4.runtime.tree.ParseTreeWalker;

import de.nog.antlr.WRBParser.ExpressionContext;

public class ExprFunction implements Function {

	String expression;
	ExpressionContext expCtx;
	WRBObserver wrbObserver;
	List<String> argList;
	private boolean debug = false;

	// public Function
	/*
	 * public ExprFunction(String string, WRBObserver wrbObserver) {
	 * this.expression = string; this.wrbObserver = wrbObserver; }
	 */

	public ExprFunction(ExpressionContext expCtx, List<String> argList, WRBObserver wrbObserver) {
		this.expCtx = expCtx;
		this.wrbObserver = wrbObserver;
		this.argList = argList;
	}

	@Override
	public double eval(double... args) {
		//
		int a = args.length;
		int b = argList.size();

		if (args.length != argList.size()) {
			throw new IllegalArgumentException("Functionparameters are not matching number of definition");
		}
		int i = 0;
		//Deque<Map.Entry<String, Double>> stack = new ArrayDeque<Map.Entry<String, Double>>();
		for (String arg : argList) {
			// save Vars before
			//Map.Entry<String, Double> vAssign;
			//stack.push(e);

			// ---------

			wrbObserver.variables.put(arg, args[i]);
			debug("Added " + arg + " = " + args[i] + " to local function context");
			i++;
		}
		debug("walking through the tree of " + expCtx.getText());
		ParseTreeWalker.DEFAULT.walk(wrbObserver, expCtx);

		return wrbObserver.lastValue;
	}

	void debug(String msg) {
		if (debug)
			System.out.println(msg);
	}
}
