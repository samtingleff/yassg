package com.tingleff.yassg.formats.mk.plugin;

import java.util.List;
import java.util.Map;

import org.markdown4j.Plugin;

public class FlickrPlugin extends Plugin {

	public FlickrPlugin() {
		super("flickr");
	}

	@Override
	public void emit(StringBuilder sb, List<String> lines,
			Map<String, String> params) {
		String width = params.get("width");
		if (width == null)
			width = "500";
		String height = params.get("height");
		if (height == null)
			height = "375";
		String user = params.get("user");
		if (user == null)
			user = "samtingleff";
		String id = params.get("id");
		if (id == null)
			id = "14521186741";

		sb.append(String.format(
				"<iframe src=\"https://www.flickr.com/photos/%1$s/%2$s/player/\" width=\"%3$s\" height=\"%4$s\" frameborder=\"0\" allowfullscreen webkitallowfullscreen mozallowfullscreen oallowfullscreen msallowfullscreen></iframe>",
				user,
				id,
				width,
				height));
	}

}
