package com.kentchiu.spring.config;


import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.util.StringUtils;

/**
 * A configurer that can be used to configure the documented URIs
 *
 * @author Andy Wilkinson
 */
public class UriConfigurer extends AbstractNestedConfigurer<RestDocumentationConfigurer> {

    /**
     * The default scheme for documented URIs
     *
     * @see #withScheme(String)
     */
    public static final String DEFAULT_SCHEME = "http";

    /**
     * The defalt host for documented URIs
     *
     * @see #withHost(String)
     */
    public static final String DEFAULT_HOST = "localhost";

    /**
     * The default port for documented URIs
     *
     * @see #withPort(int)
     */
    public static final int DEFAULT_PORT = 8080;

    /**
     * The default context path for documented URIs
     *
     * @see #withContextPath(String)
     */
    public static final String DEFAULT_CONTEXT_PATH = "";

    private String scheme = DEFAULT_SCHEME;

    private String host = DEFAULT_HOST;

    private int port = DEFAULT_PORT;

    private String contextPath = DEFAULT_CONTEXT_PATH;

    protected UriConfigurer(RestDocumentationConfigurer parent) {
        super(parent);
    }

    /**
     * Configures any documented URIs to use the given {@code scheme}. The default is
     * {@code http}.
     *
     * @param scheme The URI scheme
     * @return {@code this}
     */
    public UriConfigurer withScheme(String scheme) {
        this.scheme = scheme;
        return this;
    }

    /**
     * Configures any documented URIs to use the given {@code host}. The default is
     * {@code localhost}.
     *
     * @param host The URI host
     * @return {@code this}
     */
    public UriConfigurer withHost(String host) {
        this.host = host;
        return this;
    }

    /**
     * Configures any documented URIs to use the given {@code port}. The default is
     * {@code 8080}.
     *
     * @param port The URI port
     * @return {@code this}
     */
    public UriConfigurer withPort(int port) {
        this.port = port;
        return this;
    }

    /**
     * Configures any documented URIs to use the given {@code contextPath}. The default is
     * an empty string.
     *
     * @param contextPath The context path
     * @return {@code this}
     */
    public UriConfigurer withContextPath(String contextPath) {
        this.contextPath = (StringUtils.hasText(contextPath) && !contextPath
                .startsWith("/")) ? "/" + contextPath : contextPath;
        return this;
    }

    @Override
    void apply(MockHttpServletRequest request) {
        request.setScheme(this.scheme);
        request.setServerPort(this.port);
        request.setServerName(this.host);
        request.setContextPath(this.contextPath);
    }

}
