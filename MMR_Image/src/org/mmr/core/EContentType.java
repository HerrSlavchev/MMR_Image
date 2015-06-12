package org.mmr.core;

import java.util.Optional;

/**
 * Represents different content types supported by the system.
 */
public enum EContentType {

	BMP("image/x-ms-bmp", "bmp"), JPEG("image/jpeg", "jpg"), PNG("image/png", "png");

	private final String value;

	private final String extension;

	private EContentType(final String value, final String extension) {
		this.value = value;
		this.extension = extension;
	}

	public static final Optional<EContentType> of(final String value) {
		for (final EContentType contentType : EContentType.values()) {
			if (value.toLowerCase().startsWith(contentType.value)) {
				return Optional.of(contentType);
			}
		}

		return Optional.empty();
	}

	public String getExtension() {
		return extension;
	}

	@Override
	public String toString() {
		return value;
	}

}
