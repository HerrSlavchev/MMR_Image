package org.mmr.core;

public final class Similarity {

	private final Document document;

	private final float value;

	public Similarity(final Document document, final float value) {
		this.document = document;
		this.value = value;
	}

	public Document getDocument() {
		return document;
	}

	public float getValue() {
		return value;
	}
}
