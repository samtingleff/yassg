package com.tingleff.yassg.formats.st4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.tingleff.yassg.formats.TemplateEngine;
import com.tingleff.yassg.formats.TemplateInstance;

@RunWith(JUnit4.class)
public class StringTemplate4EngineTestCase {

	private TemplateEngine te = new StringTemplate4Engine();

	@Test
	public void simple() throws IOException {
		String t = loadResource("/simple.st");
		Assert.assertEquals("Hello, <name>", t);
		TemplateInstance ti = te.parse(t);
		Assert.assertNotNull(ti);
		ti.put("name", "foo");
		String result = ti.render();
		Assert.assertNotNull(result);
		Assert.assertEquals("Hello, foo", result);
	}

	private String loadResource(String path) throws IOException {
		InputStream is = getClass().getResourceAsStream(path);
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);
		char[] buffer = new char[128];
		int read = br.read(buffer);
		String s = new String(buffer, 0, read);
		br.close();
		isr.close();
		is.close();
		return s;
	}
}
