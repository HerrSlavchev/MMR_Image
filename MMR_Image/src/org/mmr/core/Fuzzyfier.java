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

        for (int i = 0; i < feed.length; i++) {
            float value = feed[i];
            float[] valuesAndIndex = extractValuesForIndex(value, normalizationMinimum, normalizationMaximum, bins);
            updateHistogram(valuesAndIndex, res);
        }

        return res;
    }

    public static float[] extractValuesForIndex(float value, float normalizationMinimum, float normalizationMaximum, int bins) {

        float[] res = new float[]{
            0, //the index position of preceeding bin
            0, //the value to add to "preceeding" bin
            0 //the value to add to "following" bin -- use only if different from 0, otherwise ignore!
        };

        float binLength = (normalizationMaximum - normalizationMinimum) / bins;
        float relativeIdx = value / binLength;
        int resIdx = (int) relativeIdx;

        res[0] = resIdx + 0.0001f;
        if (value % bins == 0) {
            res[1] = 1;
        } else if (resIdx >= res.length - 1) {
            res[0] = res.length - 0.9999f;
            res[1] = 1;
        } else {
            float distToLowerBin = relativeIdx - resIdx;
            float distToUpperBin = 1 - distToLowerBin;
            res[1] = distToUpperBin;
            res[2] += distToLowerBin;
        }
        return res;
    }

    public static void updateHistogram(float[] valuesForIndex, float[] target) {
        int lowerIdx = (int) valuesForIndex[0];
        target[lowerIdx] += valuesForIndex[1];
        if (valuesForIndex[2] != 0) {
            target[lowerIdx + 1] += valuesForIndex[2];
        }
    }
}
