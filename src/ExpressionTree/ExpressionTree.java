package ExpressionTree;

import java.io.Serializable;
import java.io.ObjectInputStream.GetField;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import arithmetic.Function;
import arithmetic.Term;
import data.Register;
import exceptions.IncorrectParametersAmountExcepetion;

public class ExpressionTree implements Serializable {

	private static final long serialVersionUID = 1L;
	
	Token value;
	ExpressionTree right;
	ExpressionTree left;
	
	public ExpressionTree(Token t) {
		value = t;
		right = null;
		left = null;
	}
	
	public ExpressionTree() {
		value = null;
		right = null;
		left = null;
	}
	
	public ExpressionTree(Token t, ExpressionTree lt, ExpressionTree rt) {
		value = t;
		right = rt;
		left = lt;
	}
	
	public ExpressionTree(Double var, ExpressionTree lt, ExpressionTree rt) {
		value = new Token(var.toString(), TokenType.NUMBER);
		right = rt;
		left = lt;
	}
	
	public ExpressionTree getLeft() {
		return this.left;
	}
	
	public ExpressionTree getRight() {
		return this.right;
	}
	
	public Token getValue() {
		return this.value;
	}
	
	
	public void setToken(Token t) {
		this.value = t;
	}
	
	public void setLeft(ExpressionTree t) {
		this.left = t;
	}
	
	public void setRight(ExpressionTree t) {
		this.right = t;
	}
	public ExpressionTree generateTreeFromExpression(String expr) {
		Tokenizer tokenizer = new Tokenizer();
		ArrayList<Token> tokens = tokenizer.generatePostfix(expr);
		
		//System.out.println(tokens);
		
		Stack<ExpressionTree> stack = new Stack<>();
		
		while (!tokens.isEmpty()) {
			Token t = tokens.get(0);
			ExpressionTree toCreate;
			ExpressionTree right;
			ExpressionTree left;
			try {
				if (t.getType() == TokenType.NUMBER || t.getType() == TokenType.VARIABLE) {
					stack.push(new ExpressionTree(t));
				} else if (t.getType().isOperator()) {
					right = stack.lastElement();
					stack.pop();
					left = stack.lastElement();
					stack.pop();
					toCreate = new ExpressionTree(t, left, right);
					stack.push(toCreate);
				// Token is a function
				} else {
					left = stack.lastElement();
					stack.pop();
					
					if (t.getType() == TokenType.NEGATE) {
						toCreate = new ExpressionTree(t, left, null);
						stack.push(toCreate);
					} else {
						if (Register.isRegularFunction(t.expr)) {
							toCreate = new ExpressionTree(t, left, null);
							stack.push(toCreate);
						} else {
							if (Register.getUniqueRegister().containsKey(t.expr)) {
								stack.push(Register.getUniqueRegister().get(t.expr).getExpr());
							} else {
								return null;
							}
						}
					}	
				}
			} catch(NoSuchElementException e) {
				e.printStackTrace();		
			}
			
			
			tokens.remove(0);
			
		}
		
		return stack.lastElement();
	}
	
	public static void extractVariables(ExpressionTree tree, ArrayList<Token> var) {
		
		if (tree != null) {
			if (tree.value.getType() == TokenType.VARIABLE) {
				if (!var.contains(tree.value)) {
					var.add(tree.value);
				}
			}
			extractVariables(tree.left, var);
			extractVariables(tree.right, var);
		}	
	}
	
	public static void replaceEachVariableByValue(ExpressionTree tree, Token t, String val) {
		
		if (tree != null) {

			if (tree.value.equals(t)) {
				tree.value.setType(TokenType.NUMBER);
				tree.value.setExpr(val);
			}
			replaceEachVariableByValue(tree.right, t, val);
			replaceEachVariableByValue(tree.left, t, val);
		}
	}
	
	public static double executeFunction(String name, Double param) {
		if (Register.isRegularFunction(name)) {
			String className = "java.lang.Math";
			Class<?> c = null;
			Method method = null;
			Object t = null;
			try {
				c = Class.forName(className);
			} catch (ClassNotFoundException e) {
			}       
			try {
				method = c.getDeclaredMethod(name, double.class);
			} catch (NoSuchMethodException | SecurityException e) {
			}
			try {
				t = method.invoke(null, param);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				e.printStackTrace();
			}
			
			return (Double)t;
		}
		
		return (Double) null;
	}

