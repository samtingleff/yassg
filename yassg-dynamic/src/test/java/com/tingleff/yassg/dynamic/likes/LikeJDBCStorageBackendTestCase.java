package com.tingleff.yassg.dynamic.likes;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.tingleff.yassg.dynamic.sessions.SessionServiceHandler;
import com.tingleff.yassg.search.types.TDevice;

@RunWith(JUnit4.class)
public class LikeJDBCStorageBackendTestCase {

	private LikeJDBCStorageBackend backend = new LikeJDBCStorageBackend();

	private SessionServiceHandler sessions = new SessionServiceHandler("foobar");

	@Before
	public void setUp() throws Exception {
		backend.setDriverClass("org.h2.Driver");
		backend.setUrl("jdbc:h2:mem:likes;DB_CLOSE_DELAY=-1");
		backend.init();
		backend.createTables();
	}

	@Test
	public void testZeroLikes() throws Exception {
		long page = 39250234351l;
		Assert.assertEquals(0, backend.count(page));
	}

	@Test
	public void testOneLike() throws Exception {
		long page = 39250234352l;
		Assert.assertEquals(0, backend.count(page));
		TDevice device = new TDevice();
		device.setIp("67.168.163.247");
		device.setId(sessions.create(device));
		backend.like(device, page);
		int count = backend.count(page);
		Assert.assertEquals(1, count);
		Assert.assertTrue(backend.liked(device, page));
	}

	@Test
	public void testDuplicates() throws Exception {
		long page = 39250234353l;
		Assert.assertEquals(0, backend.count(page));
		TDevice device = new TDevice();
		device.setIp("67.168.163.247");
		device.setId(sessions.create(device));
		backend.like(device, page);
		backend.like(device, page);
		int count = backend.count(page);
		Assert.assertEquals(1, count);
	}

	@Test
	public void testMultiple() throws Exception {
		long page = 39250234354l;
		Assert.assertEquals(0, backend.count(page));
		TDevice device1 = new TDevice();
		device1.setIp("67.168.163.247");
		device1.setId(sessions.create(device1));
		backend.like(device1, page);
		TDevice device2 = new TDevice();
		device2.setIp("67.168.163.247");
		device2.setId(sessions.create(device2));
		backend.like(device2, page);
		int count = backend.count(page);
		Assert.assertEquals(2, count);
	}
}
