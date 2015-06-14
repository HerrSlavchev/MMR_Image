package org.mmr.core;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class PersistentIndex {

	private static final String FILE_NAME = "mmr-index.json";

	private static final Charset CHARSET = StandardCharsets.UTF_8;

	public static final void save() throws IOException {
		final Path indexDirectory = Context.getIndexDirectory().get();
		final Path indexFile = Paths.get(indexDirectory.toString(), FILE_NAME);

		Files.deleteIfExists(indexFile);
		Files.createFile(indexFile);

		Files.write(indexFile, Context.toJson().getBytes(CHARSET));
	}

	public static final boolean load(final Path indexDirectory) throws IOException {
		final Path indexFile = Paths.get(indexDirectory.toString(), FILE_NAME);

		if (Files.exists(indexFile)) {
			final String json = new String(Files.readAllBytes(indexFile), CHARSET);
			Context.fromJson(json);

			return true;
		} else {
			return false;
		}
	}

}
