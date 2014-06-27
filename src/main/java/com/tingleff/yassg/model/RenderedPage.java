package com.tingleff.yassg.model;

import java.util.Set;

public class RenderedPage {

	private String author;

	private String title;

	private String keywords;

	private String description;

	private Set<String> tags;

	private String slug;

	private String pubDateHtml;

	private String pubDateRSS;

	private String body;

	public RenderedPage(String author,
			String title,
			String keywords,
			String description,
			Set<String> tags,
			String slug,
			String pubDateHtml,
			String pubDateRSS,
			String body) {
		this.author = author;
		this.title = title;
		this.keywords = keywords;
		this.description = description;
		this.tags = tags;
		this.slug = slug;
		this.pubDateHtml = pubDateHtml;
		this.pubDateRSS = pubDateRSS;
		this.body = body;
	}

	public String getAuthor() {
		return author;
	}

	public String getTitle() {
		return title;
	}

	public String getKeywords() {
		return keywords;
	}

	public String getDescription() {
		return description;
	}

	public Set<String> getTags() {
		return tags;
	}

	public String getSlug() {
		return slug;
	}

	public String getPubDateHtml() {
		return pubDateHtml;
	}

	public String getPubDateRSS() {
		return pubDateRSS;
	}

	public String getBody() {
		return body;
	}

}
