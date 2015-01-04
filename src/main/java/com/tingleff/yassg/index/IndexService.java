package com.tingleff.yassg.index;

import java.io.IOException;

import com.tingleff.yassg.model.Page;

public interface IndexService {

	public void indexPage(Page page) throws IOException;
}
