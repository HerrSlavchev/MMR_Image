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

    private Path chosenDirectory;
    private int bins;
    private List<DocumentBean> documents = new ArrayList<>();
    private final Set<EContentType> allowedContentTypes = new HashSet<>();

    {
        // Default allowed content types are initialized.
        bins = 64;
        allowedContentTypes.add(EContentType.PNG);
        allowedContentTypes.add(EContentType.BMP);
    }

    public Optional<Path> getChosenDirectory() {
        return Optional.ofNullable(chosenDirectory);
    }

    public void setChosenDirectory(final Path directory) {
        chosenDirectory = directory;
    }

    public Set<EContentType> getAllowedContentTypes() {
        return allowedContentTypes;
    }

    public void setAllowedContentTypes(final Collection<EContentType> contentTypes) {
        allowedContentTypes.clear();
        allowedContentTypes.addAll(contentTypes);
    }

    public List<DocumentBean> getDocuments() {
        return documents;
    }

    public void setDocuments(List<DocumentBean> documents) {
        this.documents = documents;
    }

    public int getBins() {
        return bins;
    }

    public void setBins(int bins) {
        this.bins = bins;
    }

}
