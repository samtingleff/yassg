package com.tingleff.yassg.search;

import java.io.IOException;

import org.apache.thrift.TException;

import com.tingleff.yassg.search.types.TSearchException;
import com.tingleff.yassg.search.types.TSearchResult;
import com.tingleff.yassg.search.types.TSearchService;
import com.tingleff.yassg.search.types.TSort;

public class SearchServiceHandler implements TSearchService.Iface {

	private SearchService backend;

	public SearchServiceHandler(SearchService backend) {
		this.backend = backend;
	}

	@Override
	public TSearchResult search(String query, int n, TSort sorting)
			throws TSearchException, TException {
		try {
			return backend.search(query, n, sorting);
		} catch (IOException e) {
			// TODO: log me?
			e.printStackTrace();
			throw new TSearchException();
		}
	}

	@Override
	public TSearchResult similar(int id, int n, TSort sorting)
			throws TSearchException, TException {
		try {
			return backend.similar(id, n, sorting);
		} catch (IOException e) {
			// TODO: log me?
			e.printStackTrace();
			throw new TSearchException();
		}
	}

	public void reopen() throws TSearchException {
		try {
			backend.reopen();
		} catch (IOException e) {
			// TODO: log me?
			e.printStackTrace();
			throw new TSearchException();
		}
	}
}
