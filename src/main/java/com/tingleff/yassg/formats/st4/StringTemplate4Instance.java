package com.tingleff.yassg.formats.st4;

import com.tingleff.yassg.formats.TemplateInstance;

import org.stringtemplate.v4.ST;

public class StringTemplate4Instance implements TemplateInstance {

	private ST st;

	StringTemplate4Instance(ST st) {
		this.st = st;
	}

	private StringTemplate4Instance() { }

	public void put(String key, Object value) {
		st.add(key, value);
	}

	public String render() {
		return st.render();
	}
}
