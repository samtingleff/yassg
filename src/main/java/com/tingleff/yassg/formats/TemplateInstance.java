package com.tingleff.yassg.formats;

public interface TemplateInstance {

	public void put(String key, Object value);

	public String render();
}
