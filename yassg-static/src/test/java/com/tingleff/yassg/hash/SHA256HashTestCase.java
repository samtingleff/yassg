package com.tingleff.yassg.hash;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class SHA256HashTestCase {

	private HashFunction hash = new SHA256HashFunction();

	@Test
	public void bytes() {
		byte[] input = "hello world".getBytes();
		byte[] output = hash.hash(input);
		Assert.assertNotNull(output);
		Assert.assertEquals(32, output.length);
	}

	@Test
	public void testValues() {
		Assert.assertEquals(
				"ba7816bf8f01cfea414140de5dae2223b00361a396177a9cb410ff61f20015ad".toLowerCase(),
				hash.hex("abc"));
		Assert.assertEquals(
				"248d6a61d20638b8e5c026930c3e6039a33ce45964ff2167f6ecedd419db06c1",
				hash.hex("abcdbcdecdefdefgefghfghighijhijkijkljklmklmnlmnomnopnopq"));
	}
}
