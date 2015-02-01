package com.tingleff.yassg.dynamic.likes;

import org.apache.thrift.TException;

import com.tingleff.yassg.dynamic.sessions.SessionServiceHandler;
import com.tingleff.yassg.search.types.TDevice;
import com.tingleff.yassg.search.types.TLikeException;
import com.tingleff.yassg.search.types.TLikeService;

public class LikeServiceHandler implements TLikeService.Iface {

	private SessionServiceHandler sessions;

	public LikeServiceHandler(SessionServiceHandler sessions) {
		this.sessions = sessions;
	}

	@Override
	public void like(TDevice device, long page) throws TLikeException,
			TException {
		// validate the id
		boolean isValidId = sessions.validate(device);
		if (!isValidId)
			return;
		
	}

	@Override
	public long count(TDevice device, long page) throws TLikeException,
			TException {
		return 0;
	}

}
