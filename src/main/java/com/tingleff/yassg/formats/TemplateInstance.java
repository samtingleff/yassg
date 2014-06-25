package com.tingleff.yassg.formats;

public interface TemplateInstance {

	public void put(String key, String value);

	public String render();
}
