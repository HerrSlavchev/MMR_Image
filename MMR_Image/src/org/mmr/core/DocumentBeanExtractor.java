package org.mmr.core;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.IOException;
import java.nio.file.Path;
import javax.imageio.ImageIO;

/**
 * Use to create documents from different image types.
 */
public class DocumentBeanExtractor {

	public static DocumentBean getDocumentBean(final EContentType contentType, final Path path) throws IOException {
		final DocumentBean documentBean;

		switch (contentType) {
			case BMP:
			case PNG:
				documentBean = getDocumentBean(path);
				break;
			default:
				final String errorMessage = String.format("Content type %s is allowed but not supported!", contentType);
				throw new UnsupportedOperationException(errorMessage);
		}

		return documentBean;
	}

	private static DocumentBean getDocumentBean(final Path path) throws IOException {
		final String absolutePath = path.toAbsolutePath().toString();

		final BufferedImage bufferedImage = ImageIO.read(path.toFile());
		final Raster raster = bufferedImage.getRaster();
		final int width = raster.getWidth();
		final int height = raster.getHeight();
		final int pixelCount = width * height;

		final float[] red = new float[pixelCount];
		final float[] green = new float[pixelCount];
		final float[] blue = new float[pixelCount];

		final float[] hue = new float[pixelCount];
		final float[] saturation = new float[pixelCount];
		final float[] brightness = new float[pixelCount];

		int pixelIndex = 0;
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				final int[] rgb = new int[3];
				raster.getPixel(x, y, rgb);

				red[pixelIndex] = rgb[0];
				green[pixelIndex] = rgb[1];
				blue[pixelIndex] = rgb[2];

				final float[] hsb = new float[3];
				Color.RGBtoHSB(rgb[0], rgb[1], rgb[2], hsb);

				hue[pixelIndex] = hsb[0];
				saturation[pixelIndex] = hsb[1];
				brightness[pixelIndex] = hsb[2];

				pixelIndex++;
			}
		}

		final float[][] histogramRGBFull = new float[3][pixelCount];
		histogramRGBFull[0] = red;
		histogramRGBFull[1] = green;
		histogramRGBFull[2] = blue;

		final float[][] histogramHSBFull = new float[3][pixelCount];
		histogramHSBFull[0] = hue;
		histogramHSBFull[1] = saturation;
		histogramHSBFull[2] = brightness;

		return new DocumentBean(absolutePath, width, height, histogramRGBFull, histogramHSBFull);
	}
}
