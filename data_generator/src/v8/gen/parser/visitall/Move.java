package v8.gen.parser.visitall;

import java.util.ArrayList;
import java.util.List;

import v5.gen.supply_chain.Action;

public class Move extends Action {
	List<Pos> froms;
	List<Pos> tos;
	int index;

	public Move() {
		froms = new ArrayList<>();
		tos = new ArrayList<>();
	}

	public Move(String txt) {
		super();
		String t = txt.replace("(", "").replace(")", "").trim();
		this.setName(t.replace(" ", "-"));
		String[] parts = this.getName().split("-");
		this.index = Integer.parseInt(parts[1]);
		froms = new ArrayList<>();
		tos = new ArrayList<>();
		for (int i = 2; i < parts.length - 1; i++) {
			froms.add(new Pos(parts[i]));
		}
		tos.add(new Pos(parts[parts.length - 1]));
		toPreconds();
		toEffects();
	}

	public Move(List<Pos> froms, List<Pos> tos, int index) {
		super();
		this.froms = froms;
		this.tos = tos;
		this.index = index;
		this.setName("move-" + index);
		toPreconds();
		toEffects();
	}

	public Move(List<Pos> pos, int index) {
		super();
		int last = pos.size() - 1;
		if (pos.get(index).neighbors.contains(pos.get(last))) {
			this.index = index;
			String suffix = pos.toString().replace("[", "").replace("]", "").replace(", ", "-").replace(" ", "").trim();
			this.setName("move-" + index + "-" + suffix);
			froms = new ArrayList<>();
			tos = new ArrayList<>();

			froms.addAll(pos.subList(0, last));
			tos.add(pos.get(last));
			toPreconds();
			toEffects();
		}
	}

	private void toEffects() {
		StringBuffer to = new StringBuffer("");
		for (int i = 0; i < froms.size(); i++) {
			Pos p = froms.get(i);
			if (i == index)
				p = tos.get(0);
			to.append("-" + p.id);
		}
		addPEffect("at-robot" + to);
		addPEffect("visited" + to);

		StringBuffer from = new StringBuffer("");
		for (Pos pos : froms)
			from.append("-" + pos.id);
		addNEffect("at-robot" + from);
	}

	private void toPreconds() {
		StringBuffer pre = new StringBuffer("at-robot");
		for (Pos pos : froms)
			pre.append("-" + pos.id);
		addPre(pre.toString());
	}

	@Override
	public int hashCode() {
		return toString().hashCode();
	}

	@Override
	public String toString() {
		return this.getName() + "-" + froms.toString() + "-" + tos.toString();
//		return this.getName();
	}

	@Override
	public boolean equals(Object obj) {
//		return super.equals(obj);
		return toString().equals(obj.toString());
	}
}
