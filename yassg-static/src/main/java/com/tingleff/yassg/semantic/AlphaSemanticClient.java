package com.tingleff.yassg.semantic;

import java.io.IOException;
import java.util.Collections;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;

import org.apache.commons.codec.binary.Hex;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.Dsl;
import org.asynchttpclient.Response;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tingleff.yassg.hash.HashFunction;
import com.tingleff.yassg.hash.SHA256HashFunction;

public class AlphaSemanticClient implements SemanticClient {

	private String url;

	private String secret;

	private String cacheDir;

	private AsyncHttpClient asyncHttpClient;

	private SemanticDB cache;

	private ObjectMapper mapper;

	private HashFunction sha256 = new SHA256HashFunction();

	public AlphaSemanticClient(String url, String secret, String cacheDir) {
		this.url = url;
		this.secret = secret;
		this.cacheDir = cacheDir;
	}

	public AlphaSemanticClient() { }

	public AlphaSemanticClient init() {
		this.asyncHttpClient = Dsl.asyncHttpClient(Dsl.config()
				.setThreadFactory(
						new ThreadFactory() {
							@Override
							public Thread newThread(Runnable r) {
								Thread t = new Thread(r);
								t.setDaemon(true);
								return t;
							}
							
						})
				.build());
		this.cache = new SemanticDBImpl(this.cacheDir);
		this.mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		return this;
	}

	@Override
	public NamedEntityResponse namedEntities(String url) throws IOException {
		NamedEntityResponse response = cache.lookup(url);
		if (response != null) {
			if (!response.isSuccess()) {
				long now = System.currentTimeMillis();
				if (response.getResult() != null) {
					if ((now - response.getResult().getTimestamp()) < (1000l * 60l * 60l * 24l * 7l)) {
						// do not try again for 7 days
						return response;
					}
				}
			} else {
				return response;
			}
		}
		if (response != null)
			return response;
		try {
			Future<Response> whenResponse = asyncHttpClient.prepareGet(this.url)
					.addHeader("Authorization", generateAuthHeaderValue())
					.addQueryParam("url", url)
					.execute();
			Response r = whenResponse.get();
			byte[] bytes = r.getResponseBodyAsBytes();
			NamedEntityResponse ner = mapper.readValue(bytes, NamedEntityResponse.class);
			response = ner;
			cache.save(url, response);
		} catch (Exception e) {
			NamedEntityResponse failureResponse = new NamedEntityResponse(
					false,
					new NamedEntityResult(
							"error",
							url,
							System.currentTimeMillis(),
							Collections.EMPTY_LIST));
			cache.save(url, failureResponse);
		} finally { }
		return response;
	}

	private String generateAuthHeaderValue() {
		long ts = System.currentTimeMillis();
		String input = String.format("%1$s|%2$s", ts, this.secret);
		String hash = Hex.encodeHexString(sha256.hash(input.getBytes()));
		String val = String.format("%1$s|%2$s", ts, hash);
		return val;
	}
}
