package analysis.domain.service.grounding;

import java.util.ArrayList;
import java.util.List;

import analysis.domain.service.Funct;
import analysis.domain.service.obj.Obj;

public class GroundedFunct {
	public Funct funct;
	public List<Obj> arguments;
	
	public GroundedFunct(Funct funct) {
		this.funct = funct;
		arguments = new ArrayList<>();
	}

	public void addObjs(Obj o) {
		this.arguments.add(o);
	}

	@Override
	public String toString() {
		return funct.name + "-" + arguments.toString();
	}

	@Override
	public boolean equals(Object obj) {
		return toString().equalsIgnoreCase(obj.toString());
	}

	@Override
	public int hashCode() {
		return toString().hashCode();
	}
}
