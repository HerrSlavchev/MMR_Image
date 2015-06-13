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

	private static float hueImportance;

	private static float saturationImportance;

	private static float brightnessImportance;

	private static final List<Document> similarDocuments = new ArrayList<>();

	static {
		// Default values are initialized.
		histogramBinCount = 64;

		allowedContentTypes.add(EContentType.BMP);
		allowedContentTypes.add(EContentType.JPEG);
		allowedContentTypes.add(EContentType.PNG);

		hueImportance = 0.3f;
		saturationImportance = 0.3f;
		brightnessImportance = 0.3f;
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

	public static final float getHueImportance() {
		return hueImportance;
	}

	public static final void setHueImportance(final float importance) {
		hueImportance = importance;
	}

	public static final float getSaturationImportance() {
		return saturationImportance;
	}

	public static final void setSaturationImportance(final float importance) {
		saturationImportance = importance;
	}

	public static final float getBrightnessImportance() {
		return brightnessImportance;
	}

	public static final void setBrightnessImportance(final float importance) {
		brightnessImportance = importance;
	}

	public static final List<Document> getDocuments() {
		return similarDocuments;
	}

	public static final void setDocuments(final List<Document> documents) {
		similarDocuments.clear();
		similarDocuments.addAll(documents);
	}

}
