package v8.gen.parser.visitall;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import v8.gen.parser.GroundingThread;

public class VisitallGroundingThread extends GroundingThread {
	Set<List<Pos>> combs;

	public VisitallGroundingThread(int id) {
		super(id);
		combs = new HashSet<>();
	}

	public void addComb(List<Pos> comb) {
		this.combs.add(comb);
	}

	@Override
	public void run() {
		for (List<Pos> p : combs) {
			Move m0 = new Move(p, 0);
			if (m0.getName() != null)
				addGroundedAction(m0);
			Move m1 = new Move(p, 1);
			if (m1.getName() != null)
				addGroundedAction(m1);
			Move m2 = new Move(p, 1);
			if (m2.getName() != null)
				addGroundedAction(m2);
		}
	}

	@Override
	public void info() {
		System.out.println("VisitallGroundingThread +" + this.id + ": " + combs.size());
	}

}
