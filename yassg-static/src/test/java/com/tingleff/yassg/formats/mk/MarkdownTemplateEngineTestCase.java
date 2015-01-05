package com.tingleff.yassg.formats.mk;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.tingleff.yassg.formats.TemplateEngine;
import com.tingleff.yassg.formats.TemplateInstance;

@RunWith(JUnit4.class)
public class MarkdownTemplateEngineTestCase {

	private TemplateEngine te = new MarkdownTemplateEngine();

	@Test
	public void simple() throws IOException {
		String t = "This is **bold** text";
		TemplateInstance ti = te.parse(t);
		Assert.assertNotNull(ti);
		String result = ti.render();
		Assert.assertNotNull(result);
		Assert.assertEquals("<p>This is <strong>bold</strong> text</p>\n", result);
	}

	@Test
	public void flickrPlugin() throws IOException {
		String t = "%%% flickr username=samtingleff width=450\n1934134452454\n%%%";
		TemplateInstance ti = te.parse(t);
		Assert.assertNotNull(ti);
		String result = ti.render();
		Assert.assertNotNull(result);
		Assert.assertEquals("<iframe src=\"https://www.flickr.com/photos/samtingleff/1934134452454/player/\" width=\"450\" height=\"375\" frameborder=\"0\" allowfullscreen webkitallowfullscreen mozallowfullscreen oallowfullscreen msallowfullscreen></iframe>", result);
	}
}
