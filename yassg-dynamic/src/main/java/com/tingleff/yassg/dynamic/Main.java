package com.tingleff.yassg.dynamic;

import java.util.List;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.tingleff.yassg.dynamic.search.SearchServiceHandler;
import com.tingleff.yassg.dynamic.search.ThriftSearchDaemon;
import com.tingleff.yassg.dynamic.sessions.SessionServiceHandler;
import com.tingleff.yassg.dynamic.sessions.ThriftSessionDaemon;
import com.tingleff.yassg.search.LuceneSearchService;
import com.tingleff.yassg.search.SearchService;

public class Main {

	public static void main(String[] args) throws Exception {
		Main m = new Main();
		new JCommander(m).parse(args);
		m.run();
	}

	@Parameter(names = "-dir", description = "Index directory", required = true)
	private String directory;

	@Parameter(names = "-field", description = "Index search fields", required = false)
	private List<String> fields;

	@Parameter(names = "-searchPort", description = "Search service daemon port (default 9999)", required = false)
	private int searchPort = 9999;

	@Parameter(names = "-sessionPort", description = "Session service daemon port (default 9998)", required = false)
	private int sessionPort = 9998;

	public void run() throws Exception {
		Thread t1 = runSearchDaemon();
		Thread t2 = runSessionDaemon();
	}

	private Thread runSearchDaemon() throws Exception {
		Thread t = new Thread(new Runnable() {
			public void run() {
				try {
					SearchService searcher = initSearcher();
					SearchServiceHandler proxy = new SearchServiceHandler(searcher);
					ThriftSearchDaemon daemon = new ThriftSearchDaemon(proxy, searchPort);
					daemon.run();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		t.setDaemon(false);
		t.start();
		return t;
	}

	private Thread runSessionDaemon() throws Exception {
		Thread t = new Thread(new Runnable() {
			public void run() {
				try {
					SessionServiceHandler proxy = new SessionServiceHandler();
					ThriftSessionDaemon daemon = new ThriftSessionDaemon(proxy, sessionPort);
					daemon.run();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		t.setDaemon(false);
		t.start();
		return t;
	}

	private SearchService initSearcher() throws Exception {
		String[] searchFields = SearchService.DefaultSearchFields;
		if ((fields != null) && (fields.size() > 0)) {
			searchFields = fields.toArray(new String[fields.size()]);
		}
		LuceneSearchService searcher = new LuceneSearchService(directory, searchFields);
		searcher.init();
		return searcher;
	}
}
