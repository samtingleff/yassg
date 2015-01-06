package com.tingleff.yassg.index.lucene;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.tingleff.yassg.index.IndexService;
import com.tingleff.yassg.model.Page;
import com.tingleff.yassg.semantic.NamedEntity;

public class LuceneIndexService implements IndexService {

	private static final DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyyMMddHHmm");

	// A subset from http://www.alchemyapi.com/api/entity/types/
	private static final Set<String> indexedTypes = new HashSet<String>(Arrays.asList(
			"Company",
			"Country",
			"FieldTerminology",
			"Organization",
			"Person",
			"Technology"));

	private String indexDirectory;

	private StandardAnalyzer analyzer;

	private IndexWriter writer;

	public LuceneIndexService(String dir) {
		this.indexDirectory = dir;
	}

	public LuceneIndexService() { }

	public void open() throws IOException {
		analyzer = new StandardAnalyzer();
		Directory directory = FSDirectory.open(new File(indexDirectory));
		IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_4_10_3,
				analyzer);
		writer = new IndexWriter(directory, config);
	}

	public void close() throws IOException {
		writer.close();
	}

	public void indexPage(Page page) throws IOException {
		Document doc = new Document();
		decorate(doc, page);
		writer.addDocument(doc);
	}

	public void indexPage(Page page, Iterable<NamedEntity> entities) throws IOException {
		Document doc = new Document();
		decorate(doc, page);
		decorate(doc, entities);
		writer.addDocument(doc);
	}

	public void commit() throws IOException {
		writer.commit();
	}

	private Document decorate(Document doc, Page page) {
		doc.add(new StringField("id",Long.toHexString(page.getId()), Field.Store.YES));
		doc.add(new TextField("author", page.getAuthor(), Field.Store.YES));
		doc.add(new TextField("title", page.getTitle(), Field.Store.YES));
		doc.add(new TextField("keywords", page.getKeywords(), Field.Store.YES));
		doc.add(new TextField("description", page.getDescription(), Field.Store.YES));
		Set<String> tags = page.getTags();
		for (String tag : tags) {
			doc.add(new StringField("tag", tag, Field.Store.YES));
		}
		doc.add(new StringField("href", page.getHref(), Field.Store.YES));
		doc.add(new StringField("date", page.getPubDate().toString(dtf), Field.Store.YES));
		doc.add(new TextField("body", page.getBody(), Field.Store.NO));
		for (Map.Entry<String, String> e : page.getAttributes().entrySet()) {
			doc.add(new StringField(e.getKey(), e.getValue(), Field.Store.YES));
		}
		return doc;
	}

	private void decorate(Document doc, Iterable<NamedEntity> entities) {
		for (NamedEntity ne : entities) {
			String type = ne.getType();
			if (indexedTypes.contains(type)) {
				doc.add(new TextField(ne.getType().toLowerCase(), ne.getText(), Field.Store.YES));
			}
		}
	}
}
