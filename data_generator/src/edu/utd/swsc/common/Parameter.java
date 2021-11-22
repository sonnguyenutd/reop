package edu.utd.swsc.common;

public class Parameter {
	static int COUNTER = 1000000;
	String type;
	String name;

	public static Parameter create(String type) {
		String name = "para_" + COUNTER;
		COUNTER++;
		return new Parameter(type, name);
	}

	public String getType() {
		return type.replace(Service.REPLACE, "");
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int hashCode() {
		return toString().hashCode();
	}

	@Override
	public String toString() {
		return type + " - " + name;
	}

	@Override
	public boolean equals(Object obj) {
		return toString().equals(obj.toString());
	}

	public Parameter(String type, String name) {
		this.name = name;
		this.type = type;
	}
}
