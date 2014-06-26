package com.tingleff.yassg.model;

import java.util.Iterator;
import java.util.List;

public class PageCollection implements Iterable<Page> {

	private List<Page> pages;

	public PageCollection(List<Page> pages) {
		this.pages = pages;
	}

	public int size() {
		return pages.size();
	}

	public List<Page> asList() {
		return pages;
	}

	public Iterator<Page> iterator() {
		return pages.iterator();
	}

}
