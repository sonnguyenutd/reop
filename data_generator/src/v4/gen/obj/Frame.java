package v4.gen.obj;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import v3.gen.obj.PT;
import v4.gen.generator.ObjectGenerator;

public class Frame extends PT {
	Collection<Tag> tags;
	Collection<Order> orders;

	public Frame() {
		super();
	}

	public Frame(Collection<Tag> tags, Collection<Order> orders) {
		this.tags = tags;
		this.orders = orders;
	}

	public void setOrders(Collection<Order> orders) {
		this.orders = orders;
	}

	public void setTags(Collection<Tag> tags) {
		this.tags = tags;
	}

	@Override
	public Set<String> getActions() {
		Set<String> result = new HashSet<String>();
		result.addAll(genPlaceTag());
		result.addAll(genInitTag());
//		weld-adjust-frame
		result.addAll(genAdjustFrame());
//		soften-frame
		result.addAll(genSoftFrame());
		// correct-frame
		result.addAll(genCorrectFrame());
//		start-heating-treatment
		result.addAll(genStartHeatingTreatment());
		// wash-frame
		result.addAll(genWashFrame());
//		start-surface-treatment
		result.addAll(genStartSurfaceTreatment());
//		start-grinding
		result.addAll(genStartGrinding());
//		transport-to-painting-area
		result.addAll(genTransportToPainting());
//		custom-pain
		result.addAll(genCustomPain());
//		dry
		result.addAll(genDry());
//		assemble-parts
		result.addAll(genAssembleParts());
//		store-bike
		result.addAll(genStoreBike());
		return result;
	}

	@Override
	public Set<String> getParentActions1() {
		Set<String> result = new HashSet<String>();
		result.addAll(genPlaceTag());
		result.addAll(genInitTag());

//		;;Weld and adjust frame tubes
//	    ;;weld-adjust-frame + soften-frame + correct-frame
//	    ;;start-heating-treatment + wash-frame + start-surface-treatment
//	    ;;transport-to-painting-area + custom-pain + dry + assemble-parts
//	    ;;store-bike
		result.addAll(genA1());

		return result;
	}

