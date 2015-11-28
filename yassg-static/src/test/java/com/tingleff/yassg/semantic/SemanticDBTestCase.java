package com.tingleff.yassg.semantic;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.Iterator;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class SemanticDBTestCase {

	private SemanticClient client;

	private SemanticDB db;

	@Before
	public void setUp() {
		String dir = "src/test/tmp/entities";
		// instantiate the client
		AlchemyAPISemanticClient client = new AlchemyAPISemanticClient(
				"foobar-api-key",
				dir);
		client.init();
		this.client = client;

		this.db = new SemanticDBImpl(dir);
	}

	@Test
	public void testSimple() throws IOException {
		NamedEntityResponse result = client.namedEntities("http://www.head-fi.org/t/701900/schiit-happened-the-story-of-the-worlds-most-improbable-start-up/1185");
		Assert.assertTrue(result.isSuccess());
		Assert.assertEquals(26, result.size());
		// check two of them
		NamedEntity e1 = result.get(0);
		Assert.assertEquals("Person", e1.getType());
		Assert.assertEquals("Mike Moffat", e1.getText());
		Assert.assertEquals(0.74376d, e1.getScore(), 0.0d);
		Assert.assertEquals(11, e1.getCount());

		NamedEntity e7 = result.get(7);
		Assert.assertEquals("Company", e7.getType());
		Assert.assertEquals("Western Digital", e7.getText());
		Assert.assertEquals(0.350372, e7.getScore(), 0.0d);
		Assert.assertEquals(1, e7.getCount());
	}

	@Test
	public void iteration() throws IOException {
		long now = System.currentTimeMillis();
		String url1 = "", url2 = "";

		NamedEntityResponse ner1 = new NamedEntityResponse(url1, false, now);
		ner1.add(new NamedEntity(10, "foobar", "FoobarText", 10.032d, Arrays.asList("subtype1", "subtype2")));
		db.save(url1, ner1);

		NamedEntityResponse ner2 = new NamedEntityResponse(url2, true, 0l);
		ner2.add(new NamedEntity(10, "foobar", "FoobarText", 10.032d, Arrays.asList("subtype1", "subtype2")));
		db.save(url2, ner2);

		Iterator<NamedEntityResponse> iter = db.iterator();
		Assert.assertNotNull(iter);
		while (iter.hasNext()) {
			NamedEntityResponse ner = iter.next();
			Assert.assertNotNull(ner);
			System.out.println(ner);
		}
	}

	@Test
	public void testSerialization() throws IOException, ClassNotFoundException {
		long now = System.currentTimeMillis();
		String url1 = "http://stackoverflow.com/questions/3154488/best-way-to-iterate-through-a-directory-in-java";
		String url2 = "https://commons.apache.org/proper/commons-io/description.html";

		NamedEntityResponse ner = new NamedEntityResponse(url1, false, now);
		ner.add(new NamedEntity(10, "foobar", "FoobarText", 10.032d, Arrays.asList("subtype1", "subtype2")));
		byte[] bytes = serialize(ner);

		NamedEntityResponse result = deserialize(bytes);
		Assert.assertEquals(url1, result.getUrl());
		Assert.assertFalse(result.isSuccess());
		Assert.assertEquals(now, result.getTimestamp());

		ner = new NamedEntityResponse(url2, true, 0l);
		ner.add(new NamedEntity(10, "foobar", "FoobarText", 10.032d, Arrays.asList("subtype1", "subtype2")));
		bytes = serialize(ner);

		result = deserialize(bytes);
		Assert.assertEquals(url2, result.getUrl());
		Assert.assertTrue(result.isSuccess());
		Assert.assertEquals(0l, result.getTimestamp());
	}

	private byte[] serialize(NamedEntityResponse ner) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(ner);
			return baos.toByteArray();
	}

	private NamedEntityResponse deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
		ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
		ObjectInputStream ois = new ObjectInputStream(bais);
		NamedEntityResponse ner = (NamedEntityResponse) ois.readObject();
		ois.close();
		return ner;
	}
}
