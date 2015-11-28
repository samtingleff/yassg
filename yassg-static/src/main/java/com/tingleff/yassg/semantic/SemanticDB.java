package com.tingleff.yassg.semantic;

import java.io.IOException;
import java.util.Iterator;

public interface SemanticDB {

	public NamedEntityResponse lookup(String url) throws IOException;

	public void save(String url, NamedEntityResponse response) throws IOException;

	public Iterator<NamedEntityResponse> iterator() throws IOException;
}
