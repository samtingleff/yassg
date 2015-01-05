package com.tingleff.yassg.formats.st4;

import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STRawGroupDir;

import com.tingleff.yassg.formats.TemplateEngine;
import com.tingleff.yassg.formats.TemplateInstance;

public class StringTemplate4Engine implements TemplateEngine {
	private STGroup group;

	public StringTemplate4Engine(String dir) {
		group = new STRawGroupDir(dir, '$', '$');
	}

	public TemplateInstance parse(String name) {
		ST st = group.getInstanceOf(name);
		return new StringTemplate4Instance(st);
	}
}
