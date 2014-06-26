package com.tingleff.yassg.pagedb.file;

import java.util.Set;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement()
class PageMetaTO {

	@XmlElement(name = "author")
	private String author;

	@XmlElement(name = "title")
	private String title;

	@XmlElement(name = "keywords")
	private String keywords;

	@XmlElement(name = "description")
	private String description;

	@XmlElement(name = "tags")
	private Set<String> tags;

	@XmlElement(name = "slug")
	private String slug;

	@XmlElement(name = "pubDate")
	private String pubDate;

	public PageMetaTO() { }

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set<String> getTags() {
		return tags;
	}

	public void setTags(Set<String> tags) {
		this.tags = tags;
	}

	public String getSlug() {
		return slug;
	}

	public void setSlug(String slug) {
		this.slug = slug;
	}

	public String getPubDate() {
		return pubDate;
	}

	public void setPubDate(String pubDate) {
		this.pubDate = pubDate;
	}
}
