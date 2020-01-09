package com.tingleff.yassg.semantic;

import java.io.Serializable;

public class NamedEntityResponse implements Serializable {
	private static final long serialVersionUID = -2647036148541069071L;

	private boolean success = true;

	private NamedEntityResult result;

	public NamedEntityResponse(boolean success, NamedEntityResult result) {
		this.success = success;
		this.result = result;
	}

	public NamedEntityResponse() { }

	public boolean isSuccess() {
		return success;
	}

	public NamedEntityResult getResult() {
		return result;
	}
}
