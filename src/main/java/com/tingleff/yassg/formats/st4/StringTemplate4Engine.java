package com.tingleff.yassg.formats.st4;

import org.stringtemplate.v4.ST;

import com.tingleff.yassg.formats.TemplateEngine;
import com.tingleff.yassg.formats.TemplateInstance;

public class StringTemplate4Engine implements TemplateEngine {

	public TemplateInstance parse(String template) {
System.out.println(template);
		ST st = new ST(template, '$', '$');
		return new StringTemplate4Instance(st);
	}
}
