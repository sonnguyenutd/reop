package gen.service;

import java.util.ArrayList;
import java.util.List;

import gen.general.Obj;

public class Service {
	String name;
	Obj provider;
	List<Obj> args;
	List<String> preconds;
	List<String> pEffects;
	List<String> nEffects;

	public Service() {
		args = new ArrayList<Obj>();
	}

	public String getName() {
		return name;

	}

	public Obj getProvider() {
		return provider;
	}

	@Override
	public int hashCode() {
		return (provider.getType() + "-" + provider.getId() + "-" + name).hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return toString().equals(obj.toString());
	}

	@Override
	public String toString() {
		String result = (provider.getType() + "-" + provider.getId() + "-" + name);
		result += "\n\t" + preconds.toString();
		result += "\n\t" + pEffects.toString();
		result += "\n\t" + nEffects.toString();
		return result;
	}

	public List<Obj> getArgs() {
		return args;
	}

	public void addArg(Obj o) {
		args.add(o);
	}

	public List<String> getnEffects() {
		return nEffects;
	}

	public List<String> getpEffects() {
		return pEffects;
	}

	public List<String> getPreconds() {
		return preconds;
	}

	public void setpEffects(List<String> pEffects) {
		this.pEffects = pEffects;
	}

	public void setnEffects(List<String> nEffects) {
		this.nEffects = nEffects;
	}

	public void setPreconds(List<String> preconds) {
		this.preconds = preconds;
	}

	public void setProvider(Obj provider) {
		this.provider = provider;
	}

	public void setArgs(List<Obj> args) {
		this.args = args;
	}

	public void setName(String name) {
		this.name = name;
	}
}
