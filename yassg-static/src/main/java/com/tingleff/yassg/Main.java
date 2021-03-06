package com.tingleff.yassg;

import java.io.IOException;
import java.net.URI;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.commons.codec.binary.Hex;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.tingleff.yassg.content.ContentDB;
import com.tingleff.yassg.formats.TemplateEngine;
import com.tingleff.yassg.formats.TemplateInstance;
import com.tingleff.yassg.formats.mk.MarkdownTemplateEngine;
import com.tingleff.yassg.formats.st4.StringTemplate4Engine;
import com.tingleff.yassg.index.IndexService;
import com.tingleff.yassg.index.lucene.LuceneIndexService;
import com.tingleff.yassg.model.Page;
import com.tingleff.yassg.model.RenderedPage;
import com.tingleff.yassg.pagedb.PageDB;
import com.tingleff.yassg.pagedb.file.FilePageDB;
import com.tingleff.yassg.search.LuceneSearchService;
import com.tingleff.yassg.search.SearchService;
import com.tingleff.yassg.search.types.TSearchResult;
import com.tingleff.yassg.semantic.AlphaSemanticClient;
import com.tingleff.yassg.semantic.NamedEntity;
import com.tingleff.yassg.semantic.NamedEntityResponse;
import com.tingleff.yassg.semantic.SemanticClient;
import com.tingleff.yassg.writer.ContentFileWriter;

public class Main {

	public static void main(String[] args) throws Exception {
		Main m = new Main();
		new JCommander(m).parse(args);
		m.run();
	}

	private static DateFormat htmlDateFormat = new SimpleDateFormat("EEEEE, MMMMM dd @ HH:mm");

	private static DateFormat htmlDateFormatShort = new SimpleDateFormat("MMM dd, yyyy");

	// Fri, 21 Dec 2012 10:00:01 GMT
	private static DateFormat rssDateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z");

	// 2002-10-02T10:00:00-05:00
	// http://www.faqs.org/rfcs/rfc3339.html
	private static DateFormat atomDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

	@Parameter(names = "-content", required = true)
	private String contentDir;

	@Parameter(names = "-templates", required = true)
	private String templateDir;

	@Parameter(names = "-output", required = true)
	private String outputDir;

	@Parameter(names = "-index", required = true)
	private String indexDir;

	@Parameter(names = "-semanticUrl", required = false)
	private String semanticUrl;

	@Parameter(names = "-semanticSecret", required = false)
	private String semanticSecret;

	@Parameter(names = "-semanticCacheDir", required = false)
	private String semanticCacheDir;

	@Parameter(names = "-verbose")
	private boolean verbose = false;

	@Parameter(names = "-index-count")
	private int indexCount = 10;

	private PageDB pagedb;

	private ContentDB contentdb;

	private TemplateEngine pageTemplateEngine;

	private TemplateEngine bodyTemplateEngine;

	private ContentFileWriter writer;

	private SemanticClient semanticClient;

	private IndexService indexService;

	public void run() throws Exception {
		pagedb = new FilePageDB(contentDir);
		contentdb = new ContentDB();
		pageTemplateEngine = new StringTemplate4Engine(templateDir);
		bodyTemplateEngine = new MarkdownTemplateEngine();
		writer = new ContentFileWriter(outputDir);
		if (semanticUrl == null) {
			// fallback on environment variables
			semanticUrl = System.getenv("NER_URL");
			semanticSecret = System.getenv("NER_SECRET");
		}
		if (semanticUrl != null) {
			semanticClient = new AlphaSemanticClient(
					semanticUrl, semanticSecret, semanticCacheDir).init();
		}
		indexService = new LuceneIndexService(indexDir);
		indexService.open();

		// iterate once through pages once to hit AlchemyAPI and index
		Iterable<Page> iter = pagedb.iterator();
		for (Page page : iter) {
			contentdb.addPage(page);
			RenderedPage rp = indexPage(page);
		}
		indexService.close();

		// get an index searcher
		LuceneSearchService searcher = new LuceneSearchService(
				this.indexDir, SearchService.DefaultSimilaritySearchFields);
		searcher.init();

		// iterate through again to write out
		iter = pagedb.iterator();
		for (Page page : iter) {
			TSearchResult related = searcher.similar("id:" + Long.toHexString(page.getId()), 3, null);
			writePage(page, related);
		}

		// write out /index.html
		writeIndex();

		// write out /feed.rss
		writeRSS();

		// write out /atom.xml
		writeAtom();
	}

