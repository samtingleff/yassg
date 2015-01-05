package com.tingleff.yassg.semantic;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

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
		NamedEntityResponse result = client.namedEntities("http://www.head-fi.org/t/701900/schiit-happened-the-story-of-the-worlds-most-improbable-start-up/1185");
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

	@Before
	public void setUp() throws IOException {
		// copy from test resource dir into temp dir to avoid hitting the API
		File resourcePath = new File("src/test/resources/alchemy/9ce6e821f1fbb27149caa7719db6adadc95f6ee0d7df02895023cc42b89fb310.ser");
		File cachePath = new File("src/test/tmp/entities/9c/e6/e8/9ce6e821f1fbb27149caa7719db6adadc95f6ee0d7df02895023cc42b89fb310.ser");
		FileInputStream in = new FileInputStream(resourcePath);
		FileOutputStream out = new FileOutputStream(cachePath);
		IOUtils.copy(in, out);
		out.close();
		in.close();

		// instantiate the client
		AlchemyAPISemanticClient client = new AlchemyAPISemanticClient(
				"foobar-api-key",
				"src/test/tmp/entities");
		client.init();
		this.client = client;
	}
}
