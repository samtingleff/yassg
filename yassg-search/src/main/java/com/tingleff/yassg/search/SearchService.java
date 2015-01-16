package com.tingleff.yassg.search;

import java.io.IOException;

import com.tingleff.yassg.search.types.TSearchException;
import com.tingleff.yassg.search.types.TSearchResult;
import com.tingleff.yassg.search.types.TSort;

public interface SearchService {

	public TSearchResult search(String query, int n, TSort sort) throws IOException, TSearchException;

	public TSearchResult similar(String search, int n, TSort sort) throws IOException, TSearchException;

	public void reopen() throws IOException;
}