	private Collection<String> genA1() {
		Set<String> result = new HashSet<String>();
		for (Tag t : tags) {
			String a = "(:action dry-" + getId() + "-" + t.getId() + "\n";
			a += "\t:precondition  (and" + "\n";
			a += "\t\t(tag-initialized-" + getId() + "-" + t.getId() + ")" + "\n";
			a += "\t)\n";

			a += "\t:effect  (and" + "\n";

			a += "\t\t(frame-adjusted-" + getId() + ")" + "\n";
			a += "\t\t(frame-softened-" + getId() + ")" + "\n";
			a += "\t\t(frame-corrected-" + getId() + ")" + "\n";
			a += "\t\t(heating-treatment-done-" + getId() + ")" + "\n";
			a += "\t\t(frame-washed-" + getId() + ")" + "\n";
			a += "\t\t(surface-treatment-done-" + getId() + ")" + "\n";
			a += "\t\t(grinding-done-" + getId() + ")" + "\n";
			a += "\t\t(arrived-painting-area-" + getId() + ")" + "\n";
			a += "\t\t(pained-" + getId() + ")" + "\n";
			a += "\t\t(dried-" + getId() + ")" + "\n";
			a += "\t\t(assembled-" + getId() + ")" + "\n";
			a += "\t\t(stored-" + getId() + ")" + "\n";

			a += "\t\t(status-adjusted-" + t.getId() + ")" + "\n";
			a += "\t\t(status-softened-" + t.getId() + ")" + "\n";
			a += "\t\t(status-frame-corrected-" + t.getId() + ")" + "\n";
			a += "\t\t(status-heating-treatment-" + t.getId() + ")" + "\n";
			a += "\t\t(status-washed-" + t.getId() + ")" + "\n";
			a += "\t\t(status-surface-treatment-" + t.getId() + ")" + "\n";
			a += "\t\t(status-grinding-" + t.getId() + ")" + "\n";
			a += "\t\t(status-pained-" + t.getId() + ")" + "\n";
			a += "\t\t(status-dried-" + t.getId() + ")" + "\n";
			a += "\t\t(status-assembled-" + t.getId() + ")" + "\n";
			a += "\t\t(status-stored-" + t.getId() + ")" + "\n";

			a += "\t)\n";
			a += ")";

			ObjectGenerator.predicates.add(("\t\t(tag-initialized-" + getId() + "-" + t.getId() + ")").trim());
			ObjectGenerator.predicates.add(("\t\t(frame-adjusted-" + getId() + ")").trim());
			ObjectGenerator.predicates.add(("\t\t(frame-softened-" + getId() + ")").trim());
			ObjectGenerator.predicates.add(("\t\t(frame-corrected-" + getId() + ")").trim());
			ObjectGenerator.predicates.add(("\t\t(heating-treatment-done-" + getId() + ")").trim());
			ObjectGenerator.predicates.add(("\t\t(frame-washed-" + getId() + ")").trim());
			ObjectGenerator.predicates.add(("\t\t(surface-treatment-done-" + getId() + ")").trim());
			ObjectGenerator.predicates.add(("\t\t(grinding-done-" + getId() + ")").trim());
			ObjectGenerator.predicates.add(("\t\t(arrived-painting-area-" + getId() + ")").trim());
			ObjectGenerator.predicates.add(("\t\t(pained-" + getId() + ")").trim());
			ObjectGenerator.predicates.add(("\t\t(dried-" + getId() + ")").trim());
			ObjectGenerator.predicates.add(("\t\t(assembled-" + getId() + ")").trim());

			ObjectGenerator.predicates.add(("\t\t(status-adjusted-" + t.getId() + ")").trim());
			ObjectGenerator.predicates.add(("\t\t(status-softened-" + t.getId() + ")").trim());
			ObjectGenerator.predicates.add(("\t\t(status-frame-corrected-" + t.getId() + ")").trim());
			ObjectGenerator.predicates.add(("\t\t(status-heating-treatment-" + t.getId() + ")").trim());
			ObjectGenerator.predicates.add(("\t\t(status-washed-" + t.getId() + ")").trim());
			ObjectGenerator.predicates.add(("\t\t(status-surface-treatment-" + t.getId() + ")").trim());
			ObjectGenerator.predicates.add(("\t\t(status-grinding-" + t.getId() + ")").trim());
			ObjectGenerator.predicates.add(("\t\t(status-pained-" + t.getId() + ")").trim());
			ObjectGenerator.predicates.add(("\t\t(status-dried-" + t.getId() + ")").trim());
			ObjectGenerator.predicates.add(("\t\t(status-assembled-" + t.getId() + ")").trim());
			ObjectGenerator.predicates.add(("\t\t(status-stored-" + t.getId() + ")").trim());

			result.add(a);
		}
		return result;
	}

	@Override
	public Set<String> getParentActions2() {
		return getParentActions1();
	}

	@Override
	public String getState() {
		return null;
	}

	private Collection<String> genStoreBike() {
		Set<String> result = new HashSet<String>();
		for (Tag t : tags) {
			String a = "(:action store-bike-" + getId() + "-" + t.getId() + "\n";
			a += "\t:precondition  (and" + "\n";
			a += "\t\t(tag-initialized-" + getId() + "-" + t.getId() + ")" + "\n";
			a += "\t\t(assembled-" + getId() + ")" + "\n";
			a += "\t)\n";

			a += "\t:effect  (and" + "\n";
			a += "\t\t(stored-" + getId() + ")" + "\n";
			a += "\t\t(status-stored-" + t.getId() + ")" + "\n";
			a += "\t)\n";
			a += ")";

			ObjectGenerator.predicates.add(("\t\t(tag-initialized-" + getId() + "-" + t.getId() + ")").trim());
			ObjectGenerator.predicates.add(("\t\t(assembled-" + getId() + ")").trim());
			ObjectGenerator.predicates.add(("\t\t(stored-" + getId() + ")").trim());
			ObjectGenerator.predicates.add(("\t\t(status-stored-" + t.getId() + ")").trim());

			result.add(a);
		}
		return result;
	}

