package com.tingleff.yassg.search;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.DoubleField;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.tingleff.yassg.search.types.TExplain;
import com.tingleff.yassg.search.types.TExplainTerm;
import com.tingleff.yassg.search.types.TSearchDoc;
import com.tingleff.yassg.search.types.TSearchResult;
import com.tingleff.yassg.search.types.TSort;
import com.tingleff.yassg.search.types.TSortField;
import com.tingleff.yassg.search.types.TSortFieldType;

@RunWith(JUnit4.class)
public class LuceneSearchServiceTestCase {
	private String indexDirectory = "src/test/tmp";

	private SearchService search;

	@Test
	public void testSingleFieldSearchNullSort() throws Exception {
		TSearchResult result = search.search(
				"title:Gigabytes AND score:[0 TO 2]", 10, null);
		List<TSearchDoc> docs = result.getHits();
		Assert.assertEquals(1, docs.size());
		TSearchDoc d = docs.get(0);
		Map<String, String> fields = d.getFields();
		Assert.assertEquals(5, fields.size());
		Assert.assertEquals("Managing Gigabytes", fields.get("title"));
		Assert.assertEquals("55063554A", fields.get("isbn"));
		Assert.assertEquals("0.294", fields.get("score"));
	}

	@Test
	public void testMultipleFieldSearchNullSort() throws Exception {
		TSearchResult result = search.search("lucene", 10, null);
		List<TSearchDoc> docs = result.getHits();
		Assert.assertEquals(2, docs.size());
		TSearchDoc d0 = docs.get(0);
		Map<String, String> fields0 = d0.getFields();
		Assert.assertEquals(5, fields0.size());
		Assert.assertEquals("Lucene in Action", fields0.get("title"));
		Assert.assertEquals("193398817", fields0.get("isbn"));
		Assert.assertEquals("5.5", fields0.get("score"));
		TSearchDoc d1 = docs.get(1);
		Map<String, String> fields1 = d1.getFields();
		Assert.assertEquals(5, fields1.size());
		Assert.assertEquals("Lucene for Dummies", fields1.get("title"));
		Assert.assertEquals("55320055Z", fields1.get("isbn"));
		Assert.assertEquals("12.2", fields1.get("score"));
	}

	@Test
	public void testMultipleFieldSearchWithSort() throws Exception {
		TSort sort = new TSort();
		TSortField searchSortCriteria = new TSortField();
		searchSortCriteria.setField("score");
		searchSortCriteria.setType(TSortFieldType.Double);
		searchSortCriteria.setReverse(true);
		sort.addToSortFields(searchSortCriteria);
		TSearchResult result = search.search("lucene", 10, sort);
		List<TSearchDoc> docs = result.getHits();
		Assert.assertEquals(2, docs.size());
		TSearchDoc d1 = docs.get(0);
		Map<String, String> fields1 = d1.getFields();
		Assert.assertEquals(5, fields1.size());
		Assert.assertEquals("Lucene for Dummies", fields1.get("title"));
		Assert.assertEquals("55320055Z", fields1.get("isbn"));
		Assert.assertEquals("12.2", fields1.get("score"));
		TSearchDoc d0 = docs.get(1);
		Map<String, String> fields0 = d0.getFields();
		Assert.assertEquals(5, fields0.size());
		Assert.assertEquals("Lucene in Action", fields0.get("title"));
		Assert.assertEquals("193398817", fields0.get("isbn"));
		Assert.assertEquals("5.5", fields0.get("score"));
	}

	@Test
	public void testSimilar() throws Exception {
		TSearchResult result = search.similar("id:1", 10, null);
		List<TSearchDoc> docs = result.getHits();
		Assert.assertEquals(1, docs.size());
		TSearchDoc d1 = docs.get(0);
		Map<String, String> fields1 = d1.getFields();
		Assert.assertEquals(5, fields1.size());
		Assert.assertEquals("Lucene for Dummies", fields1.get("title"));
		Assert.assertEquals("55320055Z", fields1.get("isbn"));
		Assert.assertEquals("12.2", fields1.get("score"));
		TExplain explain = d1.getExplain();
		Assert.assertNotNull(explain);
		List<TExplainTerm> terms = explain.getTerms();
		Assert.assertNotNull(terms);
		Assert.assertEquals(1, terms.size());
		Assert.assertEquals("lucene", terms.get(0).getValue());
	}

	@Before
	public void setUp() throws Exception {
		buildTestIndex();
		search = new LuceneSearchService(indexDirectory, new String[] {
				"title", "isbn", "tag" });
		((LuceneSearchService) search).init();
	}

	@After
	public void tearDown() throws Exception {
		((LuceneSearchService) search).close();
		File[] children = new File(indexDirectory).listFiles();
		for (File f : children) {
			f.delete();
		}
	}

	private void buildTestIndex() throws Exception {
		StandardAnalyzer analyzer = new StandardAnalyzer();
		Directory directory = FSDirectory.open(new File(indexDirectory));
		IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_4_10_3,
				analyzer);
		IndexWriter w = new IndexWriter(directory, config);
		addDoc(w, "1", "Lucene in Action", "193398817", 5.5, "lucene");
		addDoc(w, "2", "Lucene for Dummies", "55320055Z", 12.2, "lucene");
		addDoc(w, "3", "Managing Gigabytes", "55063554A", 0.294, "books");
		addDoc(w, "4", "The Art of Computer Science", "9900333X", 91.1311, "foobar", "cs", "books");
		w.close();
	}

	private void addDoc(IndexWriter w, String id, String title, String isbn, double score, String... tags)
			throws IOException {
		Document doc = new Document();
		doc.add(new TextField("id", id, Field.Store.YES));
		doc.add(new TextField("title", title, Field.Store.YES));
		doc.add(new StringField("isbn", isbn, Field.Store.YES));
		doc.add(new DoubleField("score", score, Field.Store.YES));
		for (String tag : tags) {
			doc.add(new StringField("tag", tag, Field.Store.YES));
		}
		w.addDocument(doc);
	}

}
