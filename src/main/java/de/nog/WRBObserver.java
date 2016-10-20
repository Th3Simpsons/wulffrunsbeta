package de.nog;

import java.util.HashMap;
import java.util.Map;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
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

public class WRBObserver extends WRBParserBaseListener {
	public WRBObserver(WRBScript wrbScript) {
		this.script = wrbScript;
		if (null == script)
			throw new IllegalArgumentException("script is null");
	}

	private static int printOffset = 0;
	protected Map<String, Double> variables = new HashMap<String, Double>();
	protected Map<ParseTree, Double> values = new HashMap<ParseTree, Double>();
	protected WRBScript script;
	protected double lastValue;

	String getSpaceOffset() {
		String space = "";
		for (int i = 0; i < printOffset; i++)
			space += " ";
		return space;
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
	}

	@Override
	public void exitStatement(StatementContext ctx) {
		// System.out.println("exit statement. children: " + ctx.children);

		lastValue = getValue(ctx.expression());
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
		if (ctx.expression() != null) {
			setValue(ctx, getValue(ctx.expression()));
		}
		System.out.println(getSpaceOffset()+"Value is " + getValue(ctx));
	}

	private void setValue(ParseTree ctx, double value) {
		values.put(ctx, value);
	}

	@Override
	public void enterEveryRule(@NotNull ParserRuleContext ctx) {
		System.out.println(getSpaceOffset()+"->:" + getPrintText(ctx.getClass().toString()));
		printOffset++;
	}

	@Override
	public void exitEveryRule(@NotNull ParserRuleContext ctx) {
		System.out.println(getSpaceOffset()+"<-:" + getPrintText(ctx.getClass().toString()));
		printOffset--;
	}

	String getPrintText(String input) {
		String out = input.substring(input.indexOf('$') + 1);
		out = out.substring(0, out.indexOf("Context"));
		return out;
	}
	/*
	 * @Override public void exitExpression(@NotNull WRBParser.ExpressionContext
	 * ctx) { int k = 0; double value = getValue(ctx.term(k)); ParseTree node =
	 * ctx.term(++k); while (null != node) { Token op = ctx.operator.get(k - 1);
	 * if (WRBLexer.ADD == op.getType()) { value += getValue(node); } else {
	 * value -= getValue(node); } } setValue(ctx, value); }
	 */
	/*
	 * @Override public void exitDotop(@NotNull WRBParser.DotopContext ctx){
	 * 
	 * }
	 * 
	 * private void setValue(ExpressionContext ctx, double value) {
	 * values.replace(ctx, value); }
	 */

	private double getValue(ParseTree node) {
		if (values.containsKey(node)) {
			return values.get(node);
		}
		// return -88;
		throw new IllegalArgumentException(node.toString());
	}

	public double getLastValue() {
		return lastValue;
	}

}