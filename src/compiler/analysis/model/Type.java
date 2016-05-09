package compiler.analysis.model;

public class Type {
	
	private String name;
	
	public Type(String name){
		this.name = name;
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

}
