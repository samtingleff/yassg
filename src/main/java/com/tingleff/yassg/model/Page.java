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

	public Page() {
	}
}
