package com.kentchiu.spring.config;


import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListener;
import org.springframework.test.context.support.AbstractTestExecutionListener;

/**
 * A {@link TestExecutionListener} that sets up and tears down the Spring REST Docs
 * context for each test method
 *
 * @author Andy Wilkinson
 */
public class RestDocumentationTestExecutionListener extends AbstractTestExecutionListener {

    @Override
    public void beforeTestMethod(TestContext testContext) throws Exception {
        RestDocumentationContext.establishContext(testContext.getTestMethod());
    }

    @Override
    public void afterTestMethod(TestContext testContext) throws Exception {
        RestDocumentationContext.clearContext();
    }
}
