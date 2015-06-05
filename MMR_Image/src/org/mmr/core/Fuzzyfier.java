/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mmr.core;

/**
 *
 * @author root
 */
public class Fuzzyfier {

    public static float[] toHistogram(float[] feed, int normalizationMinimum, int normalizationMaximum, int bins) {

        float[] res = new float[bins];
        int binLength = (normalizationMaximum - normalizationMinimum) / bins;
        //res[0]: frequency of "0", res[1]: frequency of "binLength", ... res[i] = frequency of "i*binLength"

        for (int i = 0; i < feed.length; i++) {
            float value = feed[i];
            float relativeIdx = value / binLength;
            int resIdx = (int) relativeIdx;
            
            if (resIdx == res.length - 1 || value % bins == 0) {
                res[resIdx] += 1;
            } else {
                float distToLowerBin = relativeIdx - resIdx;
                res[resIdx] += distToLowerBin;
                float distToUpperBin = 1 - distToLowerBin;
                res[resIdx + 1] += distToUpperBin;
            }
        }

        return res;
    }
}
