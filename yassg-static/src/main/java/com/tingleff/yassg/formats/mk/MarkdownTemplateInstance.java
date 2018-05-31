package com.tingleff.yassg.formats.mk;

import java.io.IOException;

import org.markdown4j.Markdown4jProcessor;

import com.tingleff.yassg.formats.TemplateInstance;

public class MarkdownTemplateInstance implements TemplateInstance {

	private Markdown4jProcessor p;

	private String template;

	MarkdownTemplateInstance(Markdown4jProcessor p, String template) {
		this.p = p;
		this.template = template;
	}

	private MarkdownTemplateInstance() { }

	@Override
	public void put(String key, Object value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String render() throws IOException {
		String result = p.process(template);
		// running into this bug
		// https://stackoverflow.com/questions/34853383/markdown4j-adding-space-if-script-is-included-in-markdown
		// and I'm too lazy to change markdown engines
		result = result.replaceAll("https: //", "https://");
		return result;
	}

}
