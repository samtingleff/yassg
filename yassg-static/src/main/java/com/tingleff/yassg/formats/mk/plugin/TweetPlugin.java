package com.tingleff.yassg.formats.mk.plugin;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import org.markdown4j.Plugin;

/**
 * 
 * See https://twitframe.com/
 *
 */
public class TweetPlugin extends Plugin {

	public TweetPlugin() {
		super("tweet");
	}

	@Override
	public void emit(StringBuilder sb, List<String> lines,
			Map<String, String> params) {
		String user = params.get("user");
		String id = params.get("id");
		String width = params.get("width");
		if (width == null)
			width = "550";
		String height = params.get("height");
		if (height == null)
			height = "250";
		try {
			String dest = URLEncoder.encode(String.format("https://twitter.com/%1$s/status/%2$s",
					user, id), "UTF-8");
			sb.append(String
					.format("<iframe border=\"0\" frameborder=\"0\" height=\"%1$d\" width=\"%2$d\" src=\"http://twitframe.com/show?url=%3$s\"></iframe>",
							Integer.parseInt(height), Integer.parseInt(width), dest));
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Damn you java");
		}
	}
}
