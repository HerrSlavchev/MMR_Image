package org.mmr.core;

import java.io.IOException;
import static java.nio.file.FileVisitOption.FOLLOW_LINKS;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
/*
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexNotFoundException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
*/
/**
 * Used to process a context.
 */
public final class Engine {
        /*
	private static final Directory DIRECTORY = new RAMDirectory();

	private static final Analyzer ANALYZER = new EngineAnalyser();
        */
	private Engine() {
	}

	/**
	 * Process a given context and create an index.
	 *
	 * @param context - contains information about chosen directory and allowed
	 * content types.
	 */
	public static final void createIndex(final Context context) {
                
		final Optional<Path> chosenDirectory = context.getChosenDirectory();

		if (chosenDirectory.isPresent()) {
			final Set<EContentType> allowedContentTypes = context.getAllowedContentTypes();

			if (!allowedContentTypes.isEmpty()) {
                                try {
                                    final List<DocumentBean> documents = context.getDocuments();
                                    Files.walkFileTree(
                                            chosenDirectory.get(),
                                            EnumSet.of(FOLLOW_LINKS),
                                            Integer.MAX_VALUE,
                                            new EngineDirectoryVisitor(allowedContentTypes, documents)
                                    );
				} catch (final IOException exception) {
					Logger.getGlobal().log(Level.SEVERE, "Unexpected exception occurred while walking through a file tree.", exception);
				}
			}
		} else {
			throw new RuntimeException("Directory is not available in context!");
		}
	}

	/*private static IndexWriter createIndexWriter() throws IOException {
		final IndexWriterConfig indexWriterConfig = new IndexWriterConfig(ANALYZER);
		indexWriterConfig.setOpenMode(OpenMode.CREATE);

		return new IndexWriter(DIRECTORY, indexWriterConfig);
	}*/

	/**
	 * Attempts to find all indexed documents containing a specific query. a
	 * specific query.
	 *
	 * @param queryString - the query string
	 * @return - the results from this query search
	 *
	 * @throws java.io.IOException
	 * @throws org.apache.lucene.queryparser.classic.ParseException
	 */
	public static List<DocumentBean> search(final String queryString) throws IOException /*, ParseException*/ {
		if (queryString == null || queryString.trim().isEmpty()) {
			throw new RuntimeException("Missing query!");
		}
                final List<DocumentBean> documentBeans = new ArrayList<>();
                /*
		try (final IndexReader reader = DirectoryReader.open(DIRECTORY)) {
			final IndexSearcher searcher = new IndexSearcher(reader);

			final QueryParser parser = new QueryParser(DocumentBean.CONTENT_FIELD_NAME, ANALYZER);
			final Query query = parser.parse(queryString);

			final TopDocs topDocs = searcher.search(query, 1000);

			

			for (final ScoreDoc scoreDoc : topDocs.scoreDocs) {
				final Document document = searcher.doc(scoreDoc.doc);

				documentBeans.add(DocumentBean.of(document));
			}

			
		} catch (IndexNotFoundException infE){
                    throw new RuntimeException("No current index exists!");
                }
                */
                return documentBeans;
	}

}
