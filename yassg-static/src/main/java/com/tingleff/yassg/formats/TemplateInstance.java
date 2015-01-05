package com.tingleff.yassg.formats;

import java.io.IOException;

public interface TemplateInstance {

	public void put(String key, Object value);

	public String render() throws IOException;
}
