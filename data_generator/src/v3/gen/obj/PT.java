package v3.gen.obj;

import java.util.HashSet;
import java.util.Set;

public abstract class PT {
	protected String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setId(int id) {
		this.id = getClass().getSimpleName() + "-" + id;
	}

	public Set<String> getActions() {
		return new HashSet<>();
	};

	public Set<String> getParentActions1() {
		return new HashSet<>();
	}

	public Set<String> getParentActions2() {
		return new HashSet<>();
	}

	public String getState() {
		return null;
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}

	@Override
	public String toString() {
		return id;
	}

	@Override
	public boolean equals(Object obj) {
		return obj.toString().equals(this.toString());
	}
}
