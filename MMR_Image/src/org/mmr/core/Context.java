package org.mmr.core;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Used for state and configuration sharing among components.
 */
public final class Context {

    private static Path chosenDirectory;
    private static int bins;
    private static List<DocumentBean> documents = new ArrayList<>();
    private static final Set<EContentType> allowedContentTypes = new HashSet<>();

    static {
        // Default allowed content types are initialized.
        bins = 64;
        allowedContentTypes.add(EContentType.PNG);
        allowedContentTypes.add(EContentType.BMP);
    }

    public static Optional<Path> getChosenDirectory() {
        return Optional.ofNullable(chosenDirectory);
    }

    public static void setChosenDirectory(final Path directory) {
        chosenDirectory = directory;
    }

    public static Set<EContentType> getAllowedContentTypes() {
        return allowedContentTypes;
    }

    public static void setAllowedContentTypes(final Collection<EContentType> contentTypes) {
        allowedContentTypes.clear();
        allowedContentTypes.addAll(contentTypes);
    }

    public static List<DocumentBean> getDocuments() {
        return documents;
    }

    public static void setDocuments(List<DocumentBean> docs) {
        documents = docs;
    }

    public static int getBins() {
        return bins;
    }

    public static void setBins(int number) {
        bins = number;
    }

}
