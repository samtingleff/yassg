package com.tingleff.yassg;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.io.IOUtils;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.tingleff.yassg.content.ContentDB;
import com.tingleff.yassg.formats.TemplateEngine;
import com.tingleff.yassg.formats.TemplateInstance;
import com.tingleff.yassg.formats.st4.StringTemplate4Engine;
import com.tingleff.yassg.model.Page;
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

	@Parameter(names = "-input")
	private String inputDir;

	@Parameter(names = "-templates")
	private String templateDir;

	@Parameter(names = "-static")
	private String staticDir;

	@Parameter(names = "-output")
	private String outputDir;

	@Parameter(names = "-verbose")
	private boolean verbose;

	private PageDB pagedb;

	private ContentDB contentdb;

	private TemplateEngine pageTemplateEngine;

	private ContentFileWriter writer;

	public void run() throws Exception {
		pagedb = new FilePageDB(inputDir);
		contentdb = new ContentDB();
		pageTemplateEngine = new StringTemplate4Engine();
		writer = new ContentFileWriter(outputDir);

		// iterate through pages, adding to a ContentDB
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

	private void writeIndex() { }

	private void writeRSS() { }

	private void writePage(Page page) throws IOException {
		if (!writer.shouldWrite(page))
			return;
		TemplateInstance ti = pageTemplateEngine.parse(readTemplate("post.st"));
		ti.put("page", page);
		String body = ti.render();
		writer.write(page, body);
	}

	private void writeStaticContent() throws Exception {
		Rsync rsync = new RsyncCommand();
		rsync.rsync(new File(staticDir), new File(outputDir, "static"), verbose, true);
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
