package v6.gen.analysis.action;

public class Parameter {
	public String type;
	public String name;

	public Parameter(String type, String name) {
		this.type = type;
		this.name = name;
	}

	public boolean compareType(Parameter other) {
		return this.type.equals(other.type);
	}

	@Override
	public boolean equals(Object obj) {
		return toString().equals(obj.toString());
	}

	@Override
	public String toString() {
		return name + " - " + type;
	}
}
