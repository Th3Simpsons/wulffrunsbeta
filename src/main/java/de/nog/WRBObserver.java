package de.nog;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;
import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.atn.ATNConfigSet;
import org.antlr.v4.runtime.dfa.DFA;

import org.antlr.v4.runtime.tree.*;

import de.lab4inf.wrb.Function;
import de.nog.antlr.WRBLexer;
import de.nog.antlr.WRBParser;
import de.nog.antlr.WRBParser.AssignContext;
import de.nog.antlr.WRBParser.ConstantContext;
import de.nog.antlr.WRBParser.ExpressionContext;
import de.nog.antlr.WRBParser.FunctiondefinitionContext;
import de.nog.antlr.WRBParser.MultiContext;
import de.nog.antlr.WRBParser.PowContext;
import de.nog.antlr.WRBParser.StatementContext;
import de.nog.antlr.WRBParserBaseListener;

public class WRBObserver extends WRBParserBaseListener implements ANTLRErrorListener {
	public WRBObserver(WRBScript wrbScript) {
		this.script = wrbScript;
		if (null == script)
			throw new IllegalArgumentException("script is null");
	}

	private static int printOffset = 0;
	protected Map<String, Double> variables = new HashMap<String, Double>();
	protected Map<String, Function> functions = new HashMap<String, Function>();
	protected Map<ParseTree, Double> treeValues = new IdentityHashMap<ParseTree, Double>();
	protected WRBScript script;
	protected double lastValue;
	protected IllegalArgumentException shitIDealtWith = null;
	private boolean debug = false;

	String getSpaceOffset() {
		String space = "";
		for (int i = 0; i < printOffset; i++)
			space += " ";
		return space;
	}

	public IllegalArgumentException getShitThatHappenedWhileParsing() {
		return shitIDealtWith;
	}

	@Override
	public void syntaxError(Recognizer<?, ?> arg0, Object arg1, int arg2, int arg3, String arg4,
			RecognitionException arg5) {
		shitIDealtWith = new IllegalArgumentException("Syntax error");
	}

	@Override
	public void reportContextSensitivity(Parser arg0, DFA arg1, int arg2, int arg3, int arg4, ATNConfigSet arg5) {
		shitIDealtWith = new IllegalArgumentException("context sensivity shit right here ");
	}

	@Override
	public void reportAttemptingFullContext(Parser arg0, DFA arg1, int arg2, int arg3, BitSet arg4, ATNConfigSet arg5) {
		shitIDealtWith = new IllegalArgumentException("attempt full context shit right here ");
	}

	@Override
	public void reportAmbiguity(Parser arg0, DFA arg1, int arg2, int arg3, boolean arg4, BitSet arg5,
			ATNConfigSet arg6) {
		shitIDealtWith = new IllegalArgumentException("ambigious shit right here ");
	}

	/*
	 * @Override public void enterStatement(StatementContext ctx) { printOffset
	 * = 0; // debug("enter statement. \"" + ctx.getText() + "\"");
	 * 
	 * }
	 */

	@Override
	public void exitAssign(AssignContext ctx) {
		String varName = ctx.ID().getText();
		Double varValue = getValue(ctx.expression());
		// debug("Assigning " + varName + " = " + varValue);
		variables.put(varName, varValue);
		treeValues.put(ctx, varValue);
	}

	@Override
	public void exitStatement(StatementContext ctx) {
		// System.out.println("exit statement. children: " + ctx.children);

		if (ctx.expression() != null) {
			lastValue = getValue(ctx.expression());
		}
		if (ctx.assign() != null) {
			lastValue = getValue(ctx.assign());
		}

		setValue(ctx, lastValue);
		// debug("Statement returns " + lastValue);
	}

	@Override
	public void exitExpression(ExpressionContext ctx) {
		int k = 0;
		double value = getValue(ctx.multi(k));
		ParseTree node = ctx.multi(++k);
		while (null != node) {
			Token op = ctx.operator.get(k - 1);
			if (WRBLexer.ADD == op.getType()) {
				value += getValue(node);
			} else {
				value -= getValue(node);
			}
			node = ctx.multi(++k);
		}
		setValue(ctx, value);

		lastValue = value;
		// debug("last value is" + lastValue);
	}

