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
			case JPEG:
				documentBean = getDocumentBean(path);
				break;
			default:
				final String errorMessage = String.format("Content type %s is allowed but not supported!", contentType);
				throw new UnsupportedOperationException(errorMessage);
		}

		return documentBean;
	}

	private static DocumentBean getDocumentBean(final Path path) throws IOException {
            
                final int bins = Context.getBins();
                
		final String absolutePath = path.toAbsolutePath().toString();

		final BufferedImage bufferedImage = ImageIO.read(path.toFile());
		final Raster raster = bufferedImage.getRaster();
		final int width = raster.getWidth();
		final int height = raster.getHeight();
                
                //RGB histogram components
		final float[] red = new float[bins];
		final float[] green = new float[bins];
		final float[] blue = new float[bins];
                
                //HSB histogram components
		final float[] hue = new float[bins];
		final float[] saturation = new float[bins];
		final float[] brightness = new float[bins];

                float[] tmpValues;
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
                                
				final int[] rgb = new int[3];
				raster.getPixel(x, y, rgb);
                                tmpValues = Fuzzyfier.extractValuesForIndex(rgb[0], 0, 255, bins);
                                Fuzzyfier.updateHistogram(tmpValues, red);
				tmpValues = Fuzzyfier.extractValuesForIndex(rgb[1], 0, 255, bins);
                                Fuzzyfier.updateHistogram(tmpValues, green);
                                tmpValues = Fuzzyfier.extractValuesForIndex(rgb[2], 0, 255, bins);
                                Fuzzyfier.updateHistogram(tmpValues, blue);

				final float[] hsb = new float[3];
				Color.RGBtoHSB(rgb[0], rgb[1], rgb[2], hsb);
				tmpValues = Fuzzyfier.extractValuesForIndex(hsb[0], 0, 1, bins);
                                Fuzzyfier.updateHistogram(tmpValues, hue);
				tmpValues = Fuzzyfier.extractValuesForIndex(hsb[1], 0, 1, bins);
                                Fuzzyfier.updateHistogram(tmpValues, saturation);
                                tmpValues = Fuzzyfier.extractValuesForIndex(hsb[2], 0, 1, bins);
                                Fuzzyfier.updateHistogram(tmpValues, brightness);
			}
		}

		final float[][] histogramRGB = new float[3][];
		histogramRGB[0] = red;
		histogramRGB[1] = green;
		histogramRGB[2] = blue;

		final float[][] histogramHSB = new float[3][];
		histogramHSB[0] = hue;
		histogramHSB[1] = saturation;
		histogramHSB[2] = brightness;

		return new DocumentBean(absolutePath, width, height, histogramRGB, histogramHSB);
	}
}
