package org.mmr.core;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import javax.imageio.ImageIO;
import org.apache.tika.Tika;

/**
 * Use to create documents from different image types.
 */
public class Extractor {

    public static Optional<Document> extract(final Path file, final boolean fromAllowedContentType) throws IOException {
        final String detectedValue = new Tika().detect(file.toFile());
        final Optional<EContentType> contentType = EContentType.of(detectedValue);

        if (contentType.isPresent()) {
            if (fromAllowedContentType && !Context.getAllowedContentTypes().contains(contentType.get())) {
                return Optional.empty();
            }

            return Optional.of(createDocumentBean(file));
        } else {
            return Optional.empty();
        }
    }

    private static Document createDocumentBean(final Path file) throws IOException {
        final int bins = Context.getHistogramBinCount();

        final String absolutePath = file.toAbsolutePath().toString();

        final BufferedImage bufferedImage = ImageIO.read(file.toFile());
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

                final int[] rgb = new int[4];
                raster.getPixel(x, y, rgb);
                tmpValues = Fuzzyfier.extractValuesForIndexRGB(rgb[0], 0, 255, bins);
                Fuzzyfier.updateHistogram(tmpValues, red);
                tmpValues = Fuzzyfier.extractValuesForIndexRGB(rgb[1], 0, 255, bins);
                Fuzzyfier.updateHistogram(tmpValues, green);
                tmpValues = Fuzzyfier.extractValuesForIndexRGB(rgb[2], 0, 255, bins);
                Fuzzyfier.updateHistogram(tmpValues, blue);

                final float[] hsb = new float[3];
                Color.RGBtoHSB(rgb[0], rgb[1], rgb[2], hsb);
                tmpValues = Fuzzyfier.extractValuesForIndexHSB(hsb[0], 0, 1, bins);
                Fuzzyfier.updateHistogram(tmpValues, hue);
                tmpValues = Fuzzyfier.extractValuesForIndexHSB(hsb[1], 0, 1, bins);
                Fuzzyfier.updateHistogram(tmpValues, saturation);
                tmpValues = Fuzzyfier.extractValuesForIndexHSB(hsb[2], 0, 1, bins);
                Fuzzyfier.updateHistogram(tmpValues, brightness);
            }
        }

        final float[][] histogramHSB = new float[][]{
            hue,
            saturation,
            brightness,};

        return new Document(absolutePath, width, height, histogramHSB);
    }
}
