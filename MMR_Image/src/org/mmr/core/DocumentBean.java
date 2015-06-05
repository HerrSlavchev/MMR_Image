package org.mmr.core;

import java.util.Optional;
/*
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
*/

public final class DocumentBean {

	public static final String CONTENT_FIELD_NAME = "content";

	private static final String PATH_FIELD_NAME = "path";

	private static final String TITLE_FIELD_NAME = "title";

	private final String path;

	private final String content;

	private Optional<String> title = Optional.empty();

	public DocumentBean(final String path, final String content, final String title) {
		this(path, content);
		this.title = Optional.of(title);
	}

	public DocumentBean(final String path, final String content) {
		this.path = path;
		this.content = content;
	}

	/*public static final DocumentBean of(final Document document) {
		final String path = document.get(PATH_FIELD_NAME);
		final String content = "";
		final String title = document.get(TITLE_FIELD_NAME);

		if (title == null) {
			return new DocumentBean(path, content);
		} else {
			return new DocumentBean(path, content, title);
		}
	}

	public Document asDocument() {
		final Document document = new Document();

		final Field pathField = new StringField(PATH_FIELD_NAME, path, Store.YES);
		document.add(pathField);

		final Field contentField = new TextField(CONTENT_FIELD_NAME, content, Store.NO);
		document.add(contentField);

		if (title.isPresent()) {
			final Field titleField = new StringField(TITLE_FIELD_NAME, title.get(), Store.YES);
			document.add(titleField);
		}

		return document;
	}*/

	public String getPath() {
		return path;
	}

	public String getContent() {
		return content;
	}

	public Optional<String> getTitle() {
		return title;
	}

	@Override
	public String toString() {
		return String.format("path: %s, title: %s", path, title.orElse(""));
	}

}
