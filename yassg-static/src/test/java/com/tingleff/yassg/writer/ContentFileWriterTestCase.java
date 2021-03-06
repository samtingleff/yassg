package com.tingleff.yassg.writer;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;

import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.tingleff.yassg.model.Page;

@RunWith(JUnit4.class)
public class ContentFileWriterTestCase {

	ContentFileWriter cfw = new ContentFileWriter("target/test/output");

	@Test
	public void futurePubDate() throws Exception {
		Page p = createPage(new DateTime(2999, 12, 31, 23, 59), "slug-dude-1");
		Assert.assertFalse(cfw.shouldWritePage(p));
	}

	@Test
	public void pastPubDate() throws Exception {
		Page p = createPage(new DateTime(1999, 12, 31, 2, 23), "slug-dude-2");
		File path = cfw.getPath(p);
		if (path.exists())
			path.delete();
		Assert.assertTrue(cfw.shouldWritePage(p));
		Thread.sleep(1000);
		cfw.writePage(p, "test body");
		Assert.assertFalse(cfw.shouldWritePage(p));
	}

	private Page createPage(DateTime pubDate, String slug) {
		Page p = new Page(System.currentTimeMillis(),
				2354902341413l,
				"author dude",
				"title dude",
				"keywords,bro",
				"description dude",
				new HashSet<String>(Arrays.asList("tag1", "tag2", "tag3")),
				slug,
				false,
				pubDate,
				"body here");
		return p;
	}
}
