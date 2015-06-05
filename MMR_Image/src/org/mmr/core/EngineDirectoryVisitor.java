package org.mmr.core;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.tika.Tika;

final class EngineDirectoryVisitor implements FileVisitor<Path> {

    private final Set<EContentType> allowedContentTypes;
    private final List<DocumentBean> documents;

    private final Tika contentTypeDetector = new Tika();

    EngineDirectoryVisitor(final Set<EContentType> allowedContentTypes, final List<DocumentBean> documents) {
        this.allowedContentTypes = allowedContentTypes;
        this.documents = documents;
    }

    @Override
    public FileVisitResult preVisitDirectory(final Path directory, final BasicFileAttributes attributes) throws IOException {
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(final Path file, final BasicFileAttributes attributes) throws IOException {
        final String detectedValue = contentTypeDetector.detect(file.toFile());
        final Optional<EContentType> detectedContentType = EContentType.of(detectedValue);

        if (detectedContentType.isPresent()) {
            final EContentType contentType = detectedContentType.get();
            if (allowedContentTypes.contains(contentType)) {
                DocumentBean documentBean = DocumentBeanExtractor.getDocumentBean(contentType, file);
                addDocumentBeanToIndex(documentBean);
            }
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

    private void addDocumentBeanToIndex(final DocumentBean documentBean) throws IOException {
        documents.add(documentBean);
    }
}
