package com.kentchiu.spring.config;


import org.springframework.test.web.servlet.setup.MockMvcConfigurer;

/**
 * A configurer that is nested and, therefore, has a parent.
 *
 * @param <PARENT> The parent's type
 * @author awilkinson
 */
public interface NestedConfigurer<PARENT extends MockMvcConfigurer> {

    /**
     * Returns the configurer's parent
     *
     * @return the parent
     */
    PARENT and();
}
