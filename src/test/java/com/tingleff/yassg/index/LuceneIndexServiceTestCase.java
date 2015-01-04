package com.tingleff.yassg.index;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.tingleff.yassg.index.lucene.LuceneIndexService;
import com.tingleff.yassg.model.Page;

@RunWith(JUnit4.class)
public class LuceneIndexServiceTestCase {

	private String indexDirectory = "src/test/tmp";

	private String[] searchFields = new String[] {
			"id", "author", "title", "keywords", "description", "tag", "href", "date", "body"
	};

	private IndexService index;

	private IndexReader reader;

	private IndexSearcher searcher;

	private StandardAnalyzer analyzer;

	private MultiFieldQueryParser parser;

	@Test
	public void testPageSearch() throws IOException, ParseException {
		Page p1 = createPage(new DateTime(), true);
		index.indexPage(p1);

		((LuceneIndexService) index).commit();
		openReader();
		// search for it
		Query q = parser.parse("gigabytes AND tag:compsci");
		TopDocs topDocs = searcher.search(q, 10);
		ScoreDoc[] hits = topDocs.scoreDocs;
		Assert.assertEquals(1, hits.length);
	}

	@Before
	public void setUp() throws IOException {
		index = new LuceneIndexService(indexDirectory);
		((LuceneIndexService) index).open();
	}

	@After
	public void tearDown() throws IOException {
		((LuceneIndexService) index).close();
		File[] children = new File(indexDirectory).listFiles();
		for (File f : children) {
			f.delete();
		}
	}

	private Page createPage(DateTime pubDate, boolean noindex) {
		Page p = new Page(System.currentTimeMillis(),
				104391493145l,
				"author dude",
				"title dude",
				"managing,gigabytes",
				"Managing Gigabytes",
				new HashSet<String>(Arrays.asList("gigabytes", "managing", "compsci")),
				"slug-dude",
				noindex,
				pubDate,
				"body here");
		return p;
	}

	private void openReader() throws IOException {
		Directory dir = FSDirectory.open(new File(this.indexDirectory));
		reader = DirectoryReader.open(dir);
		searcher = new IndexSearcher(reader);
		analyzer = new StandardAnalyzer();
		parser = new MultiFieldQueryParser(searchFields, analyzer);
	}
}
