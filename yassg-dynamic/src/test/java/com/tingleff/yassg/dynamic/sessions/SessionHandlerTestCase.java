package com.tingleff.yassg.dynamic.sessions;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.tingleff.yassg.search.types.TDevice;
import com.tingleff.yassg.search.types.TDeviceId;
import com.tingleff.yassg.search.types.TSessionException;

@RunWith(JUnit4.class)
public class SessionHandlerTestCase {

	private SessionServiceHandler sessions = new SessionServiceHandler("foobar");

	@Test
	public void testcreate() throws Exception {
		TDeviceId id = sessions.create(new TDevice());
		Assert.assertNotNull(id);
		Assert.assertTrue(id.getVersion() == 1);
		Assert.assertTrue(id.getId() != 0l);
		Assert.assertNotNull(id.getSignature());
		Assert.assertTrue(id.getSignature().length() == 16);
	}

	@Test
	public void testValidateValidSession() throws Exception {
		TDeviceId id = sessions.create(new TDevice());
		TDevice td = new TDevice();
		td.setId(id);
		Assert.assertNotNull(sessions.validate(td));
	}

	@Test(expected=TSessionException.class)
	public void testInvalidVersion() throws Exception {
		TDeviceId id = sessions.create(new TDevice());
		id.setVersion((short) 2);
		TDevice td = new TDevice();
		td.setId(id);
		Assert.assertNotNull(sessions.validate(td));
	}

	@Test(expected=TSessionException.class)
	public void testValidateNullSession() throws Exception {
		sessions.validate(new TDevice());
	}

	@Test(expected=TSessionException.class)
	public void testValidateEmptySession() throws Exception {
		TDevice td = new TDevice();
		TDeviceId id = new TDeviceId();
		td.setId(id);
		sessions.validate(td);
	}

	@Test(expected=TSessionException.class)
	public void testGarbageSession() throws Exception {
		TDevice td = new TDevice();
		TDeviceId id = new TDeviceId();
		id.setId(10003913435l);
		id.setVersion((short) 1);
		id.setSignature("jfslkdjfsfd");
		td.setId(id);
		sessions.validate(td);
	}
}
