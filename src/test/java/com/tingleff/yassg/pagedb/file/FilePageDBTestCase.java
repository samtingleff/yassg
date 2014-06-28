package com.tingleff.yassg.pagedb.file;

import java.util.Arrays;
import java.util.HashSet;

import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.tingleff.yassg.model.Page;
import com.tingleff.yassg.pagedb.Decorator;
import com.tingleff.yassg.pagedb.PageDB;

@RunWith(JUnit4.class)
public class FilePageDBTestCase {

	private PageDB db = new FilePageDB("src/test/resources/pages");

	@Test
	public void read() throws Exception {
		String id = "test";
		Page page = db.read(id);
		Assert.assertNotNull(page);
		Assert.assertNotEquals(0l, page.getModified());
		Assert.assertEquals("sam", page.getAuthor());
		Assert.assertEquals("Test page bro", page.getTitle());
		Assert.assertEquals("keywords here", page.getKeywords());
		Assert.assertEquals("description here", page.getDescription());
		Assert.assertEquals("/test-slug-bro/", page.getHref());
		Assert.assertEquals(new DateTime(2014, 6, 17, 11, 13), page.getPubDate());
		Assert.assertEquals(new HashSet<String>(Arrays.asList("tag1", "tag2", "tag3")), page.getTags());
		Assert.assertEquals("This is a paragraph.\n", page.getBody());
	}

	@Test
	public void decorator() throws Exception {
		class Foo implements Decorator<Page> {
			int counter = 0;
			@Override
			public void decorate(Page t) {
				++counter;
			}
			public int getCounter() {
				return counter;
			}
		};
		Foo foo = new Foo();
		db.decorate(foo);
		Assert.assertEquals(foo.getCounter(), 2);
	}

	@Test
	public void iterator() throws Exception {
		Iterable<Page> iter = db.iterator();
		Assert.assertNotNull(iter);
		int size = 0;
		for (Page p : iter) {
			++size;
			Assert.assertNotNull(p);
		}
		Assert.assertEquals(2, size);
	}
}
