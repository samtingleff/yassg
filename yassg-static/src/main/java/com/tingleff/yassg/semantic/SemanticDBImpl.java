package com.tingleff.yassg.semantic;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.tingleff.yassg.hash.HashFunction;
import com.tingleff.yassg.hash.SHA256HashFunction;

public class SemanticDBImpl implements SemanticDB {
	private File root;

	private HashFunction hash = new SHA256HashFunction();

	public SemanticDBImpl(String dir) {
		this.root = new File(dir);
		this.root.mkdirs();
	}

	@Override
	public NamedEntityResponse lookup(String url) throws IOException {
		NamedEntityResponse result = null;
		File path = getPath(url);
		if (path.exists()) {
			result = read(path);
		}
		return result;
	}

	@Override
	public void save(String url, NamedEntityResponse response)
			throws IOException {
		File output = getPath(url);
		File parent = output.getParentFile();
		if (!parent.exists())
			parent.mkdirs();
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(output);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(response);
		} finally {
			if (fos != null)
				fos.close();
		}
	}

	@Override
	public Iterator<NamedEntityResponse> iterator() throws IOException {
		List<Path> files = new LinkedList<Path>();
		FileCollector fc = new FileCollector(files);
		Files.walkFileTree(this.root.toPath(), fc);
		return new ConvertingIterator(fc.results.iterator());
	}

	private NamedEntityResponse read(File path) throws IOException {
		NamedEntityResponse result = null;
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(path);
			ObjectInputStream ois = new ObjectInputStream(fis);
			result = (NamedEntityResponse) ois.readObject();
			return result;
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		} finally {
			if (fis != null)
				fis.close();
		}
	}

	private File getPath(String url) {
		String hashResult = hash.hex(url);
		String path = hashResult.substring(0, 2) + "/"
				+ hashResult.substring(2, 4) + "/" + hashResult.substring(4, 6)
				+ "/" + hashResult + ".ser";
		return new File(this.root, path);
	}

	private class ConvertingIterator implements
			Iterator<NamedEntityResponse> {

		private Iterator<Path> source;

		public ConvertingIterator(Iterator<Path> source) {
			this.source = source;
		}

		@Override
		public boolean hasNext() {
			return source.hasNext();
		}

		@Override
		public NamedEntityResponse next() {
			Path p = source.next();
			try {
				NamedEntityResponse ner = read(p.toFile());
				return ner;
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		@Override
		public void remove() {
		}
	}

	private static class FileCollector implements FileVisitor<Path> {
		private Collection<Path> results;

		public FileCollector(Collection<Path> results) {
			this.results = results;
		}

		@Override
		public FileVisitResult preVisitDirectory(Path dir,
				BasicFileAttributes attrs) throws IOException {
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
				throws IOException {
			results.add(file);
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult visitFileFailed(Path file, IOException exc)
				throws IOException {
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult postVisitDirectory(Path dir, IOException exc)
				throws IOException {
			return FileVisitResult.CONTINUE;
		}
	}
}