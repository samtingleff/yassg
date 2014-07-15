package com.tingleff.yassg.formats.mk.plugin;

import java.util.List;
import java.util.Map;

import org.markdown4j.Plugin;

public class SoundcloudPlugin extends Plugin {

	public SoundcloudPlugin() {
		super("soundcloud");
	}

	@Override
	public void emit(StringBuilder sb, List<String> lines,
			Map<String, String> params) {
		String height = params.get("height");
		if (height == null)
			height = "166";
		for (String line : lines) {
			String track = line;
			sb.append(
					"<iframe width=\"100%\" height=\"{height}\" scrolling=\"no\" frameborder=\"no\" src=\"https://w.soundcloud.com/player/?url=https%3A//api.soundcloud.com/tracks/{track}&amp;color=ff5500&amp;auto_play=false&amp;hide_related=false&amp;show_comments=true&amp;show_user=true&amp;show_reposts=false\"></iframe>"
					.replace("{track}", track)
					.replace("{height}", height));
		}
	}

}
