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

	private static float hueImportance;

	private static float saturationImportance;

	private static float brightnessImportance;

	private static final List<DocumentBean> similarDocuments = new ArrayList<>();

	static {
		// Default values are initialized.
		histogramBinCount = 64;

		allowedContentTypes.add(EContentType.BMP);
		allowedContentTypes.add(EContentType.JPEG);
		allowedContentTypes.add(EContentType.PNG);

		hueImportance = 0.33f;
		saturationImportance = 0.33f;
		brightnessImportance = 0.33f;
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

	public static float getHueImportance() {
		return hueImportance;
	}

	public static void setHueImportance(final float importance) {
		hueImportance = importance;
	}

	public static float getSaturationImportance() {
		return saturationImportance;
	}

	public static void setSaturationImportance(final float importance) {
		saturationImportance = importance;
	}

	public static float getBrightnessImportance() {
		return brightnessImportance;
	}

	public static void setBrightnessImportance(final float importance) {
		brightnessImportance = importance;
	}

	public static List<DocumentBean> getDocuments() {
		return similarDocuments;
	}

	public static void setDocuments(final List<DocumentBean> documents) {
		similarDocuments.clear();
		similarDocuments.addAll(documents);
	}

}
