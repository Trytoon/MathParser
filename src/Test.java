import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ExpressionTree.ExpressionTree;
import ExpressionTree.Token;
import ExpressionTree.TokenType;
import ExpressionTree.Tokenizer;
import arithmetic.Function;
import arithmetic.Term;
import data.Register;
import exceptions.IncorrectParametersAmountExcepetion;
import exceptions.IncorrectTermProvidedExcepetion;

public class Test {

	public static void main(String[] args) {

		String expr = "(2x+7)*5";
		ExpressionTree poly = new ExpressionTree();
		poly = poly.generateTreeFromExpression(expr);
		
		System.out.println(ExpressionTree.isPolynomial(poly));
		
		System.out.println(poly);
	}

}
