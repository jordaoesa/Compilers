package compiler.analysis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import compiler.analysis.generated.Lexer;
import compiler.analysis.model.SemanticError;
import compiler.analysis.model.Type;

public class SemanticAnalyser {
	
	//Tipos
	//Contextos
	//Checagem de tipos e contextos
	//TODO:nome, quantidade e tipos de entrada e retorno
	//declaracao e uso de variaveis
	//comandos de atribuicao
	//TODO:expressoes aritmeticas
	
	//TODO:procedures, funcoes
	//expressoes relacionais, literais (int, char, bool)
	//condicionais if-else
	
	public static ArrayList<String> parameterTL = new ArrayList<>();

	public static HashMap<String, Type> variables = new HashMap<>();
	public static HashMap<String, Type> localVariables = null;
	
	public static ArrayList<String> values = new ArrayList<String>();
	public static ArrayList<Type> relationalTypes = new ArrayList<Type>();
	public String nextVariable = null;

	public SemanticAnalyser() {
		/* estes sao os unicos tipos definidos pelo professor no escopo do projeto */
		relationalTypes = new ArrayList<>(Arrays.asList(new Type("int"), new Type("char"), new Type("bool")));
	}
	
	public void isSelectionStatementOK(Type expressionType) throws SemanticError{
		if(expressionType != null){
			if(!expressionType.equals(new Type("bool")) && !expressionType.equals(new Type("int"))){
				throw new SemanticError(expressionType + " nao pode ser utilizado como expressao condicional!");
			}
		}else{
			throw new SemanticError("Expressao dentro do { if } nao pode ser nula!");
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
			}else if(localVariables != null){
				if(localVariables.containsKey(nextVariable)){
					throw new SemanticError("Identificador { " + nextVariable + " } ja esta em uso neste escopo!");
				}
				localVariables.put(nextVariable, type);
			}else{
				variables.put(nextVariable, type);
			}
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
	
	public Type getTypeIfExists(String name) throws SemanticError {
		if(name != null){
			if(localVariables != null && localVariables.containsKey(name)){
				return localVariables.get(name);
			}else if(variables.containsKey(name)){
				return variables.get(name);
			}
		}
		throw new SemanticError("Variavel { " + name + " } nao foi declarada previamente!" );
	}
	
	public void checkTypeOfSizeArray(Object o) throws SemanticError{
		Type t = (Type)o;
		if(!t.equals(new Type("int"))){
			throw new SemanticError(t.toString() + " nao pode ser usado para declarar tamanho de array!");
		}
	}
	
	public void checkConsistencyInsideArray(Object o1, Object o2) throws SemanticError{
		Type t1 = (Type) o1;
		Type t2 = (Type) o2;
		if(!t1.equals(t2)){
			throw new SemanticError(t1.toString() + " e " + t2.toString() + " encontrados em um mesmo array!");
		}
	}
	
	public void addLocalvariable(String name, Type t){
		localVariables.put(name, t);
	}
	
	public void mudaEscopo(){
		localVariables = new HashMap<String, Type>();
	}
	
}
