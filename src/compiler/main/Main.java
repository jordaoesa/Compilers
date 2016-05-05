package compiler.main;

import java.io.FileReader;

import compiler.analysis.generated.Lexer;
import compiler.analysis.generated.Parser;
import java_cup.runtime.Symbol;

public class Main {

	public static void main(String[] args) {
		String prog = "files/prog.c";
		
		try{

			Parser p = new Parser(new Lexer(new FileReader(prog)));
			// Object result = p.parse().value;
			Symbol s = p.parse();
			if(s.toString().equals("#0")){
				System.out.println("> Successfull Compilation: " + prog);
			}else{
				System.out.println(s.toString());
			}
		}catch(Exception e){
			System.err.println("Processo de Compilacao Falhou!");
			System.err.println(e.getMessage());
		}
	}

}
