package com.kentchiu.spring.config;


import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.test.web.servlet.setup.ConfigurableMockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcConfigurer;
import org.springframework.web.context.WebApplicationContext;

/**
 * Base class for {@link NestedConfigurer} implementations.
 *
 * @param <PARENT> The type of the configurer's parent
 * @author Andy Wilkinson
 */
abstract class AbstractNestedConfigurer<PARENT extends MockMvcConfigurer> extends
        AbstractConfigurer implements NestedConfigurer<PARENT>, MockMvcConfigurer {

    private final PARENT parent;

    protected AbstractNestedConfigurer(PARENT parent) {
        this.parent = parent;
    }

    @Override
    public PARENT and() {
        return this.parent;
    }

    @Override
    public void afterConfigurerAdded(ConfigurableMockMvcBuilder<?> builder) {
        this.parent.afterConfigurerAdded(builder);
    }

    @Override
    public RequestPostProcessor beforeMockMvcCreated(
            ConfigurableMockMvcBuilder<?> builder, WebApplicationContext context) {
        return this.parent.beforeMockMvcCreated(builder, context);
    }

}
