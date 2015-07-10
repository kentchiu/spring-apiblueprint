package com.kentchiu.spring.config;


import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.test.web.servlet.setup.ConfigurableMockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcConfigurer;
import org.springframework.test.web.servlet.setup.MockMvcConfigurerAdapter;
import org.springframework.util.StringUtils;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.List;

/**
 * A {@link MockMvcConfigurer} that can be used to configure the documentation
 *
 * @author Andy Wilkinson
 * @author Dmitriy Mayboroda
 * @see ConfigurableMockMvcBuilder#apply(MockMvcConfigurer)
 */
public class RestDocumentationConfigurer extends MockMvcConfigurerAdapter {

    private final UriConfigurer uriConfigurer = new UriConfigurer(this);

    private final SnippetConfigurer snippetConfigurer = new SnippetConfigurer(this);

    private final RequestPostProcessor requestPostProcessor;

    /**
     * Creates a new {@link RestDocumentationConfigurer}.
     */
    public RestDocumentationConfigurer() {
        this.requestPostProcessor = new ConfigurerApplyingRequestPostProcessor(
                Arrays.<AbstractConfigurer>asList(this.uriConfigurer,
                        this.snippetConfigurer, new StepCountConfigurer(),
                        new ContentLengthHeaderConfigurer()));
    }

    public UriConfigurer uris() {
        return this.uriConfigurer;
    }

    public SnippetConfigurer snippets() {
        return this.snippetConfigurer;
    }

    @Override
    public RequestPostProcessor beforeMockMvcCreated(
            ConfigurableMockMvcBuilder<?> builder, WebApplicationContext context) {
        return this.requestPostProcessor;
    }

    private static class StepCountConfigurer extends AbstractConfigurer {

        @Override
        void apply(MockHttpServletRequest request) {
            RestDocumentationContext currentContext = RestDocumentationContext
                    .currentContext();
            if (currentContext != null) {
                currentContext.getAndIncrementStepCount();
            }
        }

    }

    private static class ContentLengthHeaderConfigurer extends AbstractConfigurer {

        @Override
        void apply(MockHttpServletRequest request) {
            long contentLength = request.getContentLengthLong();
            if (contentLength > 0
                    && !StringUtils.hasText(request.getHeader("Content-Length"))) {
                request.addHeader("Content-Length", request.getContentLengthLong());
            }
        }

    }

    private static class ConfigurerApplyingRequestPostProcessor implements
            RequestPostProcessor {

        private final List<AbstractConfigurer> configurers;

        private ConfigurerApplyingRequestPostProcessor(
                List<AbstractConfigurer> configurers) {
            this.configurers = configurers;
        }

        @Override
        public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
            for (AbstractConfigurer configurer : this.configurers) {
                configurer.apply(request);
            }
            return request;
        }

    }
}
