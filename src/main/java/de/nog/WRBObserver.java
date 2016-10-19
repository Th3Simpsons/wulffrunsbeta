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
import de.nog.antlr.WRBParser.ExpressionContext;
//import de.nog.antlr.WRBParser.AssignContext;
//import de.nog.antlr.WRBParser.ExpressionContext;
import de.nog.antlr.WRBParser.StatementContext;
import de.nog.antlr.WRBParserBaseListener;

//dummycomment
public class WRBObserver extends WRBParserBaseListener {
	public WRBObserver(WRBScript wrbScript) {
		this.script = wrbScript;
		if (null == script)
			throw new IllegalArgumentException("script is null");
	}

	protected Map<ParseTree, Double> values = new HashMap<ParseTree, Double>();
	protected WRBScript script;
	protected double lastValue;

	/*
	 * @Override public void exitAssign(@NotNull WRBParser.AssignContext ctx) {
	 * String id = ctx.ID().getSymbol().getText(); // double value =
	 * getValue(ctx.expression()); // script.setVariable(id, value); // //
	 * setValue(ctx, value); }
	 */
	/*
	 * @Override public void enterExpression(ExpressionContext ctx) {
	 * System.out.println("Enter Expression"); }
	 */
	@Override
	public void enterStatement(StatementContext ctx) {
		// System.out.println("enter statement. children:" + ctx.children);

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
		double summe = 0;
		if (ctx.operator.get(0).getType() == WRBParser.ADD) {
			summe = getValue(ctx.multi(0)) + getValue(ctx.multi(1));
		} else {
			summe = getValue(ctx.multi(0)) - getValue(ctx.multi(1));
		}
		setValue(ctx, summe);
	}

	private void setValue(ParseTree ctx, double value) {
		values.put(ctx, value);
	}

	@Override
	public void enterEveryRule(@NotNull ParserRuleContext ctx) {
		System.out.println("enter rule:" + ctx.getClass().toString().substring(ctx.getClass().toString().indexOf('$')));
	}

	@Override
	public void exitEveryRule(@NotNull ParserRuleContext ctx) {
		System.out.println("exit rule:" + ctx.getClass().toString().substring(ctx.getClass().toString().indexOf('$')));
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
		throw new IllegalArgumentException();
	}

	public double getLastValue() {
		return lastValue;
	}

}