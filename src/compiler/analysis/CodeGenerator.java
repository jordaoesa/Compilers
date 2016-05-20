package compiler.analysis;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import compiler.analysis.model.Type;

public class CodeGenerator {
	
	//Load Variaveis globais e locais
	//Store Variaveis globais e locais
	//Operacoes aritmeticas
	//TODO: IF-ELSE (se nao for uma operacao relacional nao esta tratado)
	//chamadas a metodos

	private final String FILE_PATH = "files/output.assembly";
	
	public String nextMethod = null; 
	public String nextArraySize = null;
	
	public HashMap<String, String> methods = new HashMap<String, String>();
	public HashMap<String, String> methodReturns = new HashMap<String, String>();
	private String currMethod = null;
	
	private String code;
	private int pilhaPos;
	public int memoryPos;
	private int register;
	
	public CodeGenerator(){
		this.pilhaPos = 5000;
		this.memoryPos = 100;
		this.register = 0;
		this.code = "";
		code += nextMemPosition() + "\tLD SP, " + pilhaPos + "\n";
	}
	
	private String nextAvailableRegister(){
		return "R" + register++;
	}
	
	private String lastRegister(){
		return "R" + (register-1);
	}
	
	private String nextMemPosition(){
		String returnStr = memoryPos+":";
		memoryPos += 8;
		return returnStr;
	}
	
	public void loadVariable(String valor, String reg){
		code += nextMemPosition() + "\tLD " + reg + ", " + valor + "\n";
	}
	
	public void storeVariable(String varName, String reg){
		code += nextMemPosition() + "\tST " + varName + ", " + reg + "\n";
	}
	
	public void loadAndStoreVariable(Type type){
		//System.err.println(type.getOwnerId() + type.getValue());
		String methodName = type.getValue();
		if(methodReturns.containsKey(methodName)){
			String reg = methodReturns.get(methodName);
			storeVariable(type.getOwnerId(), reg);
		}else{
			String reg = nextAvailableRegister();
			loadVariable(type.getValue(), reg);
			storeVariable(type.getOwnerId(), reg);
		}
	}
	
	public void createMethodAndParams(String identifier, Object params){
//		if(params != null){
//			code += nextMemPosition() + " " + identifier + ":\n";
//			//Caso eu queira adicionar os parametros do metodo a registradores assim q instancio o metodo
//			/*HashMap<String, Type> parametros = (HashMap<String, Type>) params;
//			for(String n : parametros.keySet()){
//				String reg = nextAvailableRegister();
//				loadVariable(n, reg);
//			}*/
//		}else{
//			code += nextMemPosition() + " " + identifier + ":\n";
//		}
		methods.put(identifier, memoryPos+"");
		code += nextMemPosition() + " " + identifier + ":\n";
		currMethod = identifier;
		//System.err.println(identifier + methods);
	}
	
	public void loadAndStoreValuesAssignment(Object o1, Object o2){
		Type t1 = (Type) o1;
		Type t2 = (Type) o2;
		
		//System.err.println(t1.getOwnerId() + t1.getValue());
		//System.err.println(t2.getOwnerId() + t2.getValue());
		
		String temp1 = t2.getValue() != null ? t2.getValue() : t2.getOwnerId();
		String temp2 = t1.getOwnerId() == null? t1.getValue() : t1.getOwnerId();
		if(methodReturns.containsKey(temp1)){
			storeVariable(temp2, methodReturns.get(temp1));
		}else{
			String reg = nextAvailableRegister();
			String temp = t2.getOwnerId() == null? t2.getValue() : t2.getOwnerId();
			loadVariable(temp, reg);
			temp = t1.getOwnerId() == null? t1.getValue() : t1.getOwnerId();
			storeVariable(temp, reg);
		}
		
		
		//if(nextArraySize != null){
		//	System.err.println(">>>> TAMANHO NEXT ARRAY PARA MULTIPLICAR POR 8: " + nextArraySize);
		//}
		
		
	}
	
