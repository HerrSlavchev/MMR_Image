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

/**
 * Used to process a context.
 */
public final class Engine {

    private Engine() {
    }

    /**
     * Process a given context and create an index.
     *
     * @param context - contains information about chosen directory and allowed
     * content types.
     */
    public static final void createIndex() {

        final Optional<Path> chosenDirectory = Context.getChosenDirectory();

        if (chosenDirectory.isPresent()) {
            final Set<EContentType> allowedContentTypes = Context.getAllowedContentTypes();

            if (!allowedContentTypes.isEmpty()) {
                try {
                    final List<DocumentBean> documents = Context.getDocuments();
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

    /**
     * Attempts to find all indexed documents containing a specific query. a
     * specific query.
     *
     * @param queryString - the query string
     * @return - the results from this query search
     *
     * @throws java.io.IOException
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
