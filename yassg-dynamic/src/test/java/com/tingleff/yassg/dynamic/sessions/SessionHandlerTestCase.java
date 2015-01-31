package com.tingleff.yassg.dynamic.sessions;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.tingleff.yassg.search.types.TDevice;

@RunWith(JUnit4.class)
public class SessionHandlerTestCase {

	private SessionServiceHandler sessions = new SessionServiceHandler("foobar");

	@Test
	public void testcreate() throws Exception {
		String id = sessions.create(new TDevice());
		Assert.assertNotNull(id);
		Assert.assertTrue(id.length() > 0);
	}

	@Test
	public void testValidateValidSession() throws Exception {
		String id = sessions.create(new TDevice());
		TDevice td = new TDevice();
		td.setId(id);
		Assert.assertTrue(sessions.validate(td));
	}

	@Test
	public void testValidateNullSession() throws Exception {
		Assert.assertFalse(sessions.validate(new TDevice()));
	}

	@Test
	public void testValidateEmptySession() throws Exception {
		TDevice td = new TDevice();
		td.setId("");
		Assert.assertFalse(sessions.validate(td));
	}

	@Test
	public void testGarbageSession() throws Exception {
		String id = "foobar";
		TDevice td = new TDevice();
		td.setId(id);
		Assert.assertFalse(sessions.validate(td));
	}
}
