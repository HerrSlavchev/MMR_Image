package org.mmr.core;

public final class DocumentBean {

	private final String path;

	private final int width;

	private final int height;

	private final float[][] histogramRGBFull;

	private final float[][] histogramHSBFull;

	private float[][] histrogramRGBBins;

	private float[][] histrogramHSBBins;

	public DocumentBean(final String path, final int width, final int height, final float[][] histogramRGBFull, final float[][] histogramHSBFull) {
		this.path = path;
		this.width = width;
		this.height = height;
		this.histogramRGBFull = histogramRGBFull;
		this.histogramHSBFull = histogramHSBFull;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public String getPath() {
		return path;
	}

	public float[][] getHistogramRGBFull() {
		return histogramRGBFull;
	}

	public float[][] getHistogramHSBFull() {
		return histogramHSBFull;
	}

	/**
	 * @return null, if array isn't initialized!
	 */
	public float[][] getHistrogramRGBBins() {
		return histrogramRGBBins;
	}

	public void setHistrogramRGBBins(final float[][] histrogramRGBBins) {
		this.histrogramRGBBins = histrogramRGBBins;
	}

	/**
	 * @return null, if array isn't initialized!
	 */
	public float[][] getHistrogramHSBBins() {
		return histrogramHSBBins;
	}

	public void setHistrogramHSBBins(final float[][] histrogramHSBBins) {
		this.histrogramHSBBins = histrogramHSBBins;
	}

}
