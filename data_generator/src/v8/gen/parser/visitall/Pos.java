package v8.gen.parser.visitall;

import java.util.HashSet;
import java.util.Set;

public class Pos {
	String id;
	Set<Pos> neighbors;
	
	public String getId() {
		return id;
	}

	public Pos(String id) {
		this.id = id;
		this.neighbors = new HashSet<>();
	}

	public void addNeighbor(Pos n) {
		this.neighbors.add(n);
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
		return toString().equals(obj.toString());
	}
}
