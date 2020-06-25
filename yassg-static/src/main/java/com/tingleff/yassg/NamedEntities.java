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

public class NamedEntities {

	public static void main(String[] args) throws Exception {
		NamedEntities m = new NamedEntities();
		new JCommander(m).parse(args);
		m.run();
	}

	@Parameter(names = "-alchemyCache", required = true)
	private String alchemyCacheDir;

	@Parameter(names = "-url", required = true)
	private String url;

	private SemanticDB db;

	public void run() throws Exception {
		db = new SemanticDBImpl(alchemyCacheDir);

		NamedEntityResponse ner = db.lookup(url);
		System.err.println(ner);
		if (ner != null && (ner.isSuccess())) {
			System.err.println(ner.getResult());
			System.err.println(ner.getResult().getEntities().size());
			for (NamedEntity ne : ner.getResult().getEntities()) {
				System.err.println(ne);
			}
		}
	}
}
