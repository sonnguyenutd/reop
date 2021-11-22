package analysis.domain.service.grounding;

import java.util.ArrayList;
import java.util.List;

public class GroundedNumericChange {
	public String content;
	public List<GroundedFunction> participants;

	public GroundedNumericChange() {
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
