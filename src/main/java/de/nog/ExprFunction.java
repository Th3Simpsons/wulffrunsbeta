package de.nog;

import de.nog.antlr.WRBParser.ExpressionContext;

public class ExprFunction implements Function {

	String expression;
	WRBObserver wrbObserver;

	// public Function
	public ExprFunction(String string, WRBObserver wrbObserver) {
		this.expression = string;
		this.wrbObserver = wrbObserver;
	}

	@Override
	public double eval(double... args) {
		// TODO Auto-generated method stub
		return 0;
	}

}
