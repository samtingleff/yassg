package com.tingleff.yassg.semantic;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

public class NamedEntity implements Serializable {
	private static final long serialVersionUID = -6785920144951566446L;

	// reference count in the source text
	private int count;

	// entity type
	// see http://www.alchemyapi.com/api/entity/types/ for a list of known types
	private String type;

	// entity text
	private String text;

	// score
	private double score;

	// subtype, such as Politician or Athlete (for Person)
	private List<String> subtypes;

	NamedEntity(int count,
			String type,
			String text,
			double score,
			List<String> subtypes) {
		this.count = count;
		this.type = type;
		this.text = text;
		this.score = score;
		this.subtypes = subtypes;
	}

	public NamedEntity() { }

	public int getCount() {
		return count;
	}

	public String getType() {
		return type;
	}

	public String getText() {
		return text;
	}

	public double getScore() {
		return score;
	}

	public Iterable<String> getSubtypeIterator() {
		return subtypes;
	}

	@Override
	public String toString() {
		return String.format("[type=%1$s;text=%2$s;score=%3$s;count=%4$s]",
				getType(),
				getText(),
				getScore(),
				getCount());
	}
}
