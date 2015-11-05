package com.tingleff.yassg.formats.mk.plugin;

import java.util.List;
import java.util.Map;

import org.markdown4j.Plugin;

/**
 * 
 * See http://stackoverflow.com/questions/24739663/embebing-instagram-webpage-inside-an-iframe
 *
 */
public class InstagramPlugin extends Plugin {

	public InstagramPlugin() {
		super("ig");
	}

	@Override
	public void emit(StringBuilder sb, List<String> lines,
			Map<String, String> params) {
		String width = params.get("width");
		if (width == null)
			width = "320";
		String height = params.get("height");
		if (height == null)
			height = "440";
		for (String line : lines) {
		String id = line;
		sb.append(String
				.format("<iframe width=\"%1$d\" height=\"%2$d\" src=\"http://instagram.com/p/%3$s/embed\" frameborder=\"0\"></iframe>",
						Integer.parseInt(width), Integer.parseInt(height), id));
		}
	}
}
