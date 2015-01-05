package com.tingleff.yassg.semantic;

import java.io.IOException;

public interface SemanticClient {

	public NamedEntityResponse namedEntities(String url) throws IOException;
}