	private void writeIndex() throws IOException {
		Date now = new Date();
		List<Page> pages = contentdb.index(indexCount);
		List<RenderedPage> rendered = new ArrayList<RenderedPage>(pages.size());
		for (Page p : pages) {
			rendered.add(render(p));
		}
		TemplateInstance ti = pageTemplateEngine.parse("index");
		ti.put("buildDate", htmlDateFormat.format(now));
		ti.put("items", rendered);
		String body = ti.render();
		writer.writeIndex(body);
	}

	private void writeRSS() throws IOException {
		Date now = new Date();
		List<Page> pages = contentdb.index(indexCount);
		List<RenderedPage> rendered = new ArrayList<RenderedPage>(pages.size());
		for (Page p : pages) {
			rendered.add(render(p));
		}
		TemplateInstance ti = pageTemplateEngine.parse("rss");
		ti.put("buildDate", rssDateFormat.format(now));
		ti.put("items", rendered);
		String body = ti.render();
		writer.writeFeed(body, "feed.rss");
	}

	private void writeAtom() throws IOException {
		Date now = new Date();
		List<Page> pages = contentdb.index(indexCount);
		List<RenderedPage> rendered = new ArrayList<RenderedPage>(pages.size());
		for (Page p : pages) {
			rendered.add(render(p));
		}
		TemplateInstance ti = pageTemplateEngine.parse("atom");
		ti.put("buildDate", atomDateFormat.format(now));
		ti.put("items", rendered);
		String body = ti.render();
		writer.writeFeed(body, "atom.xml");
	}

	private void writePage(Page page, TSearchResult related) throws IOException {
		if (!writer.shouldWritePage(page))
			return;
		RenderedPage renderedPage = render(page);
		TemplateInstance ti = pageTemplateEngine.parse("post");
		ti.put("page", renderedPage);
		ti.put("attr", page.getAttributes());
		ti.put("related", related.getHits());
		String body = ti.render();
		writer.writePage(page, body);
	}

	private RenderedPage render(Page p) throws IOException {
		String renderedBody = bodyTemplateEngine.parse(p.getBody()).render();
		String id = getId(p, renderedBody);
		RenderedPage r = new RenderedPage(
				id,
				p.getAuthor(),
				p.getTitle(),
				p.getKeywords(),
				p.getDescription(),
				p.getTags(),
				p.getHref(),
				htmlDateFormat.format(p.getPubDate().toDate()),
				htmlDateFormatShort.format(p.getPubDate().toDate()),
				atomDateFormat.format(p.getPubDate().toDate()),
				p.getHref(),
				renderedBody);
		return r;
	}

	private RenderedPage indexPage(Page p) throws IOException {
		// render markdown body
		RenderedPage renderedPage = render(p);

		// extract links
		String htmlBody = renderedPage.getBody();
		Document doc = Jsoup.parseBodyFragment(htmlBody);
		Element body = doc.body();
		List<Element> links = body.getElementsByTag("a");
		List<String> urls = new ArrayList<String>(links.size());
		Set<String> domains = new HashSet<String>(links.size());
		for (Element link : links) {
			String nofollow = link.attr("rel");
			if ((nofollow == null) || (!"nofollow".equals(nofollow))) {
				String href = link.attr("href");
				if ((href != null) && (href.startsWith("http"))) {
					urls.add(link.attr("href"));
					// strip out domain
					try {
						URI uri = new URI(href);
						String domain = uri.getHost();
						domains.add(domain);
					} catch(Exception e) { }
				}
			}
		}

		// send to semantic client
		List<NamedEntity> entities = new LinkedList<NamedEntity>();
		if (semanticUrl != null) {
			for (String url : urls) {
				NamedEntityResponse response = null;
				try {
					response = semanticClient.namedEntities(url);
					if (response.isSuccess()) {
						for (NamedEntity ne : response.getResult().getEntities()) {
							entities.add(ne);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				/*try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
				}*/
			}
		}
		indexService.indexPage(p, entities, domains);

		return renderedPage;
	}

	private String getId(Page p, String body) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			md.update(p.getTitle().getBytes());
			md.update(p.getHref().getBytes());
			md.update(body.getBytes());
			byte[] bytes = md.digest();
			return Hex.encodeHexString(bytes);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("Damn you java");
		} finally {
		}
	}
}
