package org.mmr.core;

public final class Document {

	private final String path;

	private final int width;

	private final int height;

	private final float[][] histrogramHSB;

	public Document(final String path, final int width, final int height, final float[][] histrogramHSB) {
		this.path = path;
		this.width = width;
		this.height = height;
		this.histrogramHSB = histrogramHSB;
	}

	public String getPath() {
		return path;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public float[][] getHistrogramHSB() {
		return histrogramHSB;
	}

}
