package com.tingleff.yassg.semantic;

import java.io.Serializable;

public class NamedEntity implements Serializable {

	// reference count in the source text
	private int count;

	// entity type
	private String type;

	// entity text
	private String text;

	// score
	private double score;

	NamedEntity(int count,
			String type,
			String text,
			double score) {
		this.count = count;
		this.type = type;
		this.text = text;
		this.score = score;
	}

	public NamedEntity() { }

	@Override
	public String toString() {
		return String.format("[type=%1$s;text=%2$s;score=%3$s;count=%4$s]",
				getType(),
				getText(),
				getScore(),
				getCount());
	}

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
}
