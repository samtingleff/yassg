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
		String t = "Hello, $name$";
		TemplateInstance ti = te.parse(t);
		Assert.assertNotNull(ti);
		ti.put("name", "foo");
		String result = ti.render();
		Assert.assertNotNull(result);
		Assert.assertEquals("Hello, foo", result);
	}

	@Test
	public void object() throws IOException {
		String t = "this is foobared to the $foo.foo$ level, $foo.bar$.";
		TemplateInstance ti = te.parse(t);
		Assert.assertNotNull(ti);
		Foo foo = new Foo(10, "bro");
		ti.put("foo", foo);
		String result = ti.render();
		Assert.assertNotNull(result);
		Assert.assertEquals("this is foobared to the 10 level, bro.", result);
	}

	@Test
	public void conditionalNonNull() throws IOException {
		String t = "this is foobared, $foo.bar:{$foo.bar$ yo}$.";
		TemplateInstance ti = te.parse(t);
		Assert.assertNotNull(ti);
		Foo foo = new Foo(10, "bro");
		ti.put("foo", foo);
		String result = ti.render();
		Assert.assertNotNull(result);
		Assert.assertEquals("this is foobared, bro yo.", result);
	}

	@Test
	public void conditionalNull() throws IOException {
		String t = "this is foobared$foo.bar:{, $foo.bar$ yo}$.";
		TemplateInstance ti = te.parse(t);
		Assert.assertNotNull(ti);
		String result = ti.render();
		Assert.assertNotNull(result);
		Assert.assertEquals("this is foobared.", result);
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

	private static class Foo {
		private int foo;
		private String bar;
		public Foo(int foo, String bar) {
			this.foo = foo;
			this.bar = bar;
		}

		public int getFoo() { return foo; }

		public String getBar() { return bar; }

		public String getFooBar() { return String.format("%1$s:%2$s", foo, bar); }
	}
}
