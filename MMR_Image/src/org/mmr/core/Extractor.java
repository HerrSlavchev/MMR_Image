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

    /**
     * Use to create a document representation in the application domain
     * @param file  path to the file
     * @param fromAllowedContentType set to false if you want to override user type customizations
     * @return A document if the provided file has a correct type
     * @throws IOException 
     */
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
        Fuzzyfier.refresh(bins);
        
        final String absolutePath = file.toAbsolutePath().toString();

        final BufferedImage bufferedImage = ImageIO.read(file.toFile());
        final Raster raster = bufferedImage.getRaster();
        final int width = raster.getWidth();
        final int height = raster.getHeight();

        //HSB histogram components
        final float[] hue = new float[bins];
        final float[] saturation = new float[bins];
        final float[] brightness = new float[bins];

        float[] tmpValues;
        final int[] rgb = new int[4];
        final float[] hsb = new float[3];
        
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {

                raster.getPixel(x, y, rgb);
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
