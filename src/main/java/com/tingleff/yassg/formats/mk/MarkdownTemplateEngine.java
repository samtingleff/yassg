package com.tingleff.yassg.formats.mk;

import org.markdown4j.Markdown4jProcessor;

import com.tingleff.yassg.formats.TemplateEngine;
import com.tingleff.yassg.formats.TemplateInstance;

public class MarkdownTemplateEngine implements TemplateEngine {

	private Markdown4jProcessor p = new Markdown4jProcessor();

	@Override
	public TemplateInstance parse(String template) {
		return new MarkdownTemplateInstance(p, template);
	}

}
