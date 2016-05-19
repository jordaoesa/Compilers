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
	//nome, quantidade e tipos de entrada e retorno
	//declaracao e uso de variaveis
	//comandos de atribuicao
	//expressoes aritmeticas
	
	//procedures, funcoes
	//expressoes relacionais, literais (int, char, bool)
	//condicionais if-else
	
	public static HashMap<String, Type> variables = new HashMap<>();
	public static HashMap<String, Type> localVariables = null;
	
	public static HashMap<String, Type> methods = new HashMap<>();
	public static HashMap<String, ArrayList<Type>> methodParams = new HashMap<>();
	public static HashMap<String, ArrayList<Type>> methodCalls = new HashMap<>();
	
	public static ArrayList<Type> relationalTypes = new ArrayList<Type>();
	public String nextVariable = null;
	public String nextMethodCall = null;

	public SemanticAnalyser() {
		relationalTypes = new ArrayList<>(Arrays.asList(new Type("int"), new Type("char"), new Type("bool"), new Type("float"), new Type("double")));
	}
	
	public void isSelectionStatementOK(Object type) throws SemanticError{
		Type expressionType = null;
		try{
			expressionType = (Type) type;
		}catch(Exception e){
			throw new SemanticError("Expressao dentro do { if } nao pode { " + type + " }!");
		}
		
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
		else if(!e1.equals(e2)){
			
			if(e1.equals(new Type("bool")) && !e2.equals(new Type("int"))){
				throw new SemanticError("Voce nao pode comparar o tipo "+e1.toString()+" com o tipo "+e2.toString());
			}
			else if(e1.equals(new Type("int"))){
				if(!(e2.equals(new Type("char")) || e2.equals(new Type("bool")))){
					throw new SemanticError("Voce nao pode comparar o tipo "+e1.toString()+" com o tipo "+e2.toString());
				}
			}
			else if(e1.equals(new Type("float")) && !e2.equals(new Type("double"))){
				throw new SemanticError("Voce nao pode comparar o tipo "+e1.toString()+" com o tipo "+e2.toString());
			}
			else if(e1.equals(new Type("double")) && !e2.equals(new Type("float"))){
				throw new SemanticError("Voce nao pode comparar o tipo "+e1.toString()+" com o tipo "+e2.toString());
			}
			else if(e1.equals(new Type("char")) && !e2.equals(new Type("int"))){
				throw new SemanticError("Voce nao pode comparar o tipo "+e1.toString()+" com o tipo "+e2.toString());
			}
			
		}
		
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
					}else if(t2.equals(new Type("void"))){
						throw new SemanticError(t2.toString()+" nao pode ser atribuido a "+t1.toString());
					}
				}
				
				else if(t1.equals(new Type("char"))){
					if(t2.equals(new Type("int"))){
						System.err.println("Warning! Linha: " + Lexer.getCurrentLine() + ". Voce esta convertendo " + t2.toString() + " em " + t1.toString() + ".");
					}else if(!t2.equals(new Type("char"))){
						throw new SemanticError(t2.toString()+" nao pode ser atribuido a "+t1.toString());
					}
				}
				
				else if(t1.equals(new Type("double"))){
					if(t2.equals(new Type("int")) || t2.equals(new Type("float"))){
						System.err.println("Warning! Linha: " + Lexer.getCurrentLine() + ". Voce esta convertendo " + t2.toString() + " em " + t1.toString() + ".");
					}else{
						throw new SemanticError(t2.toString()+" nao pode ser atribuido a "+t1.toString());
					}
				}
				
				else if(t1.equals(new Type("float"))){
					if(t2.equals(new Type("int")) || t2.equals(new Type("double"))){
						System.err.println("Warning! Linha: " + Lexer.getCurrentLine() + ". Voce esta convertendo " + t2.toString() + " em " + t1.toString() + ".");
					}else{
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
			if(variables.containsKey(nextVariable) || methods.containsKey(nextVariable)){
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
				Type type = localVariables.get(name);
				type.setOwnerId(name);
				return type;
			}else if(variables.containsKey(name)){
				Type type = variables.get(name);
				type.setOwnerId(name);
				return type;
			}else if(methods.containsKey(name)){
				Type type = methods.get(name);
				type.setOwnerId(name);
				return type;
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
	
	public HashMap<String, Type> getMethods() {
		return methods;
	}
	
	public void addMetodo(String name, Type returnType) throws SemanticError{
		if(name != null){
			if(methods.containsKey(name) || variables.containsKey(name) || localVariables.containsKey(name)){
				throw new SemanticError("Identificador { " + name + " } ja esta em uso!");
			}else{
				methods.put(name, returnType);
				//methodParams.put(name, new ArrayList<Type>());
			}
		}else{
			throw new SemanticError("O nome do metodo nao pode ser nulo!");
		}
	}
	
	public void addMetodoEParametros(String name, Object params){
		if(params!= null){
			if(!methodParams.containsKey(name)){
				methodParams.put(name, new ArrayList<Type>());
			}
			HashMap<String, Type> parametros = (HashMap<String, Type>) params;
			for(String n : parametros.keySet()){
				methodParams.get(name).add(parametros.get(n));
			}
		}else{
			methodParams.put(name, new ArrayList<Type>());
		}
		//System.err.println(methodParams);
	}
	
	public void iniciaChamadaDeMetodo(Type type){
		nextMethodCall = type.getOwnerId();
		methodCalls.put(type.getOwnerId(), new ArrayList<Type>());
	}
	
	public void addTiposNaChamadaCorrente(Type type){
		//System.err.println("nextMethood >> " + nextMethodCall);
		if(nextMethodCall != null){
			methodCalls.get(nextMethodCall).add(type);
		}
		//System.err.println("atual lista >> "+methodCalls);
	}
	
	public void checaConsistenciaDeTiposChamada(String methodName) throws SemanticError{
		if(methods.containsKey(methodName) && methodCalls.containsKey(methodName) && methodParams.containsKey(methodName)){
			String chamada = methodName + "(";
			for(Type t : methodParams.get(methodName)){
				chamada += t.getName()+",";
			}
			if(methodParams.get(methodName).size() > 0)
				chamada = chamada.substring(0, chamada.length()-1) +   ");";
			else
				chamada += ");";
			
			if(methodCalls.get(methodName).size() != methodParams.get(methodName).size()){
				throw new SemanticError("Metodo { " + methodName + " } possui " + methodParams.get(methodName).size() + " parametro(s). Chamada correta: [" + chamada + "]");
			}else{
				for(int i = 0; i < methodParams.get(methodName).size(); i++){
					if(!methodParams.get(methodName).get(i).equals(methodCalls.get(methodName).get(i))){
						throw new SemanticError("Metodo { "+methodName+" } esperava " + methodParams.get(methodName).get(i) + " como #" + (i+1) + " parametro, encontrou " + methodCalls.get(methodName).get(i) + ". Chamada correta: [" + chamada + "]");
					}
				}
			}
		}else{
			throw new SemanticError("Metodo { " + methodName + " } nao foi declarado/chamado corretamente!");
		}
	}
	
	public void checkTipoDeRetornoMetodo(Type declared, Type returned) throws SemanticError{
		if(!declared.equals(returned)){
			if(declared.equals(new Type("double"))){
				if(returned.equals(new Type("double")) || returned.equals(new Type("float")) || returned.equals(new Type("int"))){
					//does nothing
				}else{
					throw new SemanticError("Tipo de retorno do metodo " + declared + " eh diferente do tipo retornado " + returned + "!");
				}
			}
			else if(declared.equals(new Type("float"))){
				if(returned.equals(new Type("double")) || returned.equals(new Type("int"))){
					System.err.println("Warning! Linha: " + Lexer.getCurrentLine() + ". Tipo de retorno eh " + declared.toString() + " e o tipo retornado eh " + returned.toString() + "!");	
				}else{
					throw new SemanticError("Tipo de retorno do metodo " + declared + " eh diferente do tipo retornado " + returned + "!");
				}
			}
			else if(declared.equals(new Type("int"))){
				if(returned.equals(new Type("double")) || returned.equals(new Type("float")) || returned.equals(new Type("char"))){
					System.err.println("Warning! Linha: " + Lexer.getCurrentLine() + ". Tipo de retorno eh " + declared.toString() + " e o tipo retornado eh " + returned.toString() + "!");
				}else{
					throw new SemanticError("Tipo de retorno do metodo " + declared + " eh diferente do tipo retornado " + returned + "!");
				}
			}
			else if(declared.equals(new Type("bool"))){
				if(!(returned.equals(new Type("bool")) || returned.equals(new Type("int")))){
					throw new SemanticError("Tipo de retorno do metodo " + declared + " eh diferente do tipo retornado " + returned + "!");
				}
			}
			else if(declared.equals(new Type("char"))){
				if(returned.equals(new Type("int"))){
					System.err.println("Warning! Linha: " + Lexer.getCurrentLine() + ". Tipo de retorno eh " + declared.toString() + " e o tipo retornado eh " + returned.toString() + "!");
				}else{
					throw new SemanticError("Tipo de retorno do metodo " + declared + " eh diferente do tipo retornado " + returned + "!");
				}
			}
			
			else{
				throw new SemanticError("Tipo de retorno do metodo " + declared + " eh diferente do tipo retornado " + returned + "!");
			}
		}
	}
	
	public Type getTypeFromExpression(String operation, Type t1, Type t2) throws SemanticError {
		
		Type caracter = new Type("char");
		Type inteiro = new Type("int");
		Type doublee = new Type("double");
		Type floatt = new Type("float");
		Type bool = new Type("bool");
		
		if(!t1.equals(t2)){
			if(t1.equals(doublee)){
				if(t2.equals(inteiro) || t2.equals(floatt)){
					System.err.println("Warning! Linha: " + Lexer.getCurrentLine() + ". Operacao convertera os dados para "+doublee.toString()+"!");
					return doublee;
				}else{
					throw new SemanticError("Voce nao pode realizar operacao entre " + t1.toString() + " e " + t2.toString());
				}
			}
			else if(t1.equals(floatt)){
				if(t2.equals(inteiro)){
					System.err.println("Warning! Linha: " + Lexer.getCurrentLine() + ". Operacao convertera os dados para "+floatt.toString()+"!");
					return floatt;
				}else if(t2.equals(doublee)){
					System.err.println("Warning! Linha: " + Lexer.getCurrentLine() + ". Operacao convertera os dados para "+doublee.toString()+"!");
					return doublee;
				}else{
					throw new SemanticError("Voce nao pode realizar operacao entre " + t1.toString() + " e " + t2.toString());
				}
			}
			else if(t1.equals(inteiro)){
				if(t2.equals(doublee)){
					System.err.println("Warning! Linha: " + Lexer.getCurrentLine() + ". Operacao convertera os dados para "+doublee.toString()+"!");
					return doublee;
				}else if(t2.equals(floatt)){
					System.err.println("Warning! Linha: " + Lexer.getCurrentLine() + ". Operacao convertera os dados para "+floatt.toString()+"!");
					return floatt;
				}else if(t2.equals(caracter)){
					System.err.println("Warning! Linha: " + Lexer.getCurrentLine() + ". Operacao convertera os dados para "+inteiro.toString()+"!");
					return inteiro;
				}else{
					throw new SemanticError("Voce nao pode realizar operacao entre " + t1.toString() + " e " + t2.toString());
				}
			}
			else if(t1.equals(caracter)){
				if(t2.equals(inteiro)){
					System.err.println("Warning! Linha: " + Lexer.getCurrentLine() + ". Operacao convertera os dados para "+caracter.toString()+"!");
					return caracter;
				}else{
					throw new SemanticError("Voce nao pode realizar operacao entre " + t1.toString() + " e " + t2.toString());
				}
			}
			else if(t1.equals(bool) || t2.equals(bool)){
				throw new SemanticError("Voce nao pode realizar operacoes aritmeticas com tipo _Bool!");
			}
			
			
			else{
				throw new SemanticError("Voce nao pode realizar operacao entre " + t1.toString() + " e " + t2.toString()+"!");
			}
			
		}else if(t1.equals(bool) && t2.equals(bool)){
			throw new SemanticError("Voce nao pode realizar operacoes aritmeticas com tipo _Bool!");
		}
		return t1;
	}
	
}