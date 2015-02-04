package com.tingleff.yassg.dynamic.sessions;

import java.net.InetSocketAddress;

import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;

import com.tingleff.yassg.search.types.TSessionService;

public class ThriftSessionDaemon {

	private int port = 9998;

	private SessionServiceHandler sessions;

	private TServerTransport serverTransport;

	private TServer server;

	private TSessionService.Processor<TSessionService.Iface> processor;

	public ThriftSessionDaemon(SessionServiceHandler sessions, int port) {
		this.sessions = sessions;
		this.port = port;
	}

	public ThriftSessionDaemon(SessionServiceHandler searcher) {
		this(searcher, 9998);
	}

	public void run() throws Exception {
		processor = new TSessionService.Processor<TSessionService.Iface>(sessions);
		serverTransport = new TServerSocket(new InetSocketAddress("127.0.0.1", port));
		server = new TThreadPoolServer(
				new TThreadPoolServer.Args(serverTransport)
					.protocolFactory(new TCompactProtocol.Factory())
					.processor(processor));
		server.serve();
	}
}
