package com.tingleff.yassg.content;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.tingleff.yassg.model.Page;

@RunWith(JUnit4.class)
public class ContentDBTestCase {

	private ContentDB contentdb = new ContentDB();

	@Test
	public void index() {
		List<Page> pages = Arrays.asList(
				createPage(new DateTime(2900, 1, 13, 7, 11), false),
				createPage(new DateTime(2014, 2, 11, 7, 11), false),
				createPage(new DateTime(2014, 6, 13, 7, 11), false),
				createPage(new DateTime(2013, 3, 13, 7, 11), false),
				createPage(new DateTime(2011, 8, 29, 12, 11), false),
				createPage(new DateTime(2014, 4, 30, 22, 19), true));
		contentdb.addAll(pages);

		List<Page> pc = contentdb.index(10);
		Assert.assertNotNull(pc);
		// items in the future, or with noindex=true should be excluded
		Assert.assertEquals(4, pc.size());
		Assert.assertEquals(pages.get(2), pc.get(0));
		Assert.assertEquals(pages.get(1), pc.get(1));
		Assert.assertEquals(pages.get(3), pc.get(2));
		Assert.assertEquals(pages.get(4), pc.get(3));

		pc = contentdb.index(2);
		Assert.assertNotNull(pc);
		Assert.assertEquals(2, pc.size());
		Assert.assertEquals(pages.get(2), pc.get(0));
		Assert.assertEquals(pages.get(1), pc.get(1));
	}

	private Page createPage(DateTime pubDate, boolean noindex) {
		Page p = new Page(System.currentTimeMillis(),
				104391493145l,
				"author dude",
				"title dude",
				"keywords,bro",
				"description dude",
				new HashSet<String>(Arrays.asList("tag1", "tag2", "tag3")),
				"slug-dude",
				noindex,
				pubDate,
				"body here");
		return p;
	}
}
