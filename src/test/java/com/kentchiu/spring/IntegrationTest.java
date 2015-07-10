package com.kentchiu.spring;

import com.google.common.collect.ImmutableMap;
import com.kentchiu.spring.config.RestDocumentationConfigurer;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.test.web.servlet.setup.ConfigurableMockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.web.servlet.setup.MockMvcConfigurer;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = IntegrationTest.TestConfiguration.class)
public class IntegrationTest {

    @Rule
    public TestName testName = new TestName();
    @Autowired
    private WebApplicationContext context;

    protected Path getApiBlueprintDocument() throws URISyntaxException, IOException {
        return getDocumentHome().resolve(testName.getMethodName() + ".md");
    }

    protected Path getDocumentHome() throws URISyntaxException, IOException {
        Path folder = Paths.get(this.getClass().getResource("/").toURI()).getParent().resolve("apiblueprint-snippet").resolve(this.getClass().getSimpleName());
        if (!Files.exists(folder)) {
            Files.createDirectories(folder);
        }
        return folder;
    }


    @Test
    public void testName() throws Exception {
        RestDocumentationConfigurer configurer = new RestDocumentationConfigurer();
        configurer.uris().withHost("8.8.8.8").withPort(7777);

        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
                .apply(configurer)
                .alwaysDo(new ApiBlueprintResultHandler(getApiBlueprintDocument()))
                .alwaysDo(new CurlResultHandler(getDocumentHome().resolve(testName.getMethodName() + "-curl" + ".md")))
                .alwaysDo(new AttributeResultHandler(getDocumentHome()))
                .alwaysDo(print())
                .alwaysDo(print())
                .build();


        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/").contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk());


    }

    @Configuration
    @EnableWebMvc
    static class TestConfiguration extends WebMvcConfigurerAdapter {

        @Bean
        public TestController testController() {
            return new TestController();
        }

    }

    @RestController
    static class TestController {

        @RequestMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
        public Map<String, Object> foo() {
            return ImmutableMap.of("foo", "bar");
        }
    }


    class DocumentConfigurer implements MockMvcConfigurer {
        @Override
        public void afterConfigurerAdded(ConfigurableMockMvcBuilder<?> builder) {
        }

        @Override
        public RequestPostProcessor beforeMockMvcCreated(ConfigurableMockMvcBuilder<?> builder, WebApplicationContext context) {
            return new RequestPostProcessor() {
                @Override
                public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
                    request.setServerName("192.168.1.1");
                    request.setServerPort(8888);
                    return request;
                }
            };
        }
    }
}