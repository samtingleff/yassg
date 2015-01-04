package com.tingleff.yassg.index.lucene;

import java.io.File;
import java.io.IOException;
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

public class LuceneIndexService implements IndexService {

	private DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyyMMddHHmm");

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
		writer.addDocument(doc);
	}

	public void commit() throws IOException {
		writer.commit();
	}
}
