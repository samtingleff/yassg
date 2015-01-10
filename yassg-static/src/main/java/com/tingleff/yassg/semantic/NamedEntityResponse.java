package com.tingleff.yassg.semantic;

import java.io.IOException;
import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class NamedEntityResponse implements Serializable, Iterable<NamedEntity> {
	private static final long serialVersionUID = 5565668814721056491L;

	private boolean success = true;

	private long timestamp = 0l;

	private List<NamedEntity> list = new LinkedList<NamedEntity>();

	public NamedEntityResponse(boolean success, long timestamp) {
		this.success = success;
		this.timestamp = timestamp;
	}

	public NamedEntityResponse() {
		this(true, System.currentTimeMillis());
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

	public boolean isSuccess() {
		return success;
	}

	public long getTimestamp() {
		return timestamp;
	}

	private void readObject(java.io.ObjectInputStream in)
		     throws IOException, ClassNotFoundException {
		// set a default value
		this.success = true;
		in.defaultReadObject();
	}
}
