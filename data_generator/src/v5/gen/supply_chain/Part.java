package v5.gen.supply_chain;

import java.util.HashSet;
import java.util.Set;

import gen.utils.Utils;

public class Part {
	String name;
	Set<Part> requiredParts;
	int deep;

	// Generated part
	Set<Supplier> suppliers;

	public void createSuppliers() {
		suppliers = new HashSet<Supplier>();
		int numOfSuppliers = Utils.randomNumber(1, ChainGenerator.MAX_SUPPLIERS_PER_PART);
//		System.out.println(name + " : REQUIRED PARTS: " + requiredParts.size() + " : NUM OF SUPPLIER: " + numOfSuppliers);
//		System.out.println("");
		while (suppliers.size() < numOfSuppliers) {
			String loc = Utils.generateLoc();
			Supplier s = new Supplier(name + "-S" + suppliers.size(), this, loc);
//			System.out.println(s.toString().trim());
			suppliers.add(s);
		}
//		System.out.println("\n-------------------------\n");
	}

	public Set<Supplier> getSuppliers() {
		return suppliers;
	}

	public Part(String name, Set<Part> requiredParts) {
		this.name = name;
		this.requiredParts = requiredParts;
	}

	public boolean containsPart(String pn) {
		Part p = new Part(pn, 0);
		return this.requiredParts.contains(p);
	}

	public void setDeep(int deep) {
		this.deep = deep;
	}

	public int getDeep() {
		return deep;
	}

	public Part(String name, int deep) {
		this.name = name;
		this.requiredParts = new HashSet<Part>();
		this.deep = deep;
	}

	public void addRequiredAPart(Part p) {
		this.requiredParts.add(p);
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setRequiredParts(Set<Part> requiredParts) {
		this.requiredParts = requiredParts;
	}

	public String getName() {
		return name;
	}

	public Set<Part> getRequiredParts() {
		return requiredParts;
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public boolean equals(Object obj) {
		return toString().equals(obj.toString());
	}
	
	public String getAvailable() {
		return "AVAILABLE-" + this.getName();
	}
	
	public String getCurrLoc(String loc) {
		return "CLOC-" + this.getName() + "-" + loc;
	}

//	public void print() {
//		System.out.println(this.name);
//	}
}
