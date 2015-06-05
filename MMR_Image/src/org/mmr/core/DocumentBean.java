package org.mmr.core;

import java.util.Optional;

public final class DocumentBean {

	private final String path;
	private final String content;
	private Optional<String> title = Optional.empty();
        
        private float[][] histogramRGBFull = new float[3][256];
        private float[][] histogramHSVFull = new float[3][256];
        
        private float[][] histrogramRGBBins;
        private float[][] histrogramHSVBins;

	public DocumentBean(final String path, final String content, final String title) {
		this(path, content);
		this.title = Optional.of(title);
	}

	public DocumentBean(final String path, final String content) {
		this.path = path;
		this.content = content;
	}	

	public String getPath() {
		return path;
	}

	public String getContent() {
		return content;
	}

	public Optional<String> getTitle() {
		return title;
	}

	@Override
	public String toString() {
		return String.format("path: %s, title: %s", path, title.orElse(""));
	}

    public float[][] getHistogramRGBFull() {
        return histogramRGBFull;
    }

    public void setHistogramRGBFull(float[][] histogramRGBFull) {
        this.histogramRGBFull = histogramRGBFull;
    }

    public float[][] getHistogramHSVFull() {
        return histogramHSVFull;
    }

    public void setHistogramHSVFull(float[][] histogramHSVFull) {
        this.histogramHSVFull = histogramHSVFull;
    }

    public float[][] getHistrogramRGBBins() {
        return histrogramRGBBins;
    }

    public void setHistrogramRGBBins(float[][] histrogramRGBBins) {
        this.histrogramRGBBins = histrogramRGBBins;
    }

    public float[][] getHistrogramHSVBins() {
        return histrogramHSVBins;
    }

    public void setHistrogramHSVBins(float[][] histrogramHSVBins) {
        this.histrogramHSVBins = histrogramHSVBins;
    }

}
