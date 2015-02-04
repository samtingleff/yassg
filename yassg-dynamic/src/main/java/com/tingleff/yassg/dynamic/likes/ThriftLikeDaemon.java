package com.tingleff.yassg.dynamic.likes;

import java.net.InetSocketAddress;

import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;

import com.tingleff.yassg.search.types.TLikeService;

public class ThriftLikeDaemon {

	private int port = 9997;

	private LikeServiceHandler likes;

	private TServerTransport serverTransport;

	private TServer server;

	private TLikeService.Processor<TLikeService.Iface> processor;

	public ThriftLikeDaemon(LikeServiceHandler likes, int port) {
		this.likes = likes;
		this.port = port;
	}

	public ThriftLikeDaemon(LikeServiceHandler searcher) {
		this(searcher, 9997);
	}

	public void run() throws Exception {
		processor = new TLikeService.Processor<TLikeService.Iface>(likes);
		serverTransport = new TServerSocket(new InetSocketAddress("127.0.0.1", port));
		server = new TThreadPoolServer(
				new TThreadPoolServer.Args(serverTransport)
					.protocolFactory(new TCompactProtocol.Factory())
					.processor(processor));
		server.serve();
	}
}
