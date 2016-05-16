package compiler.analysis;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import compiler.analysis.model.Type;

public class CodeGenerator {

	private final String FILE_PATH = "files/output.assembly";
	
	public String nextMethod = null; 
	
	private String code;
	private int pilhaPos;
	private int memoryPos;
	private int register;
	private String nextIdentifier = null;
	private String currReg = null;
	
	public CodeGenerator(){
		this.pilhaPos = 1000;
		this.memoryPos = 100;
		this.register = 0;
		this.code = "";
	}
	
	private String nextAvailableRegister(){
		return "R" + register++;
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
		String reg = nextAvailableRegister();
		loadVariable(type.getValue(), reg);
		storeVariable(type.getOwnerId(), reg);
	}
	
	public void createMethodAndParams(String identifier, Object params){
		if(params != null){
			code += nextMemPosition() + " " + identifier + ":\n";
			//Caso eu queira adicionar os parametros do metodo a registradores assim q instancio o metodo
//			HashMap<String, Type> parametros = (HashMap<String, Type>) params;
//			for(String n : parametros.keySet()){
//				String reg = nextAvailableRegister();
//				loadVariable(n, reg);
//			}
		}else{
			code += nextMemPosition() + " " + identifier + ":\n";
		}
	}
	
	public void loadAndStoreValuesAssignment(Object o1, Object o2){
		Type t1 = (Type) o1;
		Type t2 = (Type) o2;
		
		String reg = nextAvailableRegister();
		String temp = t2.getOwnerId() == null? t2.getValue() : t2.getOwnerId();
		loadVariable(temp, reg);
		temp = t1.getOwnerId() == null? t1.getValue() : t1.getOwnerId();
		storeVariable(temp, reg);
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
