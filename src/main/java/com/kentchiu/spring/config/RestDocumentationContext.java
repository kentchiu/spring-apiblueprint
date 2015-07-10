package com.kentchiu.spring.config;


import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * {@code RestDocumentationContext} encapsulates the context in which the documentation of
 * a RESTful API is being performed.
 *
 * @author Andy Wilkinson
 */
public final class RestDocumentationContext {

    private static final ThreadLocal<RestDocumentationContext> CONTEXTS = new InheritableThreadLocal<RestDocumentationContext>();

    private final AtomicInteger stepCount = new AtomicInteger(0);

    private final Method testMethod;

    private String snippetEncoding;

    private RestDocumentationContext() {
        this(null);
    }

    private RestDocumentationContext(Method testMethod) {
        this.testMethod = testMethod;
    }

    static void establishContext(Method testMethod) {
        CONTEXTS.set(new RestDocumentationContext(testMethod));
    }

    static void clearContext() {
        CONTEXTS.set(null);
    }

    /**
     * Returns the current context, never {@code null}.
     *
     * @return The current context
     */
    public static RestDocumentationContext currentContext() {
        return CONTEXTS.get();
    }

    /**
     * Returns the test {@link Method method} that is currently executing
     *
     * @return The test method
     */
    public Method getTestMethod() {
        return this.testMethod;
    }

    /**
     * Gets and then increments the current step count
     *
     * @return The step count prior to it being incremented
     */
    int getAndIncrementStepCount() {
        return this.stepCount.getAndIncrement();
    }

    /**
     * Gets the current step count
     *
     * @return The current step count
     */
    public int getStepCount() {
        return this.stepCount.get();
    }

    /**
     * Gets the encoding to be used when writing snippets
     *
     * @return The snippet encoding
     */
    public String getSnippetEncoding() {
        return this.snippetEncoding;
    }

    void setSnippetEncoding(String snippetEncoding) {
        this.snippetEncoding = snippetEncoding;
    }

}
