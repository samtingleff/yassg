package com.tingleff.yassg.rsync;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

public class RsyncCommand implements Rsync {

	@Override
	public int rsync(File source, File dest, boolean verbose, boolean delete)
			throws Exception {
		// TODO Auto-generated method stub
		dest.mkdirs();

		String command = "rsync -a";
		if (verbose)
			command = command + "v";
		if (delete)
			command = command + " --delete";
		command = command
				+ String.format(" %1$s/ %2$s/",
						source.getAbsolutePath(),
						dest.getAbsolutePath());
		if (verbose)
			System.out.println(command);
		StringBuffer output = new StringBuffer();

		Process p;
		p = Runtime.getRuntime().exec(command);
		p.waitFor();
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				p.getInputStream()));

		String line = "";
		while ((line = reader.readLine()) != null) {
			System.out.println(line);
			output.append(line + "\n");
		}
		return p.exitValue();
	}

}
