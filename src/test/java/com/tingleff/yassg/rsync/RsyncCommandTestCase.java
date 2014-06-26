package com.tingleff.yassg.rsync;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class RsyncCommandTestCase {

	private Rsync rsync = new RsyncCommand();

	@Test
	public void rsync() throws Exception {
		String src = "src/test/resources/rsync", dest = "target/test/rsync";
		File f = new File(dest, "foo.txt");
		f.delete();
		int retval = rsync.rsync(new File(src), new File(dest), false, false);
		Assert.assertEquals(0, retval);
		Assert.assertTrue(f.exists());
	}
}
