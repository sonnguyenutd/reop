package v3.gen.obj;

public class Route {
	public String from;
	public String to;
	public boolean isAvailable;

	public Route(String from, String to) {
		this.from = from;
		this.to = to;
	}

	public String getFrom() {
		return from;
	}

	public String getTo() {
		return to;
	}

	@Override
	public String toString() {
		return from + "-" + to;
	}

	@Override
	public int hashCode() {
		return toString().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return this.toString().equals(obj.toString());
	}
}
