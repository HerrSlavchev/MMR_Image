package org.mmr.core;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public final class Engine {

	private Engine() {
	}

	public static final void createIndex() throws IOException {
		if (!Context.getIndexDirectory().isPresent()) {
			throw new RuntimeException("Before index creation: No index directory in context!");
		}

		if (!Context.getDataDirectory().isPresent()) {
			throw new RuntimeException("Before index creation: No data directory in context!");
		}

		if (Context.getHistogramBinCount() <= 0) {
			throw new RuntimeException("Before index creation: Histogram bins count is <= 0 in context!");
		}

		if (Context.getAllowedContentTypes().isEmpty()) {
			throw new RuntimeException("Before index creation: No allowed content types in context!");
		}

		Context.reset();
		Files.walkFileTree(Context.getDataDirectory().get(), new DirectoryVisitor());

		PersistentIndex.save();
	}

	public static final List<Similarity> search() {
		if (!Context.getQueryDocument().isPresent()) {
			throw new RuntimeException("Before search: No query document in context!");
		}

		List<Similarity> res = Context.getAllDocuments().stream()
				.map((document) -> new Similarity(document, evalSimilarity(document)))
				.collect(Collectors.toList());

		Collections.sort(res, (Similarity s1, Similarity s2) -> {
			float diff = s1.getValue() - s2.getValue();
			if (diff > 0) {
				return -1;
			} else {
				return 1;
			}
		});

		return res;
	}

	//chi-square measure
	private static float evalSimilarity(Document doc) {
		float res = 1;
		Document query = Context.getQueryDocument().get();
		float currDist = 0;

		float[] dist = new float[]{0, 0, 0}; //keep distances in H, S and B
		float[] histQ; //pointer to H, S, or B histogram of query document
		float[] histD; //same for doc
                /*use to scale hist values according to image dimensions - e.g:
                1) Image A has value histA[2] = 15 and dimensions [10x10]
                2) Image B is exactly the same as image A and dimensions [20x20] -> thus histB[2] = 60
                3) We scale the hues: histA'[2] = 15 / (10*10) = 0.15, histB'[2] = 60 / (20 * 20) = 0.15
                4) We compare histA' and histB'
                */
                float scaleFactorQ = query.getHeight() * query.getWidth();
                float scaleFactorD = doc.getHeight() * doc.getWidth();
                float scaledQ, scaledD;
		for (int i = 0; i < dist.length; i++) {
                    histQ = query.getHistrogramHSB()[i];
                    histD = doc.getHistrogramHSB()[i];
                        
                    for (int j = 0; j < histQ.length; j++) {
                        scaledQ = histQ[j] * scaleFactorQ;
                        scaledD = histD[j] * scaleFactorD;
                        if (scaledQ + scaledD != 0) {
                            dist[i] += (2 * (scaledQ - scaledD) * (scaledQ - scaledD)) / (scaledQ + scaledD);
                        }
                    }
		}

		//apply weighing factors
		float[] weights = new float[]{Context.getHueWeight(), Context.getSaturationWeight(), Context.getBrightnessWeight()};
		float norm = 0; //use to normalise weighs -> we want {1.0, 1.0, 1.0} to produce the same similarity as {0.6, 0.6, 0.6}
		for (int i = 0; i < dist.length; i++) {
			currDist += weights[i] * dist[i];
			norm += weights[i];
		}
		if (norm != 0) {
			currDist /= norm;
		}

		res = 1 / (1 + currDist);

		return res;
	}
}
