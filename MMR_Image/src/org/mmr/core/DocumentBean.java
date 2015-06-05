package org.mmr.core;

public final class DocumentBean {

	private final String path;

	private final int width;

	private final int height;

	private final float[][] dataRGB;

	private final float[][] dataHSB;

	private float[][] histrogramRGB;

	private float[][] histrogramHSB;

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

	/**
	 * @return null, if array isn't initialized!
	 */
	public float[][] getHistrogramRGB() {
		return histrogramRGB;
	}

	public void setHistrogramRGB(final float[][] histrogramRGB) {
		this.histrogramRGB = histrogramRGB;
	}

	/**
	 * @return null, if array isn't initialized!
	 */
	public float[][] getHistrogramHSB() {
		return histrogramHSB;
	}

	public void setHistrogramHSB(final float[][] histrogramHSB) {
		this.histrogramHSB = histrogramHSB;
	}

}
