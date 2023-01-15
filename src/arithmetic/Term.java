package arithmetic;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ExpressionTree.ExpressionTree;
import ExpressionTree.TokenType;
import exceptions.IncorrectTermProvidedExcepetion;

public class Term {

	private double coeff;
	private String variable;
	private double degree;

	public Term(double coeff, String var, double degree) {
		this.coeff = coeff;
		this.variable = var;
		this.degree = degree;
	}

	public Term(String representation) {
		Term t = null;
		representation = representation.replaceAll(" ", "");
		try {
			t = generateRepresentationNumeric(representation);
		} catch (IncorrectTermProvidedExcepetion e) {
			e.printStackTrace();
		}

		this.coeff = t.coeff;
		this.variable = t.variable;
		this.degree = t.degree;
	}


	// For variables
	//([+-]?\d+(\.\d+)?)?([a-zA-Z]?)?\*?([a-zA-Z])?(\^([+-]?\d+(\.\d+)?))?(\^?[a-zA-Z])?

	public static Term generateRepresentationNumeric(String representation) throws IncorrectTermProvidedExcepetion {

		Pattern p = Pattern.compile("([+-]?\\d+(\\.\\d+)?)\\*?([a-zA-Z]?)(\\^([+-]?\\d+(\\.\\d+)?))?|([-+]?)([a-zA-Z]?)(\\^([+-]?\\d+(\\.\\d+)?))?");
		Matcher m = p.matcher(representation);
		double coeff = 0;
		String var = null;
		double pow = 0;
		
		if (m.find() == false) {
			throw new IncorrectTermProvidedExcepetion("Term cannot be converted !");
		} else {
			representation = representation.replaceAll("--", "\\+");

			if (m.group(7) == null) {
				if (!m.group(0).equals(representation)) {
					throw new IncorrectTermProvidedExcepetion("Term cannot be converted !");
				} else {
					
					
					try {
						coeff = Double.parseDouble(m.group(1));
					} catch(Exception e) {
						e.printStackTrace();
					}

					if (!m.group(3).isEmpty())
						var = m.group(3);
					if (!m.group(3).isEmpty() && m.group(5) == null) {
						pow = 1;
					}

					if (m.group(5) != null) {
						try {
							pow = Double.parseDouble(m.group(5));
						} catch(Exception e) {
							e.printStackTrace();
						}
					}
				}
			} else {
				
				if (m.group(7).equals("-")) {
					coeff = -1;
				} else {
					coeff = 1;
				}
				
				if (!m.group(8).isEmpty()) {
					var = m.group(8);
				}
				
				if (m.group(10) != null) {
					try {
						pow = Double.parseDouble(m.group(10));
					} catch(Exception e) {
						e.printStackTrace();
					}
				} else {
					pow = 1;
				}
			}
		}

		
		return new Term(coeff, var, pow);
	}

	public double getCoeff() {
		return this.coeff;
	}
	public String getVariable() {
		return this.variable;
	}
	public double getDegree() {
		return this.degree;
	}
	
	public void setCoeff(Double coeff) {
		this.coeff = coeff;
	}
	
	public void setDegree(Double degree) {
		this.degree = degree;
	}
	
	
	public String toString() {
		if(degree == 0) {
			double a = coeff;
			return Double.toString(a);
		} else if (degree == 1) {
			
			if (coeff == 1) {
				return this.variable;
			}
			
			return this.coeff + "" +this.variable;
		}
		return this.coeff +"" + this.variable + "^" + this.degree;
	}

	public boolean isConstant() {
		return this.variable == null && this.degree == 0;
	}
	
	public boolean isZero() {
		return this.coeff == 0;
	}
	
	public static boolean canBeConvertedToTerm(ExpressionTree tree) {
		
		if (tree == null) {
			return true;
		}
		
		if (tree.getRight() == null && tree.getLeft() == null) {
			return true;
		} else if (tree.getValue().getType().equals(TokenType.FUNCTION)) {
			return false;
		}
		
		if (tree.getValue().getType().ordinal() <= 1) {
			return tree.getLeft().getvariablesAmountInTree() == 0 && tree.getRight().getvariablesAmountInTree() == 0;
		}
		
		if (tree.getValue().getType().equals(TokenType.DIV)) {
			
			if (tree.getRight().getvariablesAmountInTree() != 0) {
				return false;
			}
			
			if (tree.getLeft().getValue().getType().equals(TokenType.NUMBER) && tree.getLeft().getValue().getType().equals(TokenType.NUMBER)) {
				return true;
			}
			
			if (tree.getLeft().getValue().getType() == TokenType.VARIABLE && tree.getLeft().getValue().getType() == TokenType.NUMBER) {
				return true;
			}
			
			
			
			return canBeConvertedToTerm(tree.getRight()) && canBeConvertedToTerm(tree.getLeft());
		}
		
		if (tree.getValue().getType().equals(TokenType.POW)) {
			
			if (tree.getRight().getvariablesAmountInTree() != 0) {
				return false;
			}
			
			if (tree.getRight().getValue().getType() == TokenType.NUMBER) {
				return true;
			}
			
			return canBeConvertedToTerm(tree.getRight()) && canBeConvertedToTerm(tree.getLeft());
		}
		
		return canBeConvertedToTerm(tree.getRight()) && canBeConvertedToTerm(tree.getLeft());
		
	}
}
