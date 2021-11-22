package analysis.domain.service.grounding;

import java.util.ArrayList;
import java.util.List;

public class GroundedNumericCondition {
	public String content;
	public List<GroundedFunction> participants;

	public GroundedNumericCondition() {
		participants = new ArrayList<>();
	}

	public void addParticipant(GroundedFunction p) {
		this.participants.add(p);
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	
	@Override
	public String toString() {
		return content;
	}
}
