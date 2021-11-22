package gen.general;

import gen.state.State;

public class Obj {
	String id;
	State state;
	String type;

	public void setId(String id) {
		this.id = id;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setState(State state) {
		this.state = state;
	}

	public Obj() {
		state = new State();
	}

	public Obj(String id, String type) {
		this.id = id;
		this.type = type;
		state = new State();
	}

	public String getId() {
		return id;
	}

	public State getState() {
		return state;
	}

	public String getType() {
		return type;
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return id.equals(((Obj) obj).id);
	}

	@Override
	public String toString() {
		return id + " : " + state.getDescriptor();
	}

}
