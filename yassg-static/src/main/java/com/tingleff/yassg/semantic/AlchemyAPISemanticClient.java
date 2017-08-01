package com.tingleff.yassg.semantic;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import com.ibm.watson.developer_cloud.natural_language_understanding.v1.NaturalLanguageUnderstanding;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.AnalysisResults;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.AnalyzeOptions;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.CategoriesOptions;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.ConceptsOptions;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.EntitiesOptions;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.EntitiesResult;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.Features;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.KeywordsOptions;

public class AlchemyAPISemanticClient implements SemanticClient {

	private String username;

	private String password;

	private String cacheDir;

	private SemanticDB cache;

	private NaturalLanguageUnderstanding service;

	private Features features;

	public AlchemyAPISemanticClient(String username, String password, String cacheDir) {
		this.username = username;
		this.password = password;
		this.cacheDir = cacheDir;
		this.features = buildFeatures();
	}

	public AlchemyAPISemanticClient() {
	}

	public AlchemyAPISemanticClient init() {
		this.cache = new SemanticDBImpl(this.cacheDir);
		this.service = new NaturalLanguageUnderstanding(
				  NaturalLanguageUnderstanding.VERSION_DATE_2017_02_27,
				  username,
				  password);
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

		AnalyzeOptions parameters = new AnalyzeOptions.Builder()
				.url(url)
				.features(this.features)
				.build();
		try {
			AnalysisResults response = this.service
				  .analyze(parameters)
				  .execute();

			result = new NamedEntityResponse(url);

			List<EntitiesResult> entities = response.getEntities();
			for (EntitiesResult entity : entities) {
				NamedEntity ne = new NamedEntity(
						entity.getCount(),
						entity.getType(),
						entity.getText(),
						entity.getRelevance().doubleValue(),
						Collections.EMPTY_LIST);
				result.add(ne);
			}
			cache.save(url, result);
		} catch (Exception e) {
			NamedEntityResponse failureResponse = new NamedEntityResponse(
					url, false, System.currentTimeMillis());
			cache.save(url, failureResponse);
		}
	    return result;
	}

	private Features buildFeatures() {
		CategoriesOptions categories = new CategoriesOptions();
		EntitiesOptions entitiesOptions = new EntitiesOptions.Builder()
				.emotion(true)
				.sentiment(true)
				.limit(16)
				.build();
		KeywordsOptions keywordsOptions = new KeywordsOptions.Builder()
				.emotion(true)
				.sentiment(true)
				.limit(16)
				.build();
		ConceptsOptions concepts= new ConceptsOptions.Builder()
				.limit(16)
				.build();
		Features features = new Features.Builder()
				.categories(categories)
				.concepts(concepts)
				.entities(entitiesOptions)
				.keywords(keywordsOptions)
				.build();
		return features;
	}
}
