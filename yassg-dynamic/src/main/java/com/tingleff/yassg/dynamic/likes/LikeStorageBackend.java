package com.tingleff.yassg.dynamic.likes;

import java.io.IOException;

import com.tingleff.yassg.search.types.TDevice;

public interface LikeStorageBackend {

	public void like(TDevice device, long page) throws IOException;

	public int count(long page) throws IOException;

	public boolean liked(TDevice device, long page) throws IOException;
}
