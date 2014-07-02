package com.tingleff.yassg.model;

import java.util.Set;

import org.joda.time.DateTime;

public class Page {

	private long modified;

	private long id;

	private String author;

	private String title;

	private String keywords;

	private String description;

	private Set<String> tags;

	private String href;

	private DateTime pubDate;

	private String body;

	public Page(long modified,
			long id,
			String author,
			String title,
			String keywords,
			String description,
			Set<String> tags,
			String href,
			DateTime pubDate,
			String body) {
		this.modified = modified;
		this.id = id;
		this.author = author;
		this.title = title;
		this.keywords = keywords;
		this.description = description;
		this.tags = tags;
		this.href = href;
		this.pubDate = pubDate;
		this.body = body;
	}

	public Page() {
	}

	public long getModified() {
		return modified;
	}

	public long getId() {
		return id;
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

	public String getHref() {
		return href;
	}

	public DateTime getPubDate() {
		return pubDate;
	}

	public String getBody() {
		return body;
	}
}
