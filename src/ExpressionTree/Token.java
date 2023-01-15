package ExpressionTree;

public class Token {
	String expr;
	TokenType type;
	Double numericRepresentation;

	public Token(String val, TokenType type) {
		this.expr = val;
		this.type = type;
		this.numericRepresentation = null;
	}

	public String getExpr() {
		return this.expr;
	}

	public TokenType getType() {
		return this.type;
	}
	
	public void setType(TokenType type) {
		this.type = type;
	}
	
	public void setExpr(String expr) {
		this.expr = expr;
	}
	
	public Double getNumericRepresentation() {
		return this.numericRepresentation;
	}

	@Override
	public String toString(){
		return "(Expr: "+ expr+ ", Token: "+ type+")";
	}
	
	public void generateNumericRepresentation() {
		if (this.type == TokenType.NUMBER) {
			numericRepresentation = Double.parseDouble(this.expr);
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((expr == null) ? 0 : expr.hashCode());
		result = prime * result + ((numericRepresentation == null) ? 0 : numericRepresentation.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object token) {
		
		if (this == token) {
			return true;
		}
		
		Token compared = null;
		if (token instanceof Token) {
			compared = (Token)token;
		}
		
		return (this.type.equals(compared.type)) && (this.expr.equals(compared.expr));
	}
	

}
