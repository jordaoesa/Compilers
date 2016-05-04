package compiler.main;

import java.io.FileReader;

import compiler.generated.Lexer;
import compiler.generated.Parser;
import java_cup.runtime.Symbol;

public class Main {

	public static void main(String[] args) throws Exception {
		String prog = "src/compiler/files/prog.c";

		Parser p = new Parser(new Lexer(new FileReader(prog)));
		// Object result = p.parse().value;
		Symbol s = p.parse();
		if(s.toString().equals("#0")){
			System.out.println("> Successfull Compilation: " + prog);
		}else{
			System.out.println(s.toString());
			System.out.println(s);
		}
	}

}
