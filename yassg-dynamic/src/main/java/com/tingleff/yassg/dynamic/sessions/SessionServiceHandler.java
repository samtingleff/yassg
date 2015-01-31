package com.tingleff.yassg.dynamic.sessions;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

import org.apache.thrift.TException;

import com.tingleff.yassg.search.types.TDevice;
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
	public String create(TDevice device) throws TSessionException,
			TException {
		if (validate(device))
			return device.getId();
		long idl = random.nextLong();
		String signature = sign(idl);
		String id = "1.0:" + Long.toHexString(idl) + ":" + signature;
		return id;
	}

	@Override
	public boolean validate(TDevice device) throws TSessionException,
			TException {
		String id = device.getId();
		if ((id == null) || (id.length() == 0))
			return false;
		try {
			String[] split = id.split(":");
			long idl = Long.parseLong(split[1], 16);
			String expected = sign(idl);
			if (!expected.equals(split[2]))
				return false;
			return true;
		} catch (Exception e) {
			return false;
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