	public static double evalTree(ExpressionTree tree) {
		
		if (tree == null) {
			return 0;
		}
		
		if (tree.value.type == TokenType.NUMBER) {
			tree.value.generateNumericRepresentation();
			return tree.value.getNumericRepresentation();
		}
		
		double leftVal = evalTree(tree.left);
		double rightVal = evalTree(tree.right);
		
		if (tree.value.getType() == TokenType.ADD)
			return leftVal + rightVal;
		
		if (tree.value.getType() == TokenType.SUB)
			return leftVal - rightVal;
		
		else if (tree.value.getType() == TokenType.MUL)
			return leftVal * rightVal;
		
		else if (tree.value.getType() == TokenType.DIV)
			return leftVal / rightVal;
		
		else if (tree.value.getType() == TokenType.POW)
			return Math.pow(leftVal, rightVal);
		
		else if (tree.value.getType() == TokenType.NEGATE)
			return -1 * leftVal;
		
		return executeFunction(tree.value.expr, tree.left.value.numericRepresentation);
		
	}
	
	public void replaceAllVariables(ArrayList<String> values) throws IncorrectParametersAmountExcepetion {
		ArrayList<Token> l = new ArrayList<>();
		extractVariables(this, l);
		
		if (l.size() != 0) {
			if (l.size() != values.size()) {
				throw new IncorrectParametersAmountExcepetion("Wrong amount of parameters !");
			}
			
			for (int i = 0; i < values.size(); i++) {
				replaceEachVariableByValue(this, l.get(i), values.get(i));
			}
		}
	}
	
	public int getvariablesAmountInTree() {
		ArrayList<Token> var = new ArrayList<>();
		extractVariables(this, var);
		return var.size();
	}
	
	public static String printTree(ExpressionTree tree) {
		
		if (tree == null) {
			return "";
		} else if (tree.left == null && tree.right == null) {
			return tree.value.expr;
		}
		
		String leftVal = printTree(tree.left);
		String rightVal = printTree(tree.right);
		
		return leftVal + tree.value.expr + rightVal;	
	}
	
	public static boolean isNumeric(ExpressionTree tree) {
		
		if (tree == null) {
			return true;
		}
		
		if (tree.value.type == TokenType.FUNCTION || tree.value.type == TokenType.VARIABLE) {
			return false;
		} 
		
		return isNumeric(tree.left) && isNumeric(tree.right);
	}
	
	public static boolean isPolynomial(ExpressionTree tree) {
		if (tree == null) {
			return true;
		}
		
		if (ExpressionTree.isNumeric(tree) || Term.canBeConvertedToTerm(tree)) {
			return true;
		}
		
		if (tree.value.getType() == TokenType.FUNCTION) {
			return false;
		}
		
		if (tree.value.getType() == TokenType.POW) {
			if (tree.right.getvariablesAmountInTree() != 0) {
				return false;
			}
		}
		
		return isPolynomial(tree.right) && isPolynomial(tree.left);
	}
	
	public static String retrieveExpression(ExpressionTree tree) {
		
		if (tree == null) {
			return "";
		}
		
		if(tree.value.getType() == TokenType.NUMBER || tree.value.getType() == TokenType.VARIABLE) {
			return retrieveExpression(tree.left) + tree.value.expr + retrieveExpression(tree.right);
		}
		
		
		if (tree.right != null && tree.left != null) {
			if (tree.right.value.getType().isOperator() && tree.left.value.getType().isOperator()) {
				if (tree.value.getType().getPrecendence() > tree.right.value.getType().getPrecendence() && tree.value.getType().getPrecendence() > tree.left.value.getType().getPrecendence()) {
					return "(" + retrieveExpression(tree.left) + ")" + tree.value.expr + "(" + retrieveExpression(tree.right) + ")";
				}
			}
		}
		
		if (tree.value.getType().isOperator()) {
			if (tree.left != null) {
				if (tree.left.value.getType().isOperator()) {
					if (tree.value.getType().getPrecendence() > tree.left.value.getType().getPrecendence()) {
						return "(" + retrieveExpression(tree.left) + ")" + tree.value.expr + retrieveExpression(tree.right);
					}
				}
			}
			
			if (tree.right != null) {
				if (tree.right.value.getType().isOperator()) {
					if (tree.value.getType().getPrecendence() > tree.right.value.getType().getPrecendence()) {
						return retrieveExpression(tree.left) + tree.value.expr + "(" + retrieveExpression(tree.right) + ")";
					}
				}
			}
			

			return retrieveExpression(tree.left) + tree.value.expr + retrieveExpression(tree.right);
		}
		
		
		return "";
	}
	
	@Override
	public String toString() {	
		return ExpressionTree.retrieveExpression(this);
	}
	
	public static void main(String args[]) {
		Register r = new Register();
		Function f = Function.parseFunctionDefinition("f(x) = (6x+3)(4x+7)");
		Function g = Function.parseFunctionDefinition("g(x) = 2x");
		Function h = Function.parseFunctionDefinition("h(x,y) = g");
		
		Register.addToRegister(f);
		Register.addToRegister(g);
		Register.addToRegister(h);
		System.out.println(r);
	}
}
