package com.tingleff.yassg;

import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.codec.binary.Hex;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.tingleff.yassg.content.ContentDB;
import com.tingleff.yassg.formats.TemplateEngine;
import com.tingleff.yassg.formats.TemplateInstance;
import com.tingleff.yassg.formats.mk.MarkdownTemplateEngine;
import com.tingleff.yassg.formats.st4.StringTemplate4Engine;
import com.tingleff.yassg.model.Page;
import com.tingleff.yassg.model.RenderedPage;
import com.tingleff.yassg.pagedb.PageDB;
import com.tingleff.yassg.pagedb.file.FilePageDB;
import com.tingleff.yassg.rsync.Rsync;
import com.tingleff.yassg.rsync.RsyncCommand;
import com.tingleff.yassg.writer.ContentFileWriter;

public class Main {

	public static void main(String[] args) throws Exception {
		Main m = new Main();
		new JCommander(m).parse(args);
		m.run();
	}

	private static DateFormat htmlDateFormat = new SimpleDateFormat("EEEEE, MMMMM dd @ HH:mm");

	private static DateFormat htmlDateFormatShort = new SimpleDateFormat("MMM dd");

	// Fri, 21 Dec 2012 10:00:01 GMT
	private static DateFormat rssDateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z");

	@Parameter(names = "-content", required = true)
	private String contentDir;

	@Parameter(names = "-templates", required = true)
	private String templateDir;

	@Parameter(names = "-static", required = true)
	private String staticDir;

	@Parameter(names = "-output", required = true)
	private String outputDir;

	@Parameter(names = "-verbose")
	private boolean verbose = false;

	@Parameter(names = "-index-count")
	private int indexCount = 10;

	private PageDB pagedb;

	private ContentDB contentdb;

	private TemplateEngine pageTemplateEngine;

	private TemplateEngine bodyTemplateEngine;

	private ContentFileWriter writer;

	public void run() throws Exception {
		pagedb = new FilePageDB(contentDir);
		contentdb = new ContentDB();
		pageTemplateEngine = new StringTemplate4Engine(templateDir);
		bodyTemplateEngine = new MarkdownTemplateEngine();
		writer = new ContentFileWriter(outputDir);

		// iterate through pages
		//  - add each to content db
		//  - write each
		Iterable<Page> iter = pagedb.iterator();
		for (Page page : iter) {
			contentdb.addPage(page);
			writePage(page);
		}

		// write out /index.html
		writeIndex();

		// write out /feed.rss
		writeRSS();

		// write out /atom.xml
		writeAtom();

		// write out static
		writeStaticContent();
	}

	private void writeIndex() throws IOException {
		Date now = new Date();
		List<Page> pages = contentdb.recent(indexCount);
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
		List<Page> pages = contentdb.recent(indexCount);
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
		List<Page> pages = contentdb.recent(indexCount);
		List<RenderedPage> rendered = new ArrayList<RenderedPage>(pages.size());
		for (Page p : pages) {
			rendered.add(render(p));
		}
		TemplateInstance ti = pageTemplateEngine.parse("atom");
		ti.put("buildDate", rssDateFormat.format(now));
		ti.put("items", rendered);
		String body = ti.render();
		writer.writeFeed(body, "atom.xml");
	}

	private void writePage(Page page) throws IOException {
		if (!writer.shouldWritePage(page))
			return;
		TemplateInstance ti = pageTemplateEngine.parse("post");
		ti.put("page", render(page));
		String body = ti.render();
		writer.writePage(page, body);
	}

	private void writeStaticContent() throws Exception {
		Rsync rsync = new RsyncCommand();
		rsync.rsync(new File(staticDir), new File(outputDir, "static"), verbose, true);
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
				rssDateFormat.format(p.getPubDate().toDate()),
				p.getHref(),
				renderedBody);
		return r;
	}

	private String getId(Page p, String body) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			md.update(p.getAuthor().getBytes());
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
