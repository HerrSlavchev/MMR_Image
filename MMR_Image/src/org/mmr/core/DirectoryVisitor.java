package org.mmr.core;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

final class DirectoryVisitor implements FileVisitor<Path> {

	DirectoryVisitor() {
	}

	@Override
	public FileVisitResult preVisitDirectory(final Path directory, final BasicFileAttributes attributes) throws IOException {
		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult visitFile(final Path file, final BasicFileAttributes attributes) throws IOException {
		final Optional<Document> document = Extractor.extract(file, true);
		if (document.isPresent()) {
			Context.getAllDocuments().add(document.get());
		}

		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult visitFileFailed(final Path file, final IOException exception) throws IOException {
		if (exception != null) {
			Logger.getGlobal().log(Level.WARNING, "Unable to visit file!", exception);
		}
		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult postVisitDirectory(final Path directory, final IOException exception) throws IOException {
		if (exception != null) {
			Logger.getGlobal().log(Level.WARNING, "Unable to visit all siblings of directory!", exception);
		}

		return FileVisitResult.CONTINUE;
	}
}
