/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mmr.core;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Use to create documents from different image types.
 *
 * @author root
 */
public class DocumentBeanExtractor {

    public static DocumentBean getDocumentBean(final EContentType contentType, final Path path) throws IOException {
        final DocumentBean documentBean;
        switch (contentType) {

            case BMP:
                 documentBean = getBMPDocumentBean(path);
                break;
            case PNG:
                documentBean = getPNGDocumentBean(path);
                break;

            default:
                final String errorMessage = String.format("Content type %s is allowed but not supported!", contentType);
                throw new UnsupportedOperationException(errorMessage);
        }
        return documentBean;
    }
    
    private static DocumentBean getPNGDocumentBean(final Path path) throws IOException {
        final String absolutePath = path.toAbsolutePath().toString();
        
        return new DocumentBean(absolutePath, "");
    }
    
    private static DocumentBean getBMPDocumentBean(final Path path) throws IOException {
        final String absolutePath = path.toAbsolutePath().toString();
        
        return new DocumentBean(absolutePath, "");
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
}
