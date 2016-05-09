package compiler.analysis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import compiler.analysis.generated.Lexer;
import compiler.analysis.model.SemanticError;
import compiler.analysis.model.Type;

public class SemanticAnalyser {
	
	//Tipos
	//TODO:Contextos
	//TODO:Checagem de tipos e contextos
	//TODO:nome, quantidade e tipos de entrada e retorno
	//declaracao e uso de variaveis
	//comandos de atribuicao
	//TODO:expressoes aritmeticas
	
	//TODO:procedures, funcoes
	//expressoes relacionais, literais (int, char, bool)
	//condicionais if-else
	

	public static HashMap<String, Type> variables = new HashMap<>();
	public static HashMap<String, Type> localvariables = new HashMap<>();
	public static ArrayList<String> values = new ArrayList<String>();
	public static ArrayList<Type> relationalTypes = new ArrayList<Type>();
	public String nextVariable = null;

	public SemanticAnalyser() {
		/* estes sao os unicos tipos definidos pelo professor no escopo do projeto */
		relationalTypes = new ArrayList<>(Arrays.asList(new Type("int"), new Type("char"), new Type("bool")));
	}
	
	public void isSelectionStatementOK(Type expressionType) throws SemanticError{
		Type bool = new Type("bool");
		if(expressionType == null || (expressionType != null && !expressionType.equals(bool))){
			throw new SemanticError("Erro no IF Statemente.");
		}
	}

	public Type getRelationalExpressionType(Type e1, Type e2) throws SemanticError{
		if(!relationalTypes.contains(e1) || !relationalTypes.contains(e2)){
			throw new SemanticError("Operacao relacional nao se aplica a "+e1.toString()+" ou a "+e2.toString());
		}
		/*else if(!e1.equals(e2)){
			throw new SemanticError("Voce nao pode comparar o tipo "+e1.toString()+" com o tipo "+e2.toString());
		}*/
		
		return new Type("bool");
	}
	
	public void checkTypesDeclaration(Object o1, Object o2) throws SemanticError{
		
		Type t1 = (Type) o1;
		Type t2 = null;
		String variable = null;
		try{
			t2 = (Type) o2;
		}catch(Exception e){
			variable = (String) o2;
		}
		
		if(variable == null){
			if(t1 != null && !t1.equals(t2)){
				if(t1.equals(new Type("bool"))){
					if(t2.equals(new Type("int")) || t2.equals(new Type("bool"))) 
						; //do nothing
					else throw new SemanticError(t2.toString()+" nao pode ser atribuido a "+t1.toString());
				}else if(t1.equals(new Type("int")) && t2.equals(new Type("char"))){
					System.err.println("Warning! Linha: " + Lexer.getCurrentLine() + ". Voce esta convertendo " + t2.toString() + " em " + t1.toString() + ".");
				}else if(t1.equals(new Type("char")) && t2.equals(new Type("int"))){
					System.err.println("Warning! Linha: " + Lexer.getCurrentLine() + ". Voce esta convertendo " + t2.toString() + " em " + t1.toString() + ".");
				}
				
				else if(t1.equals(new Type("int"))){
					if(t2.equals(new Type("char"))){
						System.err.println("Warning! Linha: " + Lexer.getCurrentLine() + ". Voce esta convertendo " + t2.toString() + " em " + t1.toString() + ".");
					}else if(t2.equals(new Type("float")) || t2.equals(new Type("double"))){
						System.err.println("Warning! Linha: " + Lexer.getCurrentLine() + ". Voce esta convertendo " + t2.toString() + " em " + t1.toString() + ".");
					}
				}
				
				else if(t1.equals(new Type("char"))){
					if(t2.equals(new Type("int"))){
						System.err.println("Warning! Linha: " + Lexer.getCurrentLine() + ". Voce esta convertendo " + t2.toString() + " em " + t1.toString() + ".");
					}else if(!t2.equals(new Type("char"))){
						throw new SemanticError(t2.toString()+" nao pode ser atribuido a "+t1.toString());
					}
				}
				
				else if(t2.equals(new Type("char"))){
					if(!t1.equals(new Type("char"))){
						throw new SemanticError(t2.toString()+" nao pode ser atribuido a "+t1.toString());
					}
				}
				
				else if(t2.equals(new Type("bool"))){
					if(!t1.equals(new Type("bool"))){
						throw new SemanticError(t2.toString()+" nao pode ser atribuido a "+t1.toString());
					}
				}
				
			}
		}
	}
	
	
	public void setNextVariableType(Type type) throws SemanticError{
		if(nextVariable != null){
			if(variables.containsKey(nextVariable)){
				throw new SemanticError("Identificador { " + nextVariable + " } ja esta em uso!");
			}
			variables.put(nextVariable, type);
			//System.out.println("variable name: " + nextVariable + " - variable type: " + variables.get(nextVariable));
			//System.out.println("quantidade de variaveis: " + variables.size());
		}
		nextVariable = null;
	}
	
	public Type getVariableTypeByName(String name) throws SemanticError{
		if(name != null){
			if(!variables.containsKey(name)){
				throw new SemanticError("Variavel { " + name + " } nao declarada!");
			}
		}
		return variables.get(name);
	}
	
	public Type getTypeIfExists(String name) {
		if(name != null){
			if(variables.containsKey(name)){
				return variables.get(name);
			}
		}
		return null;
	}
	
	public void addLocalVariable(){
		
	}
	
}
