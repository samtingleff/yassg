package com.tingleff.yassg.dynamic.sessions;

import org.apache.thrift.TException;

import com.tingleff.yassg.search.types.TDevice;
import com.tingleff.yassg.search.types.TSessionException;
import com.tingleff.yassg.search.types.TSessionService;

public class SessionServiceHandler implements TSessionService.Iface {

	public SessionServiceHandler() {
	}

	@Override
	public long createSession(TDevice device) throws TSessionException,
			TException {
		return 0l;
	}
}
