package compiler.analysis.model;

public class Type {
	
	private String name;
	private String ownerId;
	private String value;
	private String currentCode;
	
	public Type(String name){
		this.name = name;
	}
	public Type(String name, String value){
		this.name = name;
		this.value = value;
	}
	public Type(String name, String value, String ownerId){
		this.name = name;
		this.value = value;
		this.ownerId = ownerId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return "{ Type " + getName() + " }";
	}
	
	@Override
	public boolean equals(Object obj) {
		Type other = (Type) obj;
		return this.getName().equals(other.getName());
	}

	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	public String getCurrentCode() {
		return currentCode;
	}
	
	public void setCurrentCode(String currentCode) {
		this.currentCode = currentCode;
	}

}
