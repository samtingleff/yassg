package com.tingleff.yassg;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.math3.ml.clustering.CentroidCluster;
import org.apache.commons.math3.ml.clustering.Clusterable;
import org.apache.commons.math3.ml.clustering.KMeansPlusPlusClusterer;
import org.apache.commons.math3.ml.clustering.MultiKMeansPlusPlusClusterer;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.tingleff.yassg.semantic.NamedEntity;
import com.tingleff.yassg.semantic.NamedEntityResponse;
import com.tingleff.yassg.semantic.SemanticDB;
import com.tingleff.yassg.semantic.SemanticDBImpl;

public class ClusterReferences {

	public static void main(String[] args) throws Exception {
		ClusterReferences m = new ClusterReferences();
		new JCommander(m).parse(args);
		m.run();
	}

	@Parameter(names = "-alchemyCache", required = false)
	private String alchemyCacheDir;

	@Parameter(names = "-clusters", required = false)
	private int clusters = 10;

	@Parameter(names = "-iterations", required = false)
	private int iterations = 10000;

	private SemanticDB db;

	public void run() throws Exception {
		db = new SemanticDBImpl(alchemyCacheDir);

		// build a map of features to IDs
		Map<String, Integer> featureMap = buildFeatureMap(db.iterator());

		// build a map of ids to feature vector
		Map<String, double[]> vectors = buildVectors(db.iterator(), featureMap);

		// convert to Collection<DoublePoint>
		Collection<NamedDoublePoint> coll = new ArrayList<NamedDoublePoint>(vectors.size());
		for (Map.Entry<String, double[]> e : vectors.entrySet()) {
			coll.add(new NamedDoublePoint(e.getKey(), e.getValue()));
		}

		// cluster them
		MultiKMeansPlusPlusClusterer<NamedDoublePoint> clusterer =
				new MultiKMeansPlusPlusClusterer<NamedDoublePoint>(new KMeansPlusPlusClusterer<NamedDoublePoint>(clusters, iterations), 10);
		List<CentroidCluster<NamedDoublePoint>> clusters = clusterer.cluster(coll);
		for (CentroidCluster<NamedDoublePoint> c : clusters) {
			System.out.println("Cluster:");
			List<NamedDoublePoint> items = c.getPoints();
			for (NamedDoublePoint ndp : items) {
				System.out.println("- " + ndp);
			}
		}
	}

	public Map<String, double[]> buildVectors(Iterator<NamedEntityResponse> iter, Map<String, Integer> featureMap) {
		Map<String, double[]> result = new HashMap<String, double[]>();
		while (iter.hasNext()) {
			NamedEntityResponse ner = iter.next();
			double[] features = featureVector(ner, featureMap);
			result.put(ner.getResult().getUrl(), features);
		}
		return result;
	}

	public double[] featureVector(NamedEntityResponse ner, Map<String, Integer> featureMap) {
		double[] result = new double[featureMap.size()];
		Iterator<NamedEntity> entities = ner.getResult().getEntities().iterator();
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
			Iterator<NamedEntity> entities = ner.getResult().getEntities().iterator();
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

	private static class NamedDoublePoint implements Clusterable {

		private String name;

		private double[] points;

		NamedDoublePoint(String name, double[] points) {
			this.name = name;
			this.points = points;
		}

		@Override
		public double[] getPoint() {
			return points;
		}

		@Override
		public String toString() {
			return name;
		}
	}
}
