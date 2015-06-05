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

    public static float[] toHistogram(float[] feed, float normalizationMinimum, float normalizationMaximum, int bins) {

        float[] res = new float[bins];
        for (int i = 0; i < res.length; i++) {
            res[i] = 0;
        }
        
        float binLength = (normalizationMaximum - normalizationMinimum) / bins;
        //res[0]: frequency of "0", res[1]: frequency of "binLength", ... res[i] = frequency of "i*binLength"

        for (int i = 0; i < feed.length; i++) {
            float value = feed[i];
            float relativeIdx = value / binLength;
            int resIdx = (int) relativeIdx;

            if ( value % bins == 0) {
                res[resIdx] += 1;
            } if (resIdx >= res.length - 1) {
                res[res.length - 1] += 1;
            } else {
                float distToLowerBin = relativeIdx - resIdx;
                float distToUpperBin = 1 - distToLowerBin;
                res[resIdx] += distToUpperBin;
                res[resIdx + 1] += distToLowerBin;
            }
        }

        return res;
    }
}
