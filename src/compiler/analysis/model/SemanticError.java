package compiler.analysis.model;

import compiler.analysis.generated.Lexer;

public class SemanticError extends Exception{
	
	public SemanticError(String message) {
		super("Erro semantico na linha " + Lexer.getCurrentLine() + ". " + message);
	}

}