	void debug(String msg) {
		if (debug)
			System.out.println(msg);
	}

	@Override
	public void exitMulti(MultiContext ctx) {
		int k = 0;
		double value = getValue(ctx.pow(k));
		ParseTree node = ctx.pow(++k);
		while (null != node) {
			Token op = ctx.operator.get(k - 1);
			if (WRBLexer.MUL == op.getType()) {
				value *= getValue(node);
			} else {
				value /= getValue(node);
			}
			node = ctx.pow(++k);
		}
		setValue(ctx, value);
	}

	@Override
	public void exitPow(PowContext ctx) {
		int k = ctx.constant().size() - 1;
		double value = getValue(ctx.constant(k));
		ParseTree node = ctx.constant(--k);
		while (null != node) {
			value = Math.pow(getValue(node), value);
			node = ctx.constant(--k);
		}
		setValue(ctx, value);
	}

	@Override
	public void exitConstant(ConstantContext ctx) {
		int factor = 1;
		if (ctx.sign != null) {
			if (!ctx.sign.isEmpty()) {
				if (ctx.sign.get(0).getText().equals("-")) {
					// debug("the following is negative");
					factor = -1;
				} else {
					// debug("the term " + ctx.getText() + " is positive");
				}
			} else {// debug("the term " + ctx.getText() + " has no sign");

			}

		}

		// Function
		if (ctx.function() != null) {
			setValue(ctx, factor * getValue(ctx.function()));
			return;
		}
		//
		if (ctx.expression() != null) {
			setValue(ctx, factor * getValue(ctx.expression()));
			return;
		}
		if (ctx.INTEGER() != null) {
			setValue(ctx, factor * Double.parseDouble(ctx.INTEGER().getText()));
			return;
		}
		if (ctx.FLOAT() != null) {
			setValue(ctx, factor * Double.parseDouble(ctx.FLOAT().getText()));
			return;
		}
		// Variable
		if (ctx.ID() != null) {
			if (variables.get(ctx.ID().getText()) != null) {
				setValue(ctx, factor * variables.get(ctx.ID().getText()));
				return;
			} else {
				setValue(ctx, factor * 0);
				return;
			}
		}

		// debug(getSpaceOffset() + "Value is " + factor * getValue(ctx));
	}

	@Override
	public void exitFunction(WRBParser.FunctionContext ctx) {
		// debug("function evaluation...");
		Function f = functions.get(ctx.ID().getText());
		if (f == null)
			return;
		ArrayList<Double> args = new ArrayList<Double>();

		for (ExpressionContext exCtx : ctx.expression()) {
			double arg = getValue(exCtx);
			args.add(arg);
		}
		double[] xn = new double[args.size()];
		int i = 0;
		for (double a : args) {
			xn[i++] = a;
		}
		// debug("Setting node val of " + ctx.ID());
		setValue(ctx, f.eval(xn));

	}

	@Override
	public void enterFunctiondefinition(FunctiondefinitionContext ctx) {

		if (ctx.expression() != null) {
			ArrayList<String> argList = new ArrayList<String>();

			for (TerminalNode id : ctx.ID()) {
				// OHNE ID 0, weil es die funktions bezeichnung ist
				if (id != ctx.ID(0))
					argList.add(id.getText());
			}
			Function f = new ExprFunction(ctx.expression(), argList, this);
			functions.put(ctx.ID(0).getText(), f);
		}
		ctx.removeLastChild();
	}

	private void setValue(ParseTree ctx, double value) {
		treeValues.put(ctx, value);
	}

	

	String getPrintText(String input) {
		String out = input.substring(input.indexOf('$') + 1);
		out = out.substring(0, out.indexOf("Context"));
		return out;
	}

	private double getValue(ParseTree node) {
		if (treeValues.containsKey(node)) {
			return treeValues.get(node);
		}
		throw new IllegalArgumentException("Unable to find value for " + node.getText());
	}

	public double getLastValue() {
		return lastValue;
	}

}