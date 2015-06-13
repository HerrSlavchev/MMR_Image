package org.mmr.core;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Used for state and configuration sharing among components.
 */
public final class Context {

    private static Path indexDirectory;

    private static Path dataDirectory;

    private static int histogramBinCount;

    private static final Set<EContentType> allowedContentTypes = new HashSet<>();

    private static Document queryDocument;

    private static float hueWeight;

    private static float saturationWeight;

    private static float brightnessWeight;

    private static final List<Document> allDocuments = new ArrayList<>();
    
    private static final List<Document> similarDocuments = new ArrayList<>();

    static {
        // Default values are initialized.
        histogramBinCount = 64;

        allowedContentTypes.add(EContentType.BMP);
        allowedContentTypes.add(EContentType.JPEG);
        allowedContentTypes.add(EContentType.PNG);

        hueWeight = 0.3f;
        saturationWeight = 0.3f;
        brightnessWeight = 0.3f;
    }

    public static final Optional<Path> getIndexDirectory() {
        return Optional.ofNullable(indexDirectory);
    }

    public static final void setIndexDirectory(final Path directory) {
        indexDirectory = directory;
    }

    public static final Optional<Path> getDataDirectory() {
        return Optional.ofNullable(dataDirectory);
    }

    public static final void setDataDirectory(final Path directory) {
        dataDirectory = directory;
    }

    public static final int getHistogramBinCount() {
        return histogramBinCount;
    }

    public static final void setHistogramBinCount(final int count) {
        histogramBinCount = count;
    }

    public static final Set<EContentType> getAllowedContentTypes() {
        return allowedContentTypes;
    }

    public static final void setAllowedContentTypes(final Collection<EContentType> contentTypes) {
        allowedContentTypes.clear();
        allowedContentTypes.addAll(contentTypes);
    }

    public static final Optional<Document> getQueryDocument() {
        return Optional.ofNullable(queryDocument);
    }

    public static final void setQueryDocument(final Document document) {
        queryDocument = document;
    }

    public static final float getHueWeight() {
        return hueWeight;
    }

    public static final void setHueWeight(final float importance) {
        hueWeight = importance;
    }

    public static final float getSaturationWeight() {
        return saturationWeight;
    }

    public static final void setSaturationWeight(final float importance) {
        saturationWeight = importance;
    }

    public static final float getBrightnessWeight() {
        return brightnessWeight;
    }

    public static final void setBrightnessWeight(final float importance) {
        brightnessWeight = importance;
    }

    public static final List<Document> getSimilarDocuments() {
        return similarDocuments;
    }

    public static final void setSimilarDocuments(final List<Document> documents) {
        similarDocuments.clear();
        similarDocuments.addAll(documents);
    }

    public static List<Document> getAllDocuments() {
        return allDocuments;
    }

    public static void setAllDocuments(List<Document> documents) {
        allDocuments.clear();
        allDocuments.addAll(documents);
    }

    public static void reset() {
        similarDocuments.clear();
        allDocuments.clear();
    }

}
