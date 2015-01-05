package com.tingleff.yassg.search;

import java.util.List;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

public class Main {

	public static void main(String[] args) throws Exception {
		Main m = new Main();
		new JCommander(m).parse(args);
		m.run();
	}

	// should match LuceneIndexService in yassg project
	private static String[] defaultSearchFields = new String[] {
			"id", "author", "title", "keywords", "description", "tag", "href", "date", "body"
	};

	@Parameter(names = "-dir", description = "Index directory", required = true)
	private String directory;

	@Parameter(names = "-field", description = "Index search fields", required = false)
	private List<String> fields;

	@Parameter(names = "-port", description = "Daemon port (default 9999)", required = false)
	private int port = 9999;

	public void run() throws Exception {
		SearchService searcher = initSearcher();
		SearchServiceHandler proxy = new SearchServiceHandler(searcher);
		ThriftSearchDaemon daemon = new ThriftSearchDaemon(proxy, port);
		daemon.run();
	}

	private SearchService initSearcher() throws Exception {
		String[] searchFields = defaultSearchFields;
		if ((fields != null) && (fields.size() > 0)) {
			searchFields = fields.toArray(new String[fields.size()]);
		}
		LuceneSearchService searcher = new LuceneSearchService(directory, searchFields);
		searcher.init();
		return searcher;
	}
}