	private Collection<String> genAssembleParts() {
		Set<String> result = new HashSet<String>();
		for (Tag t : tags) {
			String a = "(:action assemble-parts-" + getId() + "-" + t.getId() + "\n";
			a += "\t:precondition  (and" + "\n";
			a += "\t\t(tag-initialized-" + getId() + "-" + t.getId() + ")" + "\n";
			a += "\t\t(dried-" + getId() + ")" + "\n";
			a += "\t)\n";

			a += "\t:effect  (and" + "\n";
			a += "\t\t(assembled-" + getId() + ")" + "\n";
			a += "\t\t(status-assembled-" + t.getId() + ")" + "\n";
			a += "\t)\n";
			a += ")";

			ObjectGenerator.predicates.add(("\t\t(tag-initialized-" + getId() + "-" + t.getId() + ")").trim());
			ObjectGenerator.predicates.add(("\t\t(dried-" + getId() + ")").trim());
			ObjectGenerator.predicates.add(("\t\t(assembled-" + getId() + ")").trim());
			ObjectGenerator.predicates.add(("\t\t(status-assembled-" + t.getId() + ")").trim());

			result.add(a);
		}
		return result;
	}

	private Collection<String> genDry() {
		Set<String> result = new HashSet<String>();
		for (Tag t : tags) {
			String a = "(:action dry-" + getId() + "-" + t.getId() + "\n";
			a += "\t:precondition  (and" + "\n";
			a += "\t\t(tag-initialized-" + getId() + "-" + t.getId() + ")" + "\n";
			a += "\t\t(pained-" + getId() + ")" + "\n";
			a += "\t)\n";

			a += "\t:effect  (and" + "\n";
			a += "\t\t(dried-" + getId() + ")" + "\n";
			a += "\t\t(status-dried-" + t.getId() + ")" + "\n";
			a += "\t)\n";
			a += ")";

			ObjectGenerator.predicates.add(("\t\t(tag-initialized-" + getId() + "-" + t.getId() + ")").trim());
			ObjectGenerator.predicates.add(("\t\t(dried-" + getId() + ")").trim());
			ObjectGenerator.predicates.add(("\t\t(pained-" + getId() + ")").trim());
			ObjectGenerator.predicates.add(("\t\t(status-dried-" + t.getId() + ")").trim());

			result.add(a);
		}
		return result;
	}

	private Collection<String> genCustomPain() {
		Set<String> result = new HashSet<String>();
		for (Tag t : tags) {
			String a = "(:action custom-pain-" + getId() + "-" + t.getId() + "\n";
			a += "\t:precondition  (and" + "\n";
			a += "\t\t(tag-initialized-" + getId() + "-" + t.getId() + ")" + "\n";
			a += "\t\t(arrived-painting-area-" + getId() + ")" + "\n";
			a += "\t)\n";

			a += "\t:effect  (and" + "\n";
			a += "\t\t(pained-" + getId() + ")" + "\n";
			a += "\t\t(status-pained-" + t.getId() + ")" + "\n";
			a += "\t)\n";
			a += ")";

			ObjectGenerator.predicates.add(("\t\t(tag-initialized-" + getId() + "-" + t.getId() + ")").trim());
			ObjectGenerator.predicates.add(("\t\t(arrived-painting-area-" + getId() + ")").trim());
			ObjectGenerator.predicates.add(("\t\t(pained-" + getId() + ")").trim());
			ObjectGenerator.predicates.add(("\t\t(status-pained-" + t.getId() + ")").trim());

			result.add(a);
		}
		return result;
	}

