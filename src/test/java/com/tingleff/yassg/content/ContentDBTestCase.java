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
import com.tingleff.yassg.model.PageCollection;

@RunWith(JUnit4.class)
public class ContentDBTestCase {

	private ContentDB contentdb = new ContentDB();

	@Test
	public void index() {
		List<Page> pages = Arrays.asList(
				createPage(new DateTime(2900, 1, 13, 7, 11)),
				createPage(new DateTime(2014, 2, 11, 7, 11)),
				createPage(new DateTime(2014, 6, 13, 7, 11)),
				createPage(new DateTime(2013, 3, 13, 7, 11)),
				createPage(new DateTime(2011, 8, 29, 12, 11)),
				createPage(new DateTime(2014, 4, 30, 22, 19)));
		contentdb.addAll(pages);

		PageCollection pc = contentdb.first(10);
		Assert.assertNotNull(pc);
		Assert.assertEquals(5, pc.size());
		Assert.assertEquals(pages.get(2), pc.asList().get(0));
		Assert.assertEquals(pages.get(5), pc.asList().get(1));
		Assert.assertEquals(pages.get(1), pc.asList().get(2));
		Assert.assertEquals(pages.get(3), pc.asList().get(3));
		Assert.assertEquals(pages.get(4), pc.asList().get(4));

		pc = contentdb.first(2);
		Assert.assertNotNull(pc);
		Assert.assertEquals(2, pc.size());
		Assert.assertEquals(pages.get(2), pc.asList().get(0));
		Assert.assertEquals(pages.get(5), pc.asList().get(1));
	}

	private Page createPage(DateTime pubDate) {
		Page p = new Page(System.currentTimeMillis(),
				"author dude",
				"title dude",
				"keywords,bro",
				"description dude",
				new HashSet<String>(Arrays.asList("tag1", "tag2", "tag3")),
				"slug-dude",
				pubDate,
				"body here");
		return p;
	}
}
