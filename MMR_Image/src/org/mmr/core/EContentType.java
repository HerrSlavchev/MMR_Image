package org.mmr.core;

import java.util.Optional;

/**
 * Represents different content types supported by the system.
 */
public enum EContentType {

    BMP("image/x-ms-bmp"), JPEG("image/jpeg"), PNG("image/png");

    private final String value;

    private EContentType(final String value) {
        this.value = value;
    }

    public static final Optional<EContentType> of(final String value) {
        for (final EContentType contentType : EContentType.values()) {
            if (value.toLowerCase().startsWith(contentType.value)) {
                return Optional.of(contentType);
            }
        }

        return Optional.empty();
    }

    @Override
    public String toString() {
        return value;
    }

}
