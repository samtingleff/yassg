package com.tingleff.yassg.dynamic.sessions;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

import org.apache.thrift.TException;

import com.tingleff.yassg.search.types.TDevice;
import com.tingleff.yassg.search.types.TDeviceId;
import com.tingleff.yassg.search.types.TSessionException;
import com.tingleff.yassg.search.types.TSessionService;

public class SessionServiceHandler implements TSessionService.Iface {

	private static final char[] hex = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

	private Random random = new SecureRandom();

	private String secret;

	public SessionServiceHandler(String secret) {
		this.secret = secret;
	}

	@Override
	public TDeviceId create(TDevice device) throws TSessionException,
			TException {
		TDeviceId id = null;
		try {
			id = validate(device);
		} catch(TSessionException e) {
			long idl = random.nextLong();
			id = new TDeviceId();
			id.setVersion((short) 1);
			id.setId(idl);
			id.setSignature(sign(idl));
		}
		return id;
	}

	@Override
	public TDeviceId validate(TDevice device) throws TSessionException,
			TException {
		TDeviceId id = device.getId();
		if ((id == null) || (id.getVersion() != 1) || (id.getId() == 0) || (id.getSignature() == null))
			throw new TSessionException();
		try {
			String expected = sign(id.getId());
			if (!expected.equals(id.getSignature()))
				throw new TSessionException();
			return id;
		} catch (Exception e) {
			throw new TSessionException();
		}
	}

	private String sign(long id) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(Long.toHexString(id).getBytes());
			md.update(secret.getBytes());
			byte[] digest = md.digest();
			String hex = byteArray2Hex(digest);
			String leadingChars = hex.substring(0, 16);
			return leadingChars;
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("damn you java");
		}
	}

	private static String byteArray2Hex(byte[] bytes) {
	    StringBuilder sb = new StringBuilder(bytes.length * 2);
	    for (final byte b : bytes) {
	        sb.append(hex[(b & 0xF0) >> 4]);
	        sb.append(hex[b & 0x0F]);
	    }
	    return sb.toString();
	}
}
