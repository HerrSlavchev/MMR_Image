package org.mmr.core;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public final class Engine {

	private Engine() {
	}

	public static final void createIndex() throws IOException {
		if (!Context.getIndexDirectory().isPresent()) {
			throw new RuntimeException("Before index creation: No index directory in context!");
		}

		if (!Context.getDataDirectory().isPresent()) {
			throw new RuntimeException("Before index creation: No data directory in context!");
		}

		if (Context.getHistogramBinCount() <= 0) {
			throw new RuntimeException("Before index creation: Histogram bins count is <= 0 in context!");
		}

		if (Context.getAllowedContentTypes().isEmpty()) {
			throw new RuntimeException("Before index creation: No allowed content types in context!");
		}

                Context.reset();
		Files.walkFileTree(Context.getDataDirectory().get(), new DirectoryVisitor());
	}

	public static final List<Similarity> search() {
		if (!Context.getQueryDocument().isPresent()) {
			throw new RuntimeException("Before search: No query document in context!");
		}

		return new ArrayList<>();
	}

}
