package org.mmr.core;

import com.google.gson.Gson;
import java.nio.file.Path;
import java.nio.file.Paths;
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

	private String indexDirectory;

	private String dataDirectory;

	private int histogramBinCount;

	private final Set<EContentType> allowedContentTypes = new HashSet<>();

	private Document queryDocument;

	private float hueWeight;

	private float saturationWeight;

	private float brightnessWeight;

	private final List<Document> allDocuments = new ArrayList<>();

	private final List<Document> similarDocuments = new ArrayList<>();

	private Context() {
		histogramBinCount = 64;

		allowedContentTypes.add(EContentType.BMP);
		allowedContentTypes.add(EContentType.JPEG);
		allowedContentTypes.add(EContentType.PNG);

		hueWeight = 0.3f;
		saturationWeight = 0.3f;
		brightnessWeight = 0.3f;
	}

	private static Context INSTANCE = new Context();

	public static final String toJson() {
		return new Gson().toJson(INSTANCE);
	}

	public static final void fromJson(final String json) {
		final Context context = new Gson().fromJson(json, Context.class);
		INSTANCE = context;
	}

	public static final Optional<Path> getIndexDirectory() {
		if (INSTANCE.indexDirectory != null) {
			return Optional.of(Paths.get(INSTANCE.indexDirectory));
		} else {
			return Optional.empty();
		}
	}

	public static final void setIndexDirectory(final Path directory) {
		INSTANCE.indexDirectory = directory.toString();
	}

	public static final Optional<Path> getDataDirectory() {
		if (INSTANCE.dataDirectory != null) {
			return Optional.of(Paths.get(INSTANCE.dataDirectory));
		} else {
			return Optional.empty();
		}
	}

	public static final void setDataDirectory(final Path directory) {
		INSTANCE.dataDirectory = directory.toString();
	}

	public static final int getHistogramBinCount() {
		return INSTANCE.histogramBinCount;
	}

	public static final void setHistogramBinCount(final int count) {
		INSTANCE.histogramBinCount = count;
	}

	public static final Set<EContentType> getAllowedContentTypes() {
		return INSTANCE.allowedContentTypes;
	}

	public static final void setAllowedContentTypes(final Collection<EContentType> contentTypes) {
		INSTANCE.allowedContentTypes.clear();
		INSTANCE.allowedContentTypes.addAll(contentTypes);
	}

	public static final Optional<Document> getQueryDocument() {
		return Optional.ofNullable(INSTANCE.queryDocument);
	}

	public static final void setQueryDocument(final Document document) {
		INSTANCE.queryDocument = document;
	}

	public static final float getHueWeight() {
		return INSTANCE.hueWeight;
	}

	public static final void setHueWeight(final float importance) {
		INSTANCE.hueWeight = importance;
	}

	public static final float getSaturationWeight() {
		return INSTANCE.saturationWeight;
	}

	public static final void setSaturationWeight(final float importance) {
		INSTANCE.saturationWeight = importance;
	}

	public static final float getBrightnessWeight() {
		return INSTANCE.brightnessWeight;
	}

	public static final void setBrightnessWeight(final float importance) {
		INSTANCE.brightnessWeight = importance;
	}

	public static final List<Document> getSimilarDocuments() {
		return INSTANCE.similarDocuments;
	}

	public static final void setSimilarDocuments(final List<Document> documents) {
		INSTANCE.similarDocuments.clear();
		INSTANCE.similarDocuments.addAll(documents);
	}

	public static List<Document> getAllDocuments() {
		return INSTANCE.allDocuments;
	}

	public static void setAllDocuments(List<Document> documents) {
		INSTANCE.allDocuments.clear();
		INSTANCE.allDocuments.addAll(documents);
	}

	public static void reset() {
		INSTANCE.similarDocuments.clear();
		INSTANCE.allDocuments.clear();
	}

}
