package com.tingleff.yassg.pagedb;

import java.io.IOException;
import java.text.ParseException;

import com.tingleff.yassg.model.Page;

public interface PageDB {

	public Page read(String id) throws IOException, ParseException;

	public void decorate(Decorator<Page> pageDecorator);

	public Iterable<Page> iterator();
}
