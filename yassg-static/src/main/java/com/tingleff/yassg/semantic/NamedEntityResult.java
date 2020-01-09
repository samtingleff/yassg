package com.tingleff.yassg.semantic;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

public class NamedEntityResult implements Serializable {
	private static final long serialVersionUID = 7923155449270469660L;

	private String source;

	private String url;

	private long timestamp;

	private List<NamedEntity> entities = Collections.EMPTY_LIST;

	public NamedEntityResult(String source, String url, long timestamp, List<NamedEntity> entities) {
		this.source = source;
		this.url = url;
		this.timestamp = timestamp;
		this.entities = entities;
	}

	public NamedEntityResult() { }

	public String getSource() {
		return source;
	}

	public String getUrl() {
		return url;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public List<NamedEntity> getEntities() {
		return entities;
	}
}
