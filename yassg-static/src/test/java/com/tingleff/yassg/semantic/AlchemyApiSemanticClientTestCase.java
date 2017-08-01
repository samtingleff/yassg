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

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class AlchemyApiSemanticClientTestCase {

	private SemanticClient client;

	@Test
	public void testSimple() throws IOException {
		NamedEntityResponse result = client.namedEntities("http://www.head-fi.org/t/701900/schiit-happened-the-story-of-the-worlds-most-improbable-start-up/1185?thisis=new");
		Assert.assertTrue(result.isSuccess());
		Assert.assertEquals(12, result.size());
		// check two of them
		NamedEntity e1 = result.get(0);
		Assert.assertEquals("Person", e1.getType());
		Assert.assertEquals("Mike Moffat", e1.getText());
		Assert.assertEquals(0.74376d, e1.getScore(), 0.0d);
		Assert.assertEquals(11, e1.getCount());
		NamedEntity e7 = result.get(6);
		Assert.assertEquals("Company", e7.getType());
		Assert.assertEquals("Western Digital", e7.getText());
		Assert.assertEquals(0.350372, e7.getScore(), 0.0d);
		Assert.assertEquals(1, e7.getCount());
	}

	@Test
	public void testSerialization() throws IOException, ClassNotFoundException {
		long now = System.currentTimeMillis();
		String url = "http://www.alchemyapi.com/api/entity/types";
		NamedEntityResponse ner = new NamedEntityResponse(url, false, now);
		ner.add(new NamedEntity(10, "foobar", "FoobarText", 10.032d, Arrays.asList("subtype1", "subtype2")));
		byte[] bytes = serialize(ner);

		NamedEntityResponse result = deserialize(bytes);
		Assert.assertFalse(result.isSuccess());
		Assert.assertEquals(now, result.getTimestamp());

		ner = new NamedEntityResponse(url, true, 0l);
		ner.add(new NamedEntity(10, "foobar", "FoobarText", 10.032d, Arrays.asList("subtype1", "subtype2")));
		bytes = serialize(ner);

		result = deserialize(bytes);
		Assert.assertTrue(result.isSuccess());
		Assert.assertEquals(0l, result.getTimestamp());
	}

	@Before
	public void setUp() throws IOException {
		// copy from test resource dir into temp dir to avoid hitting the API
		File resourcePath = new File("src/test/resources/alchemy/9ce6e821f1fbb27149caa7719db6adadc95f6ee0d7df02895023cc42b89fb310.ser");
		File cachePath = new File("src/test/tmp/entities-1/9c/e6/e8/9ce6e821f1fbb27149caa7719db6adadc95f6ee0d7df02895023cc42b89fb310.ser");
		FileInputStream in = new FileInputStream(resourcePath);
		FileOutputStream out = new FileOutputStream(cachePath);
		IOUtils.copy(in, out);
		out.close();
		in.close();

		// instantiate the client
		AlchemyAPISemanticClient client = new AlchemyAPISemanticClient(
				"revoked",
				"password",
				"src/test/tmp/entities-1");
		client.init();
		this.client = client;
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
