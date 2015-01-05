package com.tingleff.yassg.hash;

public interface HashFunction {

	public byte[] hash(byte[] bytes);

	public String hex(String input);
}
