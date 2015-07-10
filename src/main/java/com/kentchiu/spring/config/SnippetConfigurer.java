package com.kentchiu.spring.config;


import org.springframework.mock.web.MockHttpServletRequest;

/**
 * A configurer that can be used to configure the generated documentation snippets.
 *
 * @author Andy Wilkinson
 */
public class SnippetConfigurer extends
        AbstractNestedConfigurer<RestDocumentationConfigurer> {

    /**
     * The default encoding for documentation snippets
     *
     * @see #withEncoding(String)
     */
    public static final String DEFAULT_SNIPPET_ENCODING = "UTF-8";

    private String snippetEncoding = DEFAULT_SNIPPET_ENCODING;

    SnippetConfigurer(RestDocumentationConfigurer parent) {
        super(parent);
    }

    /**
     * Configures any documentation snippets to be written using the given
     * {@code encoding}. The default is UTF-8.
     *
     * @param encoding The encoding
     * @return {@code this}
     */
    public SnippetConfigurer withEncoding(String encoding) {
        this.snippetEncoding = encoding;
        return this;
    }

    @Override
    void apply(MockHttpServletRequest request) {
        RestDocumentationContext context = RestDocumentationContext.currentContext();
        if (context != null) {
            context.setSnippetEncoding(this.snippetEncoding);
        }
    }

}
