package de.nog;

import java.awt.List;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.atn.ATNConfigSet;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.*;

import de.nog.antlr.WRBLexer;
import de.nog.antlr.WRBParser;
import de.nog.antlr.WRBParser.AdditionContext;
import de.nog.antlr.WRBParser.AssignContext;
import de.nog.antlr.WRBParser.ConstantContext;
import de.nog.antlr.WRBParser.ExpressionContext;
import de.nog.antlr.WRBParser.MultiContext;
import de.nog.antlr.WRBParser.PowContext;
//import de.nog.antlr.WRBParser.AssignContext;
//import de.nog.antlr.WRBParser.ExpressionContext;
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
	protected RecognitionException shitIDealtWith = null;

	String getSpaceOffset() {
		String space = "";
		for (int i = 0; i < printOffset; i++)
			space += " ";
		return space;
	}

	public RecognitionException getShitThatHappenedWhileParsing() {
		return shitIDealtWith;
	}

	@Override
	public void syntaxError(Recognizer<?, ?> arg0, Object arg1, int arg2, int arg3, String arg4,
			RecognitionException arg5) {
		shitIDealtWith = new RecognitionException(arg0, arg0.getInputStream(), null);
	}

	@Override
	public void reportContextSensitivity(Parser arg0, DFA arg1, int arg2, int arg3, int arg4, ATNConfigSet arg5) {
		System.err.println("context sensivity shit right here ");
		shitIDealtWith = new RecognitionException(arg0, arg0.getInputStream(), null);
	}

	@Override
	public void reportAttemptingFullContext(Parser arg0, DFA arg1, int arg2, int arg3, BitSet arg4, ATNConfigSet arg5) {
		System.err.println("attempt full context shit right here ");
		shitIDealtWith = new RecognitionException(arg0, arg0.getInputStream(), null);
	}

	@Override
	public void reportAmbiguity(Parser arg0, DFA arg1, int arg2, int arg3, boolean arg4, BitSet arg5,
			ATNConfigSet arg6) {
		System.err.println("ambigious shit right here ");
		shitIDealtWith = new RecognitionException(arg0, arg0.getInputStream(), null);
	}

	@Override
	public void enterStatement(StatementContext ctx) {
		printOffset = 0;
		System.out.println("enter statement. \"" + ctx.getText() + "\"");

	}

	@Override
	public void exitAssign(AssignContext ctx) {
		String varName = ctx.ID().getText();
		Double varValue = getValue(ctx.expression());
		System.out.println("Assigning " + varName + " = " + varValue);
		variables.put(varName, varValue);
		treeValues.put(ctx, varValue);
		debugPrintVariables();
	}

	public void debugPrintVariables() {
		for (Entry<String, Double> e : variables.entrySet()) {
			System.out.println("Entry \"" + e.getKey() + "\" = " + e.getValue()
					+ (e.getKey().equals("x") ? " (equals x)" : " (not x)"));

		}
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
		System.out.println("Statement returns " + lastValue);
	}

	@Override
	public void exitExpression(ExpressionContext ctx) {
		setValue(ctx, getValue(ctx.addition()));
	}

	@Override
	public void exitAddition(AdditionContext ctx) {
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
		int k = 0;
		double value = getValue(ctx.constant(k));
		ParseTree node = ctx.constant(++k);
		while (null != node) {
			value = Math.pow(value, getValue(node));
			node = ctx.constant(++k);
		}
		setValue(ctx, value);
	}

	@Override
	public void exitConstant(ConstantContext ctx) {

		if (ctx.INTEGER() != null) {
			setValue(ctx, Double.parseDouble(ctx.INTEGER().getText()));
		}
		if (ctx.FLOAT() != null) {
			setValue(ctx, Double.parseDouble(ctx.FLOAT().getText()));
		}
		//
		if (ctx.expression() != null) {
			setValue(ctx, getValue(ctx.expression()));
		}
		//Function
		if (ctx.function() != null) {
			setValue(ctx, getValue(ctx.function()) );
		}
		//Variable
		if (ctx.ID() != null) {
			if (variables.get(ctx.ID().getText()) != null)
				setValue(ctx, variables.get(ctx.ID().getText()));
			else
				setValue(ctx, 0);
		}

		System.out.println(getSpaceOffset() + "Value is " + getValue(ctx));
	}

	@Override
	public void exitFunction(WRBParser.FunctionContext ctx) {
		System.out.println("function evaluation...");
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
System.out.println("Setting node val of " + ctx.ID());
		setValue(ctx, f.eval(xn));

	}

	@Override
	public void exitFunctiondefinition(WRBParser.FunctiondefinitionContext ctx) {

		System.out.println(
				"Found functiondef: " + ctx.ID().get(0).getText().toString() + " = " + ctx.expression().getText());
		functions.put(ctx.ID().get(0).getText().toString(), new ExprFunction(ctx.expression().getText(), this) {

			@Override
			public double eval(double... args) {
				int i = 1;
				for (double d : args) {
					System.out.println("Added x" + i + " = " + d + " to localvarset");
					wrbObserver.variables.put("x" + i++, d);
				}
				System.out.println("Started parsing expression");
				double ret = script.parse(expression);
				return ret;
			}
		});
	}

	private void setValue(ParseTree ctx, double value) {
		treeValues.put(ctx, value);
	}

	@Override
	public void enterEveryRule(@NotNull ParserRuleContext ctx) {
		System.out.println(getSpaceOffset() + "->:" + getPrintText(ctx.getClass().toString()));
		printOffset++;
	}

	@Override
	public void exitEveryRule(@NotNull ParserRuleContext ctx) {
		System.out.println(getSpaceOffset() + "<-:" + getPrintText(ctx.getClass().toString()));
		printOffset--;
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
		throw new IllegalArgumentException(node.toString());
	}

	public double getLastValue() {
		return lastValue;
	}

}