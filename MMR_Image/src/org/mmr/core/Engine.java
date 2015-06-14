package org.mmr.core;

import java.io.IOException;
import java.nio.file.Files;
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
    }

    public static final List<Similarity> search() {
        if (!Context.getQueryDocument().isPresent()) {
            throw new RuntimeException("Before search: No query document in context!");
        }

        return Context.getAllDocuments().stream().map((document) -> new Similarity(document, evalSimilarity(document))).collect(Collectors.toList());
    }

    //chi-square measure
    private static float evalSimilarity(Document doc) {
        float res = 1;
        Document query = Context.getQueryDocument().get();
        float currDist = 0;
        
        float[] dist = new float[]{0,0,0}; //
        float[] histQ; //pointer to H, S, or B histogram of query document
        float[] histD; //same for doc
        
        for (int i = 0; i < dist.length; i++) {
            histQ = query.getHistrogramHSB()[i];
            histD = doc.getHistrogramHSB()[i];
            for (int j = 0; j < histQ.length; j++) {
                if(histQ[j] + histD[j] != 0){
                    dist[i] += (2 * (histQ[j] - histD[j]) * (histQ[j] - histD[j])) / (histQ[j] + histD[j]);
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
