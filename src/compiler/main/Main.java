package compiler.main;

import java.io.FileReader;

import compiler.generated.Lexer;
import compiler.generated.Parser;

public class Main {
	
	public static void main(String[] args) throws Exception {
		String prog = "src/compiler/files/prog.c";
		
    	Parser p = new Parser(new Lexer(new FileReader(prog)));
		Object result = p.parse().value;
		
	}

}
