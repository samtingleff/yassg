package com.tingleff.yassg.rsync;

import java.io.File;

public interface Rsync {

	public int rsync(File source, File dest, boolean verbose, boolean delete) throws Exception;
}
