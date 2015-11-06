package com.tingleff.yassg.dynamic.likes;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runners.JUnit4;

import org.junit.runner.RunWith;

import com.tingleff.yassg.dynamic.sessions.SessionServiceHandler;
import com.tingleff.yassg.search.types.TDevice;
import com.tingleff.yassg.search.types.TSessionException;

@RunWith(JUnit4.class)
public class LikeServiceHandlerTestCase {

	private SessionServiceHandler sessions = new SessionServiceHandler("foobar");

	private LikeJDBCStorageBackend backend = new LikeJDBCStorageBackend();

	private LikeServiceHandler likes = null;

	@Before
	public void setUp() throws Exception {
		backend.setDriverClass("org.h2.Driver");
		backend.setUrl("jdbc:h2:mem:likes;DB_CLOSE_DELAY=-1");
		backend.init();
		backend.createTables();
		likes = new LikeServiceHandler(sessions, backend);
	}

	@Test
	public void testInvalidId() throws Exception {
		long pageid = 193419344l;
		TDevice device = new TDevice();
		device.setIp("127.0.0.1");
		device.setUa("mozilla bro");
		try {
			likes.like(device, pageid);
		} catch(TSessionException e) { }
		device.setId(sessions.create(device));
		Assert.assertEquals(0, likes.count(device, pageid));
	}

	@Test
	public void testValidId() throws Exception {
		long pageid = 193419345l;
		TDevice device = new TDevice();
		device.setIp("127.0.0.1");
		device.setUa("mozilla bro");
		device.setId(sessions.create(device));
		likes.like(device, pageid);
		Assert.assertEquals(1, likes.count(device, pageid));
	}

	@Test
	public void testDuplicates() throws Exception {
		long pageid = 193419346l;
		TDevice device = new TDevice();
		device.setIp("127.0.0.1");
		device.setUa("mozilla bro");
		device.setId(sessions.create(device));
		likes.like(device, pageid);
		likes.like(device, pageid);
		Assert.assertEquals(1, likes.count(device, pageid));
	}

	@Test
	public void testMultiple() throws Exception {
		long pageid = 193419347l;
		TDevice device1 = new TDevice();
		device1.setIp("127.0.0.1");
		device1.setUa("mozilla bro");
		device1.setId(sessions.create(device1));
		likes.like(device1, pageid);
		TDevice device2 = new TDevice();
		device2.setIp("127.0.0.1");
		device2.setUa("mozilla bro");
		device2.setId(sessions.create(device2));
		likes.like(device2, pageid);
		Assert.assertEquals(2, likes.count(device1, pageid));
		Assert.assertEquals(2, likes.count(device2, pageid));
	}
}
