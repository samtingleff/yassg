package com.tingleff.yassg.pagedb.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.joda.time.DateTime;

import com.tingleff.yassg.model.Page;
import com.tingleff.yassg.pagedb.Decorator;
import com.tingleff.yassg.pagedb.PageDB;

public class FilePageDB implements PageDB {

	private DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");

	private File root;

	public FilePageDB(String root) {
		this.root = new File(root);
	}

	private FilePageDB() { }

	@Override
	public Page read(String id) throws IOException, ParseException {
		File metaFile = new File(root, id + ".properties");
		return read(metaFile, id);
	}

	@Override
	public void decorate(Decorator<Page> pageDecorator) {
		Iterable<Page> iter = iterator();
		for (Page p : iter) {
			pageDecorator.decorate(p);
		}
	}

	@Override
	public Iterable<Page> iterator() {
		Iterator<File> metaFilesIterator = FileUtils.iterateFiles(
				root,
				new String[] { "properties" },
				true);
		return new LazyPageIterable(metaFilesIterator);
	}

	private Page read(File metaFile) throws IOException, ParseException {
		String id = metaFile.getName().substring(0, metaFile.getName().indexOf(".properties"));
		return read(metaFile, id);
	}

	private Page read(File metaFile, String id) throws IOException, ParseException {
		File bodyFile = new File(metaFile.getParentFile(), id + ".markdown");
		long modified = (metaFile.lastModified() > bodyFile.lastModified())
				? metaFile.lastModified()
				: bodyFile.lastModified();

		String body = readFile(bodyFile);

		Properties props = new Properties();
		FileInputStream fis = new FileInputStream(metaFile);
		try {
			props.load(fis);
			Set<String> tags = new HashSet<String>();
			String tagProperty = props.getProperty("tags");
			if (tagProperty != null) {
				String[] vals = tagProperty.split(",\\s?");
				for (String v : vals)
					tags.add(v);
			}
			long pageId = generatePageId(props.getProperty("href"));
			boolean noindex = Boolean.parseBoolean(props.getProperty("noindex"));
			Map<String, String> attributes = new HashMap<String, String>();
			Enumeration e = props.propertyNames();
			while (e.hasMoreElements()) {
				String key = (String) e.nextElement();
				String val = props.getProperty(key);
				attributes.put(key, val);
			}
			Page p = new Page(
					modified,
					pageId,
					props.getProperty("author"),
					props.getProperty("title"),
					props.getProperty("keywords"),
					props.getProperty("description"),
					tags,
					props.getProperty("href"),
					noindex,
					new DateTime(df.parse(props.getProperty("pubDate"))),
					body,
					attributes);
			return p;
		} finally {
			fis.close();
		}
	}

	private long generatePageId(String href) {
		try {
			MessageDigest sha1 = MessageDigest.getInstance("SHA1");
			sha1.update(href.getBytes());
			byte[] bytes = sha1.digest();
			BigInteger bi = new BigInteger(bytes);
			return bi.longValue();
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("damn you java");
		} finally {
		}
	}

	private String readFile(File path) throws IOException {
		FileInputStream fis = new FileInputStream(path);
		try {
			return IOUtils.toString(fis);
		} finally {
			fis.close();
		}
	}

	private class LazyPageIterable implements Iterable<Page>, Iterator<Page> {

		private Iterator<File> metaFilesIterator;

		public LazyPageIterable(Iterator<File> metaFilesIterator) {
			this.metaFilesIterator = metaFilesIterator;
		}

		@Override
		public Iterator<Page> iterator() {
			return this;
		}

		@Override
		public boolean hasNext() {
			return metaFilesIterator.hasNext();
		}

		@Override
		public Page next() {
			File f = metaFilesIterator.next();
			Page p;
			try {
				p = read(f);
			} catch (IOException e) {
				throw new RuntimeException(e);
			} catch (ParseException e) {
				throw new RuntimeException(e);
			}
			return p;
		}

		@Override
		public void remove() {
		}
	}
}
