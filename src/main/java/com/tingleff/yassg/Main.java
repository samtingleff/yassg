package com.tingleff.yassg;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.IOUtils;

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

	// Fri, 21 Dec 2012 10:00:01 GMT
	private static DateFormat rssDateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z");

	private DateFormat hrefDateFormat = new SimpleDateFormat("yyyy/MM/dd");

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
		pageTemplateEngine = new StringTemplate4Engine();
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
		TemplateInstance ti = pageTemplateEngine.parse(readTemplate("index.st"));
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
		TemplateInstance ti = pageTemplateEngine.parse(readTemplate("feed.st"));
		ti.put("buildDate", rssDateFormat.format(now));
		ti.put("items", rendered);
		String body = ti.render();
		writer.writeFeed(body);
	}

	private void writePage(Page page) throws IOException {
		if (!writer.shouldWritePage(page))
			return;
		TemplateInstance ti = pageTemplateEngine.parse(readTemplate("post.st"));
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
		RenderedPage r = new RenderedPage(
				p.getAuthor(),
				p.getTitle(),
				p.getKeywords(),
				p.getDescription(),
				p.getTags(),
				p.getSlug(),
				htmlDateFormat.format(p.getPubDate().toDate()),
				rssDateFormat.format(p.getPubDate().toDate()),
				String.format("%1$s/%2$s/",
						hrefDateFormat.format(p.getPubDate().toDate()),
						p.getSlug()),
				renderedBody);
		return r;
	}

	private String readTemplate(String id) throws IOException {
		File f = new File(templateDir, id);
		FileInputStream fis = new FileInputStream(f);
		try {
			return IOUtils.toString(fis);
		} finally {
			fis.close();
		}
	}
}
