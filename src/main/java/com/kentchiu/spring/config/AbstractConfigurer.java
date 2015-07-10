package com.kentchiu.spring.config;

import org.springframework.mock.web.MockHttpServletRequest;

/**
 * Abstract configurer that declares methods that are internal to the documentation
 * configuration implementation.
 *
 * @author Andy Wilkinson
 */
abstract class AbstractConfigurer {

    /**
     * Applies the configuration, possibly be modifying the given {@code request}
     *
     * @param request the request that may be modified
     */
    abstract void apply(MockHttpServletRequest request);

}
