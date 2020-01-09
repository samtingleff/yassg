package com.tingleff.yassg.semantic;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class SemanticDBTestCase {

	private SemanticDB db;

	@Before
	public void setUp() {
		String dir = "src/test/tmp/entities-2";
		this.db = new SemanticDBImpl(dir);
	}

	@Test
	public void iteration() throws IOException {
		long now = System.currentTimeMillis();
		String url1 = "http://www.alchemyapi.com/api/entity/types",
				url2 = "https://commons.apache.org/proper/commons-io/description.html";

		NamedEntityResponse ner1 = new NamedEntityResponse(false,
				new NamedEntityResult("foo", url1, now,
						Arrays.asList(new NamedEntity(10, "foobar", "FoobarText", 10.032d))));
		db.save(url1, ner1);

		NamedEntityResponse ner2 = new NamedEntityResponse(true,
				new NamedEntityResult("foo", url2, now,
						Arrays.asList(new NamedEntity(10, "foobar", "FoobarText", 10.032d))));
		db.save(url2, ner2);

		Iterator<NamedEntityResponse> iter = db.iterator();
		Assert.assertNotNull(iter);
		int count = 0;
		while (iter.hasNext()) {
			NamedEntityResponse ner = iter.next();
			Assert.assertNotNull(ner);
			++count;
		}
		Assert.assertEquals(2, count);
	}
}
