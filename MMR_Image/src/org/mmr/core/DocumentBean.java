package org.mmr.core;

public final class DocumentBean {

	private final String path;

	private final int width;

	private final int height;

	private final float[][] histrogramRGB;

	private final float[][] histrogramHSB;

	public DocumentBean(final String path, final int width, final int height, final float[][] histrogramRGB, final float[][] histrogramHSB) {
		this.path = path;
		this.width = width;
		this.height = height;
		this.histrogramRGB = histrogramRGB;
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

	public float[][] getHistrogramRGB() {
		return histrogramRGB;
	}

	public float[][] getHistrogramHSB() {
		return histrogramHSB;
	}

}
