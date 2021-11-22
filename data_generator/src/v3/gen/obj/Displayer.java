package v3.gen.obj;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import v3.gen.generator.ObjectGenerator;

public class Displayer extends PT {
	Set<String> locs;
	
	private Displayer() {
	}
	public Displayer(Collection<String> locs, int index) {
		this.locs = new HashSet<>(locs);
		this.setId(index);
	}
	

	@Override
	public Set<String> getActions() {
		Set<String> result = new HashSet<String>();
		result.addAll(genDisplay());

		return result;
	}

	private Collection<String> genDisplay() {
		Set<String> result = new HashSet<>();
		for (String l : locs) {
			String a = "(:action display-" + getId() + "-" + l + "\n";
			a += "\t:precondition  (and" + "\n";
			a += "\t\t(assident-message-sent-" + getId() + "-" + l + ")" + "\n";
			a += "\t)\n";

			a += "\t:effect  (and" + "\n";
			a += "\t\t(accident-message-displayed-" + l + ")" + "\n";
			a += "\t)\n";
			a += ")";
			result.add(a);
			
			ObjectGenerator.predicates.add(("\t\t(assident-message-sent-" + getId() + "-" + l + ")").trim());
			ObjectGenerator.predicates.add(("\t\t(accident-message-displayed-" + l + ")").trim());
			
		}
		return result;
	}

	@Override
	public String getState() {
		return null;
	}
	@Override
	public Set<String> getParentActions1() {
		return getActions();
	}
	@Override
	public Set<String> getParentActions2() {
		return getActions();
	}

}