	private Collection<String> genTransportToPainting() {
		Set<String> result = new HashSet<String>();
		for (Tag t : tags) {
			String a = "(:action transport-to-painting-area-" + getId() + "\n";
			a += "\t:precondition  (and" + "\n";
			a += "\t\t(tag-initialized-" + getId() + "-" + t.getId() + ")" + "\n";
			a += "\t\t(surface-treatment-done-" + getId() + ")" + "\n";
			a += "\t\t(grinding-done-" + getId() + ")" + "\n";
			a += "\t)\n";

			a += "\t:effect  (and" + "\n";
			a += "\t\t(arrived-painting-area-" + getId() + ")" + "\n";
			a += "\t)\n";
			a += ")";

			ObjectGenerator.predicates.add(("\t\t(surface-treatment-done-" + getId() + ")").trim());
			ObjectGenerator.predicates.add(("\t\t(grinding-done-" + getId() + ")").trim());
			ObjectGenerator.predicates.add(("\t\t(arrived-painting-area-" + getId() + ")").trim());

			result.add(a);
		}
		return result;
	}

	private Collection<String> genStartGrinding() {
		Set<String> result = new HashSet<String>();
		for (Tag t : tags) {
			String a = "(:action start-grinding-" + getId() + "-" + t.getId() + "\n";
			a += "\t:precondition  (and" + "\n";
			a += "\t\t(tag-initialized-" + getId() + "-" + t.getId() + ")" + "\n";
			a += "\t\t(frame-washed-" + getId() + ")" + "\n";
			a += "\t)\n";

			a += "\t:effect  (and" + "\n";
			a += "\t\t(grinding-done-" + getId() + ")" + "\n";
			a += "\t\t(status-grinding-" + t.getId() + ")" + "\n";
			a += "\t)\n";
			a += ")";

			ObjectGenerator.predicates.add(("\t\t(tag-initialized-" + getId() + "-" + t.getId() + ")").trim());
			ObjectGenerator.predicates.add(("\t\t(frame-washed-" + getId() + ")").trim());
			ObjectGenerator.predicates.add(("\t\t(grinding-done-" + getId() + ")").trim());
			ObjectGenerator.predicates.add(("\t\t(status-grinding-" + t.getId() + ")").trim());

			result.add(a);
		}
		return result;
	}

	private Collection<String> genStartSurfaceTreatment() {
		Set<String> result = new HashSet<String>();
		for (Tag t : tags) {
			String a = "(:action start-surface-treatment-" + getId() + "-" + t.getId() + "\n";
			a += "\t:precondition  (and" + "\n";
			a += "\t\t(tag-initialized-" + getId() + "-" + t.getId() + ")" + "\n";
			a += "\t\t(frame-washed-" + getId() + ")" + "\n";
			a += "\t)\n";

			a += "\t:effect  (and" + "\n";
			a += "\t\t(surface-treatment-done-" + getId() + ")" + "\n";
			a += "\t\t(status-surface-treatment-" + t.getId() + ")" + "\n";
			a += "\t)\n";
			a += ")";

			ObjectGenerator.predicates.add(("\t\t(tag-initialized-" + getId() + "-" + t.getId() + ")").trim());
			ObjectGenerator.predicates.add(("\t\t(heating-treatment-done-" + getId() + ")").trim());
			ObjectGenerator.predicates.add(("\t\t(frame-washed-" + getId() + ")").trim());
			ObjectGenerator.predicates.add(("\t\t(status-surface-treatment-" + t.getId() + ")").trim());

			result.add(a);
		}
		return result;
	}

