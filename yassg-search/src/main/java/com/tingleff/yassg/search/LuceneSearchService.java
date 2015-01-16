package com.tingleff.yassg.search;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.queries.mlt.MoreLikeThis;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.SearcherFactory;
import org.apache.lucene.search.SearcherManager;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import com.tingleff.yassg.search.types.TSearchDoc;
import com.tingleff.yassg.search.types.TSearchException;
import com.tingleff.yassg.search.types.TSearchResult;
import com.tingleff.yassg.search.types.TSort;
import com.tingleff.yassg.search.types.TSortField;

public class LuceneSearchService implements SearchService {

	private String directory;

	private String[] searchFields;

	//private IndexReader reader;

	private Directory dir;

	private SearcherManager searcherManager;

	private StandardAnalyzer analyzer;

	private MultiFieldQueryParser parser;

	public LuceneSearchService() { }

	public LuceneSearchService(String directory, String[] searchFields) {
		this.directory = directory;
		this.searchFields = searchFields;
	}

	public void init() throws Exception {
		this.dir = FSDirectory.open(new File(this.directory));
		//this.reader = DirectoryReader.open(dir);
		searcherManager = new SearcherManager(dir, new SearcherFactory());
		analyzer = new StandardAnalyzer();
		parser = new MultiFieldQueryParser(searchFields, analyzer);
	}

	public void close() throws IOException {
		searcherManager.close();
	}

	public TSearchResult search(String query, int n, TSort sort) throws IOException, TSearchException {
		Query q = null;
		try {
			q = parser.parse(query);
		} catch (ParseException e) {
			// TODO: log me?
			e.printStackTrace();
			throw new TSearchException();
		} finally { }
		Sort sorting = buildSort(sort);
		IndexSearcher searcher = searcherManager.acquire();
		try {
			TopDocs topDocs = (sorting == null) ? searcher.search(q, n)
					: searcher.search(q, n, sorting);
			TSearchResult sr = convert(searcher, topDocs, null);
			return sr;
		} finally {
			searcherManager.release(searcher);
			searcher = null;
		}
	}

	public TSearchResult similar(int targetDocId, int n, TSort sort) throws IOException, TSearchException {
		IndexSearcher searcher = searcherManager.acquire();
		IndexReader reader = DirectoryReader.open(this.dir);
		Sort sorting = buildSort(sort);
		try {
			MoreLikeThis mlt = new MoreLikeThis(reader);
			mlt.setMinTermFreq(0);
			mlt.setMinDocFreq(0);
			mlt.setAnalyzer(this.analyzer);
			mlt.setFieldNames(this.searchFields);
			mlt.setBoost(true);
			Query q = mlt.like(targetDocId);
			TopDocs topDocs = (sorting == null) ? searcher.search(q, n)
					: searcher.search(q, n, sorting);

			TSearchResult sr = convert(searcher, topDocs, new Integer(
					targetDocId));
			return sr;
		} finally {
			searcherManager.release(searcher);
			reader.close();
			searcher = null;
		}
	}

	public synchronized void reopen() throws IOException {
		searcherManager.maybeRefresh();
	}

	private TSearchResult convert(IndexSearcher searcher, TopDocs topDocs, Integer excludeDocId) throws IOException {
		ScoreDoc[] hits = topDocs.scoreDocs;
		List<TSearchDoc> results = new ArrayList<TSearchDoc>(hits.length);
		for (int i = 0; i < hits.length; ++i) {
			int docId = hits[i].doc;
			if ((excludeDocId != null) && (excludeDocId.intValue() == docId)) {
				continue;
			}
			TSearchDoc doc = new TSearchDoc();
			doc.setDocId(docId);
			Document d = searcher.doc(docId);
			List<IndexableField> fields = d.getFields();
			for (IndexableField f : fields) {
				doc.putToFields(f.name(), f.stringValue());
			}
			results.add(doc);
		}
		TSearchResult sr = new TSearchResult();
		sr.setHits(results);
		return sr;
	}

	private Sort buildSort(TSort searchSort) {
		if ((searchSort == null) || (searchSort.getSortFields() == null) || (searchSort.getSortFields().size() == 0))
			return null;
		List<TSortField> fields = searchSort.getSortFields();
		SortField[] resultFields = new SortField[fields.size()];
		int i = 0;
		for (TSortField f : fields) {
			resultFields[i] = buildSortField(f);
			++i;
		}
		return new Sort(resultFields);
	}

	private SortField buildSortField(TSortField sortField) {
		SortField.Type type = SortField.Type.SCORE;
		switch (sortField.getType()) {
			case IndexOrder:
				type = SortField.Type.DOC;
				break;
			case Double:
				type = SortField.Type.DOUBLE;
				break;
			case Float:
				type = SortField.Type.FLOAT;
				break;
			case Integer:
				type = SortField.Type.INT;
				break;
			case Long:
				type = SortField.Type.LONG;
				break;
			case Relevance:
				type = SortField.Type.SCORE;
				break;
			case String:
				type = SortField.Type.STRING;
				break;
			default:
				type = SortField.Type.SCORE;
				break;
		}
		SortField sf = new SortField(sortField.getField(), type, sortField.isReverse());
		return sf;
	}
}
