package com.tingleff.yassg.dynamic;

import java.util.List;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.tingleff.yassg.dynamic.likes.LikeJDBCStorageBackend;
import com.tingleff.yassg.dynamic.likes.LikeServiceHandler;
import com.tingleff.yassg.dynamic.likes.ThriftLikeDaemon;
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

	@Parameter(names = "-secret", description = "Secret (used for id generation)", required = true)
	private String secret;

	@Parameter(names = "-dir", description = "Index directory", required = true)
	private String directory;

	@Parameter(names = "-field", description = "Index search fields", required = false)
	private List<String> fields;

	@Parameter(names = "-driverClass", description = "JDBC driver class", required = true)
	private String driverClass;

	@Parameter(names = "-url", description = "JDBC url", required = true)
	private String url;

	@Parameter(names = "-username", description = "JDBC username", required = true)
	private String username;

	@Parameter(names = "-password", description = "JDBC password", required = true)
	private String password;

	@Parameter(names = "-searchPort", description = "Search service daemon port (default 9999)", required = false)
	private int searchPort = 9999;

	@Parameter(names = "-sessionPort", description = "Session service daemon port (default 9998)", required = false)
	private int sessionPort = 9998;

	@Parameter(names = "-likesPort", description = "Likes service daemon port (default 9997)", required = false)
	private int likesPort = 9997;

	public void run() throws Exception {
		final SessionServiceHandler sessions = new SessionServiceHandler(secret);
		Thread t1 = runSearchDaemon();
		Thread t2 = runSessionDaemon(sessions);
		Thread t3 = runLikeDaemon(sessions);
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

	private Thread runSessionDaemon(final SessionServiceHandler sessions) throws Exception {
		Thread t = new Thread(new Runnable() {
			public void run() {
				try {
					ThriftSessionDaemon daemon = new ThriftSessionDaemon(sessions, sessionPort);
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

	private Thread runLikeDaemon(final SessionServiceHandler sessions) throws Exception {
		Thread t = new Thread(new Runnable() {
			public void run() {
				try {
					LikeJDBCStorageBackend likesBackend = new LikeJDBCStorageBackend();
					likesBackend.setDriverClass(driverClass);
					likesBackend.setUrl(url);
					likesBackend.setUsername(username);
					likesBackend.setPassword(password);
					likesBackend.init();
					LikeServiceHandler proxy = new LikeServiceHandler(sessions, likesBackend);
					ThriftLikeDaemon daemon = new ThriftLikeDaemon(proxy, likesPort);
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
