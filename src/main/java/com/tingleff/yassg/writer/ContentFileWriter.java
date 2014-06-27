package com.tingleff.yassg.writer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.apache.commons.io.IOUtils;
import org.joda.time.DateTime;

import com.tingleff.yassg.model.Page;

public class ContentFileWriter {

	private DateFormat df = new SimpleDateFormat("yyyy/MM/dd");

	private File dest;

	public ContentFileWriter(String dest) {
		this.dest = new File(dest);
	}

	public boolean shouldWritePage(Page p) {
		DateTime now = new DateTime();
		if (now.isBefore(p.getPubDate())) {
			return false;
		}
		File output = getPath(p);
		if (!output.exists()) {
			return true;
		}
		if (output.lastModified() >= p.getModified()) {
			return false;
		}
		return true;
	}

	public void writePage(Page p, String body) throws IOException {
		File output = getPath(p);
		if (!shouldWritePage(p))
			return;
		File parent = output.getParentFile();
		parent.mkdirs();
		FileOutputStream os = new FileOutputStream(output);
		try {
			IOUtils.write(body, os);
		} finally {
			os.close();
		}
	}

	public void writeIndex(String body) throws IOException {
		File output = new File(dest, "index.html");
		File parent = output.getParentFile();
		parent.mkdirs();
		FileOutputStream os = new FileOutputStream(output);
		try {
			IOUtils.write(body, os);
		} finally {
			os.close();
		}
	}

	File getPath(Page p) {
		File output = new File(dest,
				String.format("%1$s/%2$s/index.html",
						df.format(p.getPubDate().toDate()),
						p.getSlug()));
		return output;
	}
}
