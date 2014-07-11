package com.tingleff.yassg.formats.mk;

import org.markdown4j.Markdown4jProcessor;

import com.tingleff.yassg.formats.TemplateEngine;
import com.tingleff.yassg.formats.TemplateInstance;
import com.tingleff.yassg.formats.mk.plugin.FlickrPlugin;
import com.tingleff.yassg.formats.mk.plugin.SoundcloudPlugin;

public class MarkdownTemplateEngine implements TemplateEngine {

	private Markdown4jProcessor p = new Markdown4jProcessor()
			.registerPlugins(new FlickrPlugin())
			.registerPlugins(new SoundcloudPlugin());

	@Override
	public TemplateInstance parse(String template) {
		return new MarkdownTemplateInstance(p, template);
	}

}