	private Collection<String> genWashFrame() {
		Set<String> result = new HashSet<String>();
		for (Tag t : tags) {
			String a = "(:action wash-frame-" + getId() + "-" + t.getId() + "\n";
			a += "\t:precondition  (and" + "\n";
			a += "\t\t(tag-initialized-" + getId() + "-" + t.getId() + ")" + "\n";
			a += "\t\t(heating-treatment-done-" + getId() + ")" + "\n";
			a += "\t)\n";

			a += "\t:effect  (and" + "\n";
			a += "\t\t(frame-washed-" + getId() + ")" + "\n";
			a += "\t\t(status-washed-" + t.getId() + ")" + "\n";
			a += "\t)\n";
			a += ")";

			ObjectGenerator.predicates.add(("\t\t(tag-initialized-" + getId() + "-" + t.getId() + ")").trim());
			ObjectGenerator.predicates.add(("\t\t(heating-treatment-done-" + getId() + ")").trim());
			ObjectGenerator.predicates.add(("\t\t(frame-washed-" + getId() + ")").trim());
			ObjectGenerator.predicates.add(("\t\t(status-washed-" + t.getId() + ")").trim());

			result.add(a);
		}
		return result;
	}

	private Collection<String> genStartHeatingTreatment() {
		Set<String> result = new HashSet<String>();
		for (Tag t : tags) {
			String a = "(:action correct-frame-" + getId() + "-" + t.getId() + "\n";
			a += "\t:precondition  (and" + "\n";
			a += "\t\t(tag-initialized-" + getId() + "-" + t.getId() + ")" + "\n";
			a += "\t\t(frame-corrected-" + getId() + ")" + "\n";
			a += "\t)\n";

			a += "\t:effect  (and" + "\n";
			a += "\t\t(heating-treatment-done-" + getId() + ")" + "\n";
			a += "\t\t(status-heating-treatment-" + t.getId() + ")" + "\n";
			a += "\t)\n";
			a += ")";

			ObjectGenerator.predicates.add(("\t\t(tag-initialized-" + getId() + "-" + t.getId() + ")").trim());
			ObjectGenerator.predicates.add(("\t\t(frame-corrected-" + getId() + ")").trim());
			ObjectGenerator.predicates.add(("\t\t(heating-treatment-done-" + getId() + ")").trim());
			ObjectGenerator.predicates.add(("\t\t(status-heating-treatment-" + t.getId() + ")").trim());

			result.add(a);
		}
		return result;
	}

	private Collection<String> genCorrectFrame() {
		Set<String> result = new HashSet<String>();
		for (Tag t : tags) {
			String a = "(:action correct-frame-" + getId() + "-" + t.getId() + "\n";
			a += "\t:precondition  (and" + "\n";
			a += "\t\t(tag-initialized-" + getId() + "-" + t.getId() + ")" + "\n";
			a += "\t\t(frame-softened-" + getId() + ")" + "\n";
			a += "\t\t(frame-adjusted-" + getId() + ")" + "\n";
			a += "\t)\n";

			a += "\t:effect  (and" + "\n";
			a += "\t\t(frame-corrected-" + getId() + ")" + "\n";
			a += "\t\t(status-frame-corrected-" + t.getId() + ")" + "\n";
			a += "\t)\n";
			a += ")";

			ObjectGenerator.predicates.add(("\t\t(tag-initialized-" + getId() + "-" + t.getId() + ")").trim());
			ObjectGenerator.predicates.add(("\t\t(frame-softened-" + getId() + ")").trim());
			ObjectGenerator.predicates.add(("\t\t(frame-adjusted-" + getId() + ")").trim());
			ObjectGenerator.predicates.add(("\t\t(frame-corrected-" + getId() + ")").trim());
			ObjectGenerator.predicates.add(("\t\t(status-frame-corrected-" + t.getId() + ")").trim());

			result.add(a);
		}
		return result;
	}

