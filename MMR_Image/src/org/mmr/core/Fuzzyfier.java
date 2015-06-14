/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mmr.core;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author root
 */
public class Fuzzyfier {

    private static int lastBins = 0; //how many bins were used for last caching
    private static final Map<Float, float[]> hsbCache = new HashMap();
           
    public static float[] extractValuesForIndexHSB(float value, float normalizationMinimum, float normalizationMaximum, int bins) {
        
        float[] res;
        //use caching in order to lookup known results instead of repeating all computations
        float[] cached = hsbCache.get(value);
        if(cached != null) {
            res = cached;
        } else {
            res = extractValuesForIndex(value, normalizationMinimum, normalizationMaximum, bins);
            hsbCache.put(value, res);
        }
        
        return res;
    }

    /**
     * @param value - a simple scalar value that will be separated between the two closest bins
     * @param normalizationMinimum - the minimal value of the expected data region
     * @param normalizationMaximum - the maximal value of the expected data region
     * @param bins - the number of bins in which the region must be separated
     * @return - array with three values: 
     *      v[0] = index position of the first bin, 
     *      v[1] = how much should be added to the first bin, 
     *      v[2] = how much should be added to the next bin (set to 0 if v[0] is index of the last possible bin)
     */
    private static float[] extractValuesForIndex(float value, float normalizationMinimum, float normalizationMaximum, int bins) {
                
        float[] res = new float[]{
            0, //the index position of preceeding bin
            0, //the value to add to preceeding bin
            0 //the value to add to following bin -- use only if different from 0, otherwise ignore!
        };

        /*e.g: we expect values from 0 to 255 and want to separate them into 32 bins -> check how large each bin is
         1) from 0 to 255 -> (255 - 0) = 255 possible discrete values
         2) 32 bins, each will cover 256 / 32 = 8 discrete values
         */
        float binLength = (normalizationMaximum - normalizationMinimum) / bins;
        /*e.g: we want to determine between which bins is the given value, e.g 57
        relativeIdx = 57 / 8 = 7.125
        resIdx = 7 -> the given value is between bins with index 7 and 8
        */
        float relativeIdx = value / binLength;
        int resIdx = (int) relativeIdx;

        /*
        store the preceeding index in the first field of the result
        */
        res[0] = resIdx;
        
        /*
        1) if the given value is precisely equal to a bin, e.g value = 64 
                -> equal to bins[8] -> attribute 1 to bins[8] and 0 to bins[9]
        2) if the given value is from the last bin, e.g value = 250 
                -> attribute 1 to bins[last] and set res[2] = 0 to mark that there is no next bin
        3) else: determine distance between value and surrounding bins in order to decide how much should be attributed to each bin
                (hint: use distance between the relative and discrete index to obtain it directly)
        */
        if (value % bins == 0) {
            res[1] = 1;
        } else if (resIdx >= bins - 1) {
            res[0] = bins - 1;
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
    
    /**
     *
     * @param bins - the number of bins to use for the new caching. If it is different from the last used, maps will be erased
     */
    public static void refresh(int bins){
        if (lastBins != bins) {
            hsbCache.clear();
            lastBins = bins;
        }
    }
}
