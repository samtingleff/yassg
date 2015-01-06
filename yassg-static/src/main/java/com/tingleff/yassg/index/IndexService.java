package com.tingleff.yassg.index;

import java.io.IOException;

import com.tingleff.yassg.model.Page;
import com.tingleff.yassg.semantic.NamedEntity;

public interface IndexService {

	public void open() throws IOException;

	public void close() throws IOException;

	public void indexPage(Page page) throws IOException;

	public void indexPage(Page page, Iterable<NamedEntity> entities) throws IOException;
}
