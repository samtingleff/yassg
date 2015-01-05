package com.tingleff.yassg.hash;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Hex;

public abstract class BaseHashFunction implements HashFunction {

	private final String alg;

	public BaseHashFunction(String alg) {
		this.alg = alg;
	}

	@Override
	public String hex(String input) {
		byte[] bytes = hash(input.getBytes());
		return Hex.encodeHexString(bytes);
	}

	@Override
	public byte[] hash(byte[] bytes) {
		try {
			MessageDigest md = MessageDigest.getInstance(alg);
			md.update(bytes);
			return md.digest();
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("stupid java");
		}
	}
}
