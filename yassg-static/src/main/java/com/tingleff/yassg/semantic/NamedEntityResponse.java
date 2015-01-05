package com.tingleff.yassg.semantic;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class NamedEntityResponse implements Serializable, Iterable<NamedEntity> {
	private static final long serialVersionUID = 5565668814721056491L;

	private List<NamedEntity> list = new LinkedList<NamedEntity>();

	public NamedEntityResponse() {
	}

	void add(NamedEntity ne) {
		list.add(ne);
	}

	@Override
	public Iterator<NamedEntity> iterator() {
		return list.iterator();
	}

	public int size() {
		return list.size();
	}

	public NamedEntity get(int index) {
		return list.get(index);
	}
}
