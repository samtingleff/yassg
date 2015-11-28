package com.tingleff.yassg;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.math3.ml.clustering.CentroidCluster;
import org.apache.commons.math3.ml.clustering.DoublePoint;
import org.apache.commons.math3.ml.clustering.KMeansPlusPlusClusterer;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.tingleff.yassg.semantic.NamedEntity;
import com.tingleff.yassg.semantic.NamedEntityResponse;
import com.tingleff.yassg.semantic.SemanticDB;
import com.tingleff.yassg.semantic.SemanticDBImpl;

public class CrawlEntities {

	public static void main(String[] args) throws Exception {
		CrawlEntities m = new CrawlEntities();
		new JCommander(m).parse(args);
		m.run();
	}

	@Parameter(names = "-alchemyCache", required = false)
	private String alchemyCacheDir;

	private SemanticDB db;

	public void run() throws Exception {
		db = new SemanticDBImpl(alchemyCacheDir);

		// build a map of features to IDs
		Map<String, Integer> featureMap = buildFeatureMap(db.iterator());

		// build a map of ids to feature vector
		Map<String, double[]> vectors = buildVectors(db.iterator(), featureMap);

		// convert to Collection<DoublePoint>
		Collection<DoublePoint> coll = new ArrayList<DoublePoint>(vectors.size());
		for (Map.Entry<String, double[]> e : vectors.entrySet()) {
			coll.add(new DoublePoint(e.getValue()));
		}

		// cluster them
		KMeansPlusPlusClusterer<DoublePoint> clusterer = new KMeansPlusPlusClusterer<DoublePoint>(10, 10000);
		List<CentroidCluster<DoublePoint>> clusters = clusterer.cluster(coll);
		for (CentroidCluster<DoublePoint> c : clusters) {
			System.out.println(c);
		}
	}

	public Map<String, double[]> buildVectors(Iterator<NamedEntityResponse> iter, Map<String, Integer> featureMap) {
		Map<String, double[]> result = new HashMap<String, double[]>();
		while (iter.hasNext()) {
			NamedEntityResponse ner = iter.next();
			double[] features = featureVector(ner, featureMap);
			result.put(ner.getUrl(), features);
		}
		return result;
	}

	public double[] featureVector(NamedEntityResponse ner, Map<String, Integer> featureMap) {
		double[] result = new double[featureMap.size()];
		Iterator<NamedEntity> entities = ner.iterator();
		while (entities.hasNext()) {
			NamedEntity ne = entities.next();
			Integer index = featureMap.get(featureKey(ne));
			result[index.intValue()] = ne.getScore();
		}
		return result;
	}

	private Map<String, Integer> buildFeatureMap(Iterator<NamedEntityResponse> iter) {
		int id = 0;
		Map<String, Integer> m = new HashMap<String, Integer>();
		while (iter.hasNext()) {
			NamedEntityResponse ner = iter.next();
			Iterator<NamedEntity> entities = ner.iterator();
			while (entities.hasNext()) {
				NamedEntity ne = entities.next();
				String key = featureKey(ne);
				Integer i = m.get(key);
				if (i == null) {
					m.put(key, new Integer(id));
					++id;
				}
			}
		}
		return m;
	}

	private String featureKey(NamedEntity ne) {
		return String.format("%1$s:%2$s", ne.getType(), ne.getText());
	}
}
