package org.mmr.core;

public final class DocumentBean {

	private final String path;

	private final int width;

	private final int height;

	private final float[][] dataRGB;

	private final float[][] dataHSB;

	private final float[][] histrogramRGB = new float[3][];

	private final float[][] histrogramHSB = new float[3][];

	public DocumentBean(final String path, final int width, final int height, final float[][] dataRGB, final float[][] dataHSB) {
		this.path = path;
		this.width = width;
		this.height = height;
		this.dataRGB = dataRGB;
		this.dataHSB = dataHSB;
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

	public float[][] getDataRGB() {
		return dataRGB;
	}

	public float[][] getDataHSB() {
		return dataHSB;
	}

	public float[][] getHistrogramRGB() {
		return histrogramRGB;
	}

	public float[][] getHistrogramHSB() {
		return histrogramHSB;
	}

}
