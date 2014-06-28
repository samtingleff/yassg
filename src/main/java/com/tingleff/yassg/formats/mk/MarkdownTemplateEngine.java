package com.tingleff.yassg.formats.mk;

import org.markdown4j.Markdown4jProcessor;

import com.tingleff.yassg.formats.TemplateEngine;
import com.tingleff.yassg.formats.TemplateInstance;
import com.tingleff.yassg.formats.mk.plugin.FlickrPlugin;

public class MarkdownTemplateEngine implements TemplateEngine {

	private Markdown4jProcessor p = new Markdown4jProcessor()
			.registerPlugins(new FlickrPlugin());

	@Override
	public TemplateInstance parse(String template) {
		return new MarkdownTemplateInstance(p, template);
	}

}
