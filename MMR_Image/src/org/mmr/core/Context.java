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

	private static DocumentBean queryDocument;

	private static float hueSimilarityImportance;

	private static float saturationSimilarityImportance;

	private static float brightnessSimilarityImportance;

	private static final List<DocumentBean> similarDocuments = new ArrayList<>();

	static {
		// Default values are initialized.
		histogramBinCount = 64;

		allowedContentTypes.add(EContentType.BMP);
		allowedContentTypes.add(EContentType.JPEG);
		allowedContentTypes.add(EContentType.PNG);

		hueSimilarityImportance = 0.33f;
		saturationSimilarityImportance = 0.33f;
		brightnessSimilarityImportance = 0.33f;
	}

	public static Optional<Path> getIndexDirectory() {
		return Optional.ofNullable(indexDirectory);
	}

	public static void setIndexDirectory(final Path directory) {
		indexDirectory = directory;
	}

	public static Optional<Path> getDataDirectory() {
		return Optional.ofNullable(dataDirectory);
	}

	public static void setDataDirectory(final Path directory) {
		dataDirectory = directory;
	}

	public static int getHistogramBinCount() {
		return histogramBinCount;
	}

	public static void setHistogramBinCount(final int count) {
		histogramBinCount = count;
	}

	public static Set<EContentType> getAllowedContentTypes() {
		return allowedContentTypes;
	}

	public static void setAllowedContentTypes(final Collection<EContentType> contentTypes) {
		allowedContentTypes.clear();
		allowedContentTypes.addAll(contentTypes);
	}

	public static Optional<DocumentBean> getQueryDocument() {
		return Optional.ofNullable(queryDocument);
	}

	public static void setQueryDocument(final DocumentBean document) {
		queryDocument = document;
	}

	public static float getHueSimilarityImportance() {
		return hueSimilarityImportance;
	}

	public static void setHueSimilarityImportance(final float similarityImportance) {
		hueSimilarityImportance = similarityImportance;
	}

	public static float getSaturationSimilarityImportance() {
		return saturationSimilarityImportance;
	}

	public static void setSaturationSimilarityImportance(final float similarityImportance) {
		saturationSimilarityImportance = similarityImportance;
	}

	public static float getBrightnessSimilarityImportance() {
		return brightnessSimilarityImportance;
	}

	public static void setBrightnessSimilarityImportance(final float similarityImportance) {
		brightnessSimilarityImportance = similarityImportance;
	}

	public static List<DocumentBean> getDocuments() {
		return similarDocuments;
	}

	public static void setDocuments(final List<DocumentBean> documents) {
		similarDocuments.clear();
		similarDocuments.addAll(documents);
	}

}
