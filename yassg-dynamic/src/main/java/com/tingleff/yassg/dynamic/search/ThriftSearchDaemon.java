package com.tingleff.yassg.dynamic.search;

import java.net.InetSocketAddress;

import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;

import com.tingleff.yassg.search.types.TSearchService;

public class ThriftSearchDaemon {

	private int port = 9999;

	private SearchServiceHandler searcher;

	private TServerTransport serverTransport;

	private TServer server;

	private TSearchService.Processor<TSearchService.Iface> processor;

	public ThriftSearchDaemon(SearchServiceHandler searcher, int port) {
		this.searcher = searcher;
		this.port = port;
	}

	public ThriftSearchDaemon(SearchServiceHandler searcher) {
		this(searcher, 9999);
	}

	public void run() throws Exception {
		processor = new TSearchService.Processor<TSearchService.Iface>(searcher);
		serverTransport = new TServerSocket(new InetSocketAddress("127.0.0.1", port));
		server = new TThreadPoolServer(
				new TThreadPoolServer.Args(serverTransport)
					.protocolFactory(new TCompactProtocol.Factory())
					.processor(processor));
		server.serve();
	}
}
