package compiler.analysis.model;

public class Type {
	
	private String name;
	private String ownerId;
	
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

	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

}