	public void createCodeForIF(Type t1, Type t2, String operator){
		String op = "SUB ";
		String desvio = "";
		if(operator.equals("==")) desvio = "BNEQZ ";
		else if(operator.equals("!=")) desvio = "BEQZ ";
		else if(operator.equals("<=")) desvio = "BGTZ ";
		else if(operator.equals(">=")) desvio = "BLTZ ";
		else if(operator.equals("<")) desvio = "BGTEQZ ";
		else if(operator.equals(">")) desvio = "BLTEQZ ";
		
		String temp1 = t1.getOwnerId() != null? t1.getOwnerId() : t1.getValue();
		String temp2 = t2.getOwnerId() != null? t2.getOwnerId() : t2.getValue();
		
		//System.err.println(t1.getOwnerId() + t1.getValue());
		//System.err.println(t2.getOwnerId() + t2.getValue());
		
		String reg1 = nextAvailableRegister();
		loadVariable(temp1, reg1);
		String reg2 = nextAvailableRegister();
		loadVariable(temp2, reg2);
		String reg3 = nextAvailableRegister();
		
		code += nextMemPosition() + "\t" + op + reg3 + ", " + reg1 + ", " + reg2 + "\n";
		
		//String nextPos = (memoryPos + 8) + "";
		code += nextMemPosition() + "\t" + desvio + reg3 + ", " + "<POS>" + "\n";
		
	}
	
	public void createCodeForIFNoRelationalExpression(Type type){
		String temp = type.getOwnerId() != null? type.getOwnerId() : type.getValue();
		
		String reg1 = nextAvailableRegister();
		loadVariable(temp, reg1);
		
		//String nextPos = (memoryPos + 8) + "";
		code += nextMemPosition() + "\t" + "BNEQZ " + reg1 + ", " + "<POS>" + "\n";
		
		
	}
	
	public String getCodeExpression(String operation, Type t1, Type t2){
		String returnCode = "";
		String op = "";
		if(operation.equals("+")) op = "ADD ";
		else if(operation.equals("-")) op = "SUB ";
		else if(operation.equals("*")) op = "MUL ";
		else if(operation.equals("/")) op = "DIV ";
		else if(operation.equals("%")) op = "MOD ";
		
		String temp1 = t1.getValue() == null? t1.getOwnerId() : t1.getValue();
		String temp2 = t2.getValue() == null? t2.getOwnerId() : t2.getValue();
		
		String reg1 = nextAvailableRegister();
		String reg2 = nextAvailableRegister();
		String reg3 = nextAvailableRegister();
				
		
//		loadVariable(temp1, reg1);
		returnCode += nextMemPosition() + "\tLD " + reg1 + ", " + temp1 + "\n";
//		loadVariable(temp2, reg2);
		returnCode += nextMemPosition() + "\tLD " + reg2 + ", " + temp2 + "\n";
		
		returnCode += nextMemPosition() + "\t" + op + reg3 + ", " + reg1 + ", " + reg2 + "\n";
		
		//System.err.println(t1 + "-" + t1.getOwnerId() + " " + t1.getValue());
		//System.err.println(op);
		//System.err.println(t2 + "-" + t2.getOwnerId() + " " + t2.getValue());
		return returnCode;
	}
	
	public void createExpressionCode(Object declarado, Object expression){
		Type t1 = (Type) declarado;
		Type exp = (Type) expression;
		
		code += exp.getCurrentCode();
		
		String reg = lastRegister();
		storeVariable(t1.getOwnerId(), reg);
		
	}
	
	/** LD SP, 1000
	 * ADD SP, SP, #this_method_size
	 * ST *SP, #position_come_back_SUB
	 * BR #called_method_position
	 * SUB SP, SP, #this_method_size
	 * */
	public void creatMethodCall(Object type){
		Type methodType = (Type) type;
		String calledMethod = methodType.getOwnerId() == null? methodType.getValue() : methodType.getOwnerId();
		
		code += nextMemPosition() + "\tADD SP, SP, #" + currMethod + "\n";
		code += nextMemPosition() + "\tST *SP, " + (memoryPos+8) + "\n";
		code += nextMemPosition() + "\tBR " + methods.get(calledMethod) + "\n";
		code += nextMemPosition() + "\tSUB SP, SP #" + currMethod + "\n";
		
		
	}
	
	public void finalizeMethod(Type type){
		//System.err.println(type.getName() + type.getOwnerId() + type.getValue() + type.getCurrentCode());
		//System.err.println(currMethod);
		
		String temp = type.getValue() == null ? type.getOwnerId() : type.getValue();
		if(temp != null){
			String reg = nextAvailableRegister();
			loadVariable(temp, reg);
			
			methodReturns.put(currMethod, reg);
		}
		//System.err.println(methodReturns);
		
		code += nextMemPosition() + "\tBR *0(SP)\n";
	}
	
	public void setPositionStartElse(){
		code = code.replace("<POS>", memoryPos+"");
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public void writeFile(){
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(new File(FILE_PATH)));
			writer.write(code);
			System.out.println(">> Successful Code Generation");
		} catch (IOException e) {
			System.err.println(e.getMessage());
		} finally {
			try {
				writer.close();
			} catch (IOException e) {
				System.err.println(e.getMessage());
			}
		}
	}

}
