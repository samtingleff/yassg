package com.tingleff.yassg.semantic;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.likethecolor.alchemy.api.Client;
import com.likethecolor.alchemy.api.call.AbstractCall;
import com.likethecolor.alchemy.api.call.RankedNamedEntitiesCall;
import com.likethecolor.alchemy.api.call.type.CallTypeUrl;
import com.likethecolor.alchemy.api.entity.NamedEntityAlchemyEntity;
import com.likethecolor.alchemy.api.entity.Response;
import com.likethecolor.alchemy.api.params.NamedEntityParams;
import com.tingleff.yassg.hash.HashFunction;
import com.tingleff.yassg.hash.SHA256HashFunction;

public class AlchemyAPISemanticClient implements SemanticClient {

	private String apiKey;

	private String cacheDir;

	private EntityResponseCache cache;

	private Client client;

	private NamedEntityParams namedEntityParams;

	public AlchemyAPISemanticClient(String apiKey, String cacheDir) {
		this.apiKey = apiKey;
		this.cacheDir = cacheDir;
	}

	public AlchemyAPISemanticClient() {
	}

	public AlchemyAPISemanticClient init() {
		this.cache = new EntityResponseCache(this.cacheDir);
		this.client = new Client(this.apiKey);
		namedEntityParams = new NamedEntityParams();
	    namedEntityParams.setIsCoreference(true);
	    namedEntityParams.setIsDisambiguate(true);
	    namedEntityParams.setIsLinkedData(true);
	    namedEntityParams.setIsQuotations(true);
	    namedEntityParams.setIsSentiment(true);
	    namedEntityParams.setIsShowSourceText(true);
	    return this;
	}

	public NamedEntityResponse namedEntities(String url) throws IOException {
		NamedEntityResponse result = cache.lookup(url);
		if (result != null)
			return result;

		AbstractCall<NamedEntityAlchemyEntity> rankedNamedEntitiesCall = new RankedNamedEntitiesCall(new CallTypeUrl(url), namedEntityParams);
	    Response<NamedEntityAlchemyEntity> rankedNamedEntitiesResponse = client.call(rankedNamedEntitiesCall);
	    Iterator<NamedEntityAlchemyEntity> iter = rankedNamedEntitiesResponse.iterator();
	    result = new NamedEntityResponse();
		while (iter.hasNext()) {
			NamedEntityAlchemyEntity entity = iter.next();
			List<String> subtypes = new ArrayList<String>(entity.getSubtypeSize());
			Iterator<String> subtypeIterator = entity.subtypeIterator();
			while (subtypeIterator.hasNext()) {
				String s = subtypeIterator.next();
				subtypes.add(s);
			}
			NamedEntity ne = new NamedEntity(entity.getCount(),
					entity.getType(),
					entity.getText(),
					entity.getScore().doubleValue(),
					subtypes);
			result.add(ne);
		}
	    cache.save(url, result);
	    return result;
	}

	private static class EntityResponseCache {
		private File root;

		private HashFunction hash = new SHA256HashFunction();

		EntityResponseCache(String dir) {
			this.root = new File(dir);
			this.root.mkdirs();
		}

		public NamedEntityResponse lookup(String url) throws IOException {
			NamedEntityResponse result = null;
			File path = getPath(url);
			if (path.exists()) {
				FileInputStream fis = null;
				try {
					fis = new FileInputStream(path);
					ObjectInputStream ois = new ObjectInputStream(fis);
					result = (NamedEntityResponse) ois.readObject();
				} catch (ClassNotFoundException e) {
					throw new RuntimeException(e);
				} finally {
					if (fis != null)
						fis.close();
				}

			}
			return result;
		}

		public void save(String url, NamedEntityResponse response) throws IOException {
			File output = getPath(url);
			File parent = output.getParentFile();
			if (!parent.exists())
				parent.mkdirs();
			FileOutputStream fos = null;
			try {
				fos = new FileOutputStream(output);
				ObjectOutputStream oos = new ObjectOutputStream(fos);
				oos.writeObject(response);
			} finally {
				if (fos != null)
					fos.close();
			}
		}

		private File getPath(String url) {
			String hashResult = hash.hex(url);
			String path = hashResult.substring(0, 2)
					+ "/" + hashResult.substring(2, 4)
					+ "/" + hashResult.substring(4, 6)
					+ "/" + hashResult + ".ser";
			return new File(this.root, path);
		}
	}
}
