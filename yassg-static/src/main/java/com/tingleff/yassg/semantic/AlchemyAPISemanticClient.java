package com.tingleff.yassg.semantic;

import java.io.IOException;
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

public class AlchemyAPISemanticClient implements SemanticClient {

	private String apiKey;

	private String cacheDir;

	private SemanticDB cache;

	private Client client;

	private NamedEntityParams namedEntityParams;

	public AlchemyAPISemanticClient(String apiKey, String cacheDir) {
		this.apiKey = apiKey;
		this.cacheDir = cacheDir;
	}

	public AlchemyAPISemanticClient() {
	}

	public AlchemyAPISemanticClient init() {
		this.cache = new SemanticDBImpl(this.cacheDir);
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
		if (result != null) {
			if (!result.isSuccess()) {
				long now = System.currentTimeMillis();
				if ((now - result.getTimestamp()) < (1000l * 60l * 60l * 24l * 7l)) {
					// do not try again for 7 days
					return result;
				}
			} else {
				return result;
			}
		}

		AbstractCall<NamedEntityAlchemyEntity> rankedNamedEntitiesCall = new RankedNamedEntitiesCall(new CallTypeUrl(url), namedEntityParams);
		try {
			Response<NamedEntityAlchemyEntity> rankedNamedEntitiesResponse = client
					.call(rankedNamedEntitiesCall);
			Iterator<NamedEntityAlchemyEntity> iter = rankedNamedEntitiesResponse
					.iterator();
			result = new NamedEntityResponse(url);
			while (iter.hasNext()) {
				NamedEntityAlchemyEntity entity = iter.next();
				List<String> subtypes = new ArrayList<String>(
						entity.getSubtypeSize());
				Iterator<String> subtypeIterator = entity.subtypeIterator();
				while (subtypeIterator.hasNext()) {
					String s = subtypeIterator.next();
					subtypes.add(s);
				}
				NamedEntity ne = new NamedEntity(entity.getCount(),
						entity.getType(), entity.getText(), entity.getScore()
								.doubleValue(), subtypes);
				result.add(ne);
			}
			cache.save(url, result);
		} catch (IOException e) {
			NamedEntityResponse failureResponse = new NamedEntityResponse(
					url, false, System.currentTimeMillis());
			cache.save(url, failureResponse);
		}
	    return result;
	}
}
