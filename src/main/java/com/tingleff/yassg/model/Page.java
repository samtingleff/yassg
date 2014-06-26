package com.tingleff.yassg.model;

import java.util.Set;

import org.joda.time.DateTime;

public class Page {

	private String author;

	private String title;

	private String keywords;

	private String description;

	private Set<String> tags;

	private String slug;

	private DateTime pubDate;

	private String body;

	public Page(String author,
			String title,
			String keywords,
			String description,
			Set<String> tags,
			String slug,
			DateTime pubDate,
			String body) {
		this.author = author;
		this.title = title;
		this.keywords = keywords;
		this.description = description;
		this.tags = tags;
		this.slug = slug;
		this.pubDate = pubDate;
		this.body = body;
	}

	public Page() {
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

	public DateTime getPubDate() {
		return pubDate;
	}

	public String getBody() {
		return body;
	}
}
