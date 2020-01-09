package com.tingleff.yassg.semantic;

import java.io.IOException;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class AlphaSemanticClientTestCase {

	private SemanticClient client;

	@Test
	public void testSimple() throws IOException {
		//NamedEntityResponse response = client.namedEntities("https://www.head-fi.org/threads/schiit-happened-the-story-of-the-worlds-most-improbable-start-up.701900/page-80");
		NamedEntityResponse response = client.namedEntities("https://www.armstrongeconomics.com/international-news/north_america/americas-current-economy/oklahoma-police-can-seize-your-entire-bank-account-on-a-traffic-stop-without-any-charges-at-all/");
		Assert.assertTrue(response.isSuccess());
		NamedEntityResult result = response.getResult();
		Assert.assertEquals("corenlp", result.getSource());
		List<NamedEntity> entities = result.getEntities();
		Assert.assertEquals(36, entities.size());
		// check two of them
		NamedEntity e1 = entities.get(0);
		Assert.assertEquals("organization", e1.getType());
		Assert.assertEquals("AAC", e1.getText());
		Assert.assertEquals(1.0, e1.getScore(), 0.001d);
		Assert.assertEquals(1, e1.getCount());
		NamedEntity e7 = entities.get(16);
		Assert.assertEquals("location", e7.getType());
		Assert.assertEquals("Montserrat", e7.getText());
		Assert.assertEquals(1.0, e7.getScore(), 0.001d);
		Assert.assertEquals(6, e7.getCount());
	}

	@Before
	public void setUp() throws IOException {
		AlphaSemanticClient client = new AlphaSemanticClient(
				System.getenv("NER_URL"),
				System.getenv("NER_SECRET"),
				"src/test/tmp");
		client.init();
		this.client = client;
	}
}
