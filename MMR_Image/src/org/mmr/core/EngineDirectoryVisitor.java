package org.mmr.core;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
//import org.apache.lucene.index.IndexWriter;
import org.apache.tika.Tika;
//import org.jsoup.Jsoup;

final class EngineDirectoryVisitor implements FileVisitor<Path> {

	private final Set<EContentType> allowedContentTypes;
        private final List<DocumentBean> documents;
	//private final IndexWriter indexWriter;

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
				switch (contentType) {
                                        /*
					case TXT:
						final DocumentBean txtDocumentBean = getTxtDocumentBean(file);
						addDocumentBeanToIndex(txtDocumentBean);
						break;
					case HTML:
						final DocumentBean htmlDocumentBean = getHtmlDocumentBean(file);
						addDocumentBeanToIndex(htmlDocumentBean);
						break;
                                        */
					default:
						final String errorMessage = String.format("Content type %s is allowed but not supported!", contentType);
						throw new UnsupportedOperationException(errorMessage);
				}
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

	/*private DocumentBean getTxtDocumentBean(final Path path) throws IOException {
		final String absolutePath = path.toAbsolutePath().toString();
		final byte[] bytes = Files.readAllBytes(path);
		final String content = new String(bytes, StandardCharsets.UTF_8);

		return new DocumentBean(absolutePath, content);
	}

	private DocumentBean getHtmlDocumentBean(final Path path) throws IOException {
		final String absolutePath = path.toAbsolutePath().toString();

		org.jsoup.nodes.Document jsoupDocument = Jsoup.parse(path.toFile(), StandardCharsets.UTF_8.name());
		final String title = jsoupDocument.title();
		final String content = jsoupDocument.text();

		return new DocumentBean(absolutePath, content, title);
	}*/

	private void addDocumentBeanToIndex(final DocumentBean documentBean) throws IOException {
		//System.out.println("Add: " + documentBean);
		//indexWriter.addDocument(documentBean.asDocument());
                documents.add(documentBean);
	}
}
