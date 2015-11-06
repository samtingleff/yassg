package com.tingleff.yassg.dynamic.likes;

import org.apache.thrift.TException;

import com.tingleff.yassg.dynamic.sessions.SessionServiceHandler;
import com.tingleff.yassg.search.types.TDevice;
import com.tingleff.yassg.search.types.TDeviceId;
import com.tingleff.yassg.search.types.TLikeException;
import com.tingleff.yassg.search.types.TLikeService;
import com.tingleff.yassg.search.types.TSessionException;

public class LikeServiceHandler implements TLikeService.Iface {

	private SessionServiceHandler sessions;

	public LikeServiceHandler(SessionServiceHandler sessions) {
		this.sessions = sessions;
	}

	@Override
	public void like(TDevice device, long page) throws TLikeException,
			TException {
		// validate the id
		TDeviceId id = null;
		try {
			id = sessions.validate(device);
		} catch(TSessionException e) {
			id = sessions.create(device);
		}
		
	}

	@Override
	public long count(TDevice device, long page) throws TLikeException,
			TException {
		return 0;
	}

}
