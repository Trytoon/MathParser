package ExpressionTree;

public enum TokenType {
	ADD,
	SUB,
	MUL,
	DIV,
	POW,
	LPAR,
	RPAR,
	NUMBER,
	VARIABLE,
	FUNCTION,
	NEGATE;

	@Override
	public String toString() {
		switch (this.ordinal()){
		case 0:
			return "ADD";
		case 1:
			return "SUB";
		case 2:
			return "MUL";
		case 3:
			return "DIV";
		case 4:
			return "POW";
		case 5:
			return "LPAR";
		case 6:
			return "RPAR";
		case 7:
			return "NUM";
		case 8:
			return "VAR";
		case 9:
			return "FUN";
		case 10:
			return "NEG";
		default:
			return "null";
		}
	}
	
	public boolean isOperator() {
		return this.ordinal() >= 0 && this.ordinal() <= 4;
	}
	
	public int getPrecendence() {
		switch(this) {
		case FUNCTION:
			return 5;
		case POW:
			return 4;
		case MUL:
			return 3;
		case DIV:
			return 3;
		case ADD:
			return 2;
		case SUB:
			return 2;
		case NEGATE:
			return 5;
		default:
			return 1;
		}
	}
}
