package com.tingleff.yassg.content;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.joda.time.DateTime;

import com.tingleff.yassg.model.Page;
import com.tingleff.yassg.model.PageCollection;

public class ContentDB implements Iterable<Page> {
	private PubDateComparator pubDateComparator = new PubDateComparator();

	private List<Page> pages = Collections.synchronizedList(new LinkedList<Page>());

	public void addPage(Page page) {
		pages.add(page);
		Collections.sort(pages, pubDateComparator);
	}

	public void addAll(Collection<Page> pages) {
		for (Page p : pages)
			addPage(p);
	}

	public PageCollection index(int count) {
		// return first n items where pub date is before now
		DateTime now = new DateTime();
		List<Page> results = new ArrayList<Page>(count);
		for (Page p : pages) {
			if (p.getPubDate().isAfter(now))
				continue;
			if (results.size() >= count)
				break;
			results.add(p);
		}
		return new PageCollection(results);
	}

	public Iterator<Page> iterator() {
		return pages.iterator();
	}

	private static class PubDateComparator implements Comparator<Page> {

		@Override
		public int compare(Page p1, Page p2) {
			DateTime dt1 = p1.getPubDate();
			DateTime dt2 = p2.getPubDate();
			return dt2.compareTo(dt1);
		}
		
	}
}
