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
		Assert.assertEquals("<iframe src=\"//www.flickr.com/photos/samtingleff/1934134452454/player/\" width=\"450\" height=\"375\" frameborder=\"0\" allowfullscreen webkitallowfullscreen mozallowfullscreen oallowfullscreen msallowfullscreen></iframe>", result);
	}

	@Test
	public void soundcloudPlugin() throws IOException {
		String t = "%%% soundcloud height=150\n1934134452454\n%%%";
		TemplateInstance ti = te.parse(t);
		Assert.assertNotNull(ti);
		String result = ti.render();
		Assert.assertNotNull(result);
		Assert.assertEquals("<iframe width=\"100%\" height=\"150\" scrolling=\"no\" frameborder=\"no\" src=\"https://w.soundcloud.com/player/?url=https%3A//api.soundcloud.com/tracks/1934134452454&amp;color=ff5500&amp;auto_play=false&amp;hide_related=false&amp;show_comments=true&amp;show_user=true&amp;show_reposts=false\"></iframe>", result);
	}

	@Test
	public void tweetPlugin() throws IOException {
		String t = "%%% tweet user=samtingleff\n1934134452454\n%%%";
		TemplateInstance ti = te.parse(t);
		Assert.assertNotNull(ti);
		String result = ti.render();
		Assert.assertNotNull(result);
		Assert.assertEquals("<iframe border=\"0\" frameborder=\"0\" height=\"250\" width=\"550\" src=\"https://twitframe.com/show?url=https%3A%2F%2Ftwitter.com%2Fsamtingleff%2Fstatus%2F1934134452454\"></iframe>", result);
	}
}
