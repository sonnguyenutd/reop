package v5.gen.transport2;

public class Package extends Object{
	String name;
	Loc currLoc;
	Loc desLoc;
	public Package(String name, Loc currLoc, Loc des) {
		this.name = name;
		this.currLoc = currLoc;
		this.desLoc = des;
	}
	@Override
	public String toString() {
		return name;
	}
	@Override
	public int hashCode() {
		return name.hashCode();
	}
	@Override
	public boolean equals(Object obj) {
		return toString().equals(obj.toString());
	}
	public String getName() {
		return name;
	}
	public Loc getCurrLoc() {
		return currLoc;
	}
	public Loc getDesLoc() {
		return desLoc;
	}
}