	private Collection<String> genSoftFrame() {
		Set<String> result = new HashSet<String>();
		for (Tag t : tags) {
			String a = "(:action soften-frame-" + getId() + "-" + t.getId() + "\n";
			a += "\t:precondition  (and" + "\n";
			a += "\t\t(tag-initialized-" + getId() + "-" + t.getId() + ")" + "\n";
			a += "\t)\n";

			a += "\t:effect  (and" + "\n";
			a += "\t\t(status-softened-" + t.getId() + ")" + "\n";
			a += "\t\t(frame-softened-" + getId() + ")" + "\n";
			a += "\t)\n";
			a += ")";

			ObjectGenerator.predicates.add(("\t\t(tag-initialized-" + getId() + "-" + t.getId() + ")").trim());
			ObjectGenerator.predicates.add(("\t\t(status-softened-" + t.getId() + ")").trim());
			ObjectGenerator.predicates.add(("\t\t(frame-softened-" + getId() + ")").trim());
			result.add(a);
		}
		return result;
	}

	private Collection<String> genAdjustFrame() {
		Set<String> result = new HashSet<String>();
		for (Tag t : tags) {
			String a = "(:action weld-adjust-frame-" + getId() + "-" + t.getId() + "\n";
			a += "\t:precondition  (and" + "\n";
			a += "\t\t(tag-initialized-" + getId() + "-" + t.getId() + ")" + "\n";
			a += "\t)\n";

			a += "\t:effect  (and" + "\n";
			a += "\t\t(status-adjusted-" + t.getId() + ")" + "\n";
			a += "\t\t(frame-adjusted-" + getId() + ")" + "\n";
			a += "\t)\n";
			a += ")";

			ObjectGenerator.predicates.add(("\t\t(status-adjusted-" + t.getId() + ")").trim());
			ObjectGenerator.predicates.add(("\t\t(tag-initialized-" + getId() + "-" + t.getId() + ")").trim());
			ObjectGenerator.predicates.add(("\t\t(frame-adjusted-" + getId() + ")").trim());
			result.add(a);
		}
		return result;
	}

	private Collection<String> genInitTag() {
		Set<String> result = new HashSet<String>();
		for (Tag t : tags) {
			String a = "(:action init-RFID-tag-" + getId() + "-" + t.getId() + "\n";
			a += "\t:precondition  (and" + "\n";
			a += "\t\t(tag-inserted-" + getId() + "-" + t.getId() + ")" + "\n";
			a += "\t)\n";

			a += "\t:effect  (and" + "\n";
			a += "\t\t(tag-initialized-" + getId() + "-" + t.getId() + ")" + "\n";
			a += "\t\t(status-ordered-" + t.getId() + ")" + "\n";
			a += "\t)\n";
			a += ")";

			ObjectGenerator.predicates.add(("\t\t(tag-inserted-" + getId() + "-" + t.getId() + ")").trim());
			ObjectGenerator.predicates.add(("\t\t(tag-initialized-" + getId() + "-" + t.getId() + ")").trim());
			ObjectGenerator.predicates.add(("\t\t(status-ordered-" + t.getId() + ")").trim());
			result.add(a);
		}
		return result;
	}

	private Collection<String> genPlaceTag() {
		Set<String> result = new HashSet<String>();
		for (Tag t : tags) {
			for (Order o : orders) {
				String a = "(:action place-RFID-tag-" + getId() + "-" + t.getId() + "-" + o.getId() + "\n";
				a += "\t:precondition  (and" + "\n";
				a += "\t\t(frame-ordered-" + o.getId() + "-" + getId() + ")" + "\n";
				a += "\t)\n";

				a += "\t:effect  (and" + "\n";
				a += "\t\t(tag-inserted-" + getId() + "-" + t.getId() + ")" + "\n";
				a += "\t\t(accociated-" + t.getId() + "-" + o.getId() + ")" + "\n";
				a += "\t)\n";
				a += ")";

				ObjectGenerator.predicates.add(("\t\t(frame-ordered-" + o.getId() + "-" + getId() + ")").trim());
				ObjectGenerator.predicates.add(("\t\t(tag-inserted-" + getId() + "-" + t.getId() + ")").trim());
				ObjectGenerator.predicates.add(("\t\t(accociated-" + t.getId() + "-" + o.getId() + ")").trim());
				result.add(a);
			}
		}
		return result;
	}

}
