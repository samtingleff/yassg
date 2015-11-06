package com.tingleff.yassg.dynamic.likes;

import java.io.IOException;

import org.apache.thrift.TException;

import com.tingleff.yassg.dynamic.sessions.SessionServiceHandler;
import com.tingleff.yassg.search.types.TDevice;
import com.tingleff.yassg.search.types.TDeviceId;
import com.tingleff.yassg.search.types.TLikeException;
import com.tingleff.yassg.search.types.TLikeService;

public class LikeServiceHandler implements TLikeService.Iface {

	private SessionServiceHandler sessions;

	private LikeStorageBackend backend;

	public LikeServiceHandler(SessionServiceHandler sessions, LikeStorageBackend backend) {
		this.sessions = sessions;
		this.backend = backend;
	}

	@Override
	public void like(TDevice device, long page) throws TLikeException,
			TException {
		try {
			TDeviceId id = sessions.validate(device);
			backend.like(device, page);
		} catch (IOException e) {
			throw new TLikeException();
		}
		
	}

	@Override
	public int count(TDevice device, long page) throws TLikeException,
			TException {
		try {
			TDeviceId id = sessions.validate(device);
			int count = backend.count(page);
			return count;
		} catch (IOException e) {
			throw new TLikeException();
		}
	}

}
