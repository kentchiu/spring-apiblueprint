package com.kentchiu.spring;

import com.google.common.base.Joiner;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MvcResult;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

public class ApiBlueprintResultHandlerTest {

    private ApiBlueprintResultHandler handler;
    private MvcResult mockResult;
    private MockHttpServletRequest mockRequest;
    private MockHttpServletResponse mockResponse;
    private Path book;

    @Test
    public void testGetBook() throws Exception {
        mockRequest.setContentType("application/json");
        mockRequest.setMethod("GET");

        mockResponse.setContentType("application/json");
        mockResponse.getWriter().println("{");
        mockResponse.getWriter().println("  \"id\": 4,");
        mockResponse.getWriter().println("  \"name\": \"ABC\"");
        mockResponse.getWriter().println("}");


        handler.handle(mockResult);
        List<String> lines = Files.readAllLines(book);
        System.out.println(Joiner.on('\n').join(lines));
        assertThat(lines.get(0), is("+ Response 200 (application/json)"));
        assertThat(lines.get(1), is(""));
        assertThat(lines.get(2), is("\t\t\t{"));
        assertThat(lines.get(3), is("\t\t\t  \"id\": 4,"));
        assertThat(lines.get(4), is("\t\t\t  \"name\": \"ABC\""));
        assertThat(lines.get(5), is("\t\t\t}"));
        assertThat(lines.size(), is(7));

    }


    @Before
    public void setUp() throws Exception {
        book = Files.createTempFile("book", ".md");
        handler = new ApiBlueprintResultHandler(book);
        mockResult = Mockito.mock(MvcResult.class);
        mockRequest = new MockHttpServletRequest();
        when(mockResult.getRequest()).thenReturn(mockRequest);
        mockResponse = new MockHttpServletResponse();
        when(mockResult.getResponse()).thenReturn(mockResponse);
    }

    @Test
    public void testUpdateBook() throws Exception {
        mockRequest.setContentType("application/json");
        mockRequest.setMethod("PUT");
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("  \"id\": 1,");
        sb.append("  \"name\": \"DEF\"");
        sb.append("}");
        mockRequest.setContent(sb.toString().getBytes());

        mockResponse.setContentType("application/json");
        mockResponse.getWriter().println("{");
        mockResponse.getWriter().println("  \"id\": 4,");
        mockResponse.getWriter().println("  \"name\": \"ABC\"");
        mockResponse.getWriter().println("}");


        handler.handle(mockResult);
        List<String> lines = Files.readAllLines(book);
        System.out.println(Joiner.on('\n').join(lines));

        assertThat(lines.get(0), is("+ Request (application/json)"));
        assertThat(lines.get(1), is(""));
        assertThat(lines.get(2), is("\t\t\t{  \"id\": 1,  \"name\": \"DEF\"}"));
        assertThat(lines.get(3), is(""));

        int start = 4;
        assertThat(lines.get(start + 0), is("+ Response 200 (application/json)"));
        assertThat(lines.get(start + 1), is(""));
        assertThat(lines.get(start + 2), is("\t\t\t{"));
        assertThat(lines.get(start + 3), is("\t\t\t  \"id\": 4,"));
        assertThat(lines.get(start + 4), is("\t\t\t  \"name\": \"ABC\""));
        assertThat(lines.get(start + 5), is("\t\t\t}"));
        assertThat(lines.size(), is(11));
    }


    @Test
    public void testDeleteBook() throws Exception {
        mockRequest.setContentType("application/json");
        mockRequest.setMethod("DELETE");

        mockResponse.setContentType("application/json");
        mockResponse.setStatus(204);

        handler.handle(mockResult);
        List<String> lines = Files.readAllLines(book);
        System.out.println(Joiner.on('\n').join(lines));

        assertThat(lines.get(0), is("+ Response 204"));
        assertThat(lines.size(), is(1));
    }
}