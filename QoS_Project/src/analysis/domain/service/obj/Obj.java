package analysis.domain.service.obj;

import java.util.ArrayList;
import java.util.List;

public class Obj {
	public String type;
	public String name;
	public List<String> properties;

	public Obj(String type) {
		this.type = type.trim();
		properties = new ArrayList<String>();
	}

	public Obj(String type, String name) {
		this.type = type.trim();
		this.name = name.trim();
		properties = new ArrayList<String>();
	}

	public boolean compareType(Obj other) {
		return this.type.equals(other.type);
	}
	
	public void addProperty(String prop) {
		this.properties.add(prop);
	}

	@Override
	public boolean equals(Object obj) {
		return toString().equalsIgnoreCase(obj.toString());
	}

	@Override
	public String toString() {
		return name + "--" + type;
	}

	@Override
	public int hashCode() {
		return toString().hashCode();
	}

	public boolean canBeGroundedTo(Obj other) {
		String otherType = other.type;
		String[] parts = otherType.split("-");
		for (String t : parts) {
			if (t.trim().equals(this.type))
				return true;
		}
		return (this.type.equals(other.type));
	}
}
