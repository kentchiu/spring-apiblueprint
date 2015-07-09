package com.kentchiu.spring;

import com.google.common.base.Joiner;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.MvcResult;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

public class CurlResultHandlerTest {

    private CurlResultHandler handler;
    private MvcResult mockResult;
    private MockHttpServletRequest mockRequest;
    private Path book;

    @Test
    public void testListBooks() throws Exception {
        mockRequest.setContentType("application/json");
        mockRequest.setMethod("GET");
        mockRequest.setRequestURI("/books");

        handler.handle(mockResult);
        List<String> lines = Files.readAllLines(book);
        System.out.println(Joiner.on('\n').join(lines));
        assertThat(lines.get(0), is("curl 'http://localhost:8080/books' -H \"Content-Type: application/json\""));
        assertThat(lines.size(), is(1));
    }

    @Test
    public void testGetBook() throws Exception {
        mockRequest.setContentType("application/json");
        mockRequest.setMethod("GET");
        mockRequest.setRequestURI("/books/1");

        handler.handle(mockResult);
        List<String> lines = Files.readAllLines(book);
        System.out.println(Joiner.on('\n').join(lines));
        assertThat(lines.get(0), is("curl 'http://localhost:8080/books/1' -H \"Content-Type: application/json\""));
        assertThat(lines.size(), is(1));
    }


    @Test
    public void testGetBookWithQueryString() throws Exception {
        mockRequest.setContentType("application/json");
        mockRequest.setMethod("GET");
        mockRequest.setRequestURI("/books/1");
        mockRequest.setQueryString("foo=1&bar=2&baz=3&baz=4");

        handler.handle(mockResult);
        List<String> lines = Files.readAllLines(book);
        System.out.println(Joiner.on('\n').join(lines));
        assertThat(lines.get(0), is("curl 'http://localhost:8080/books/1?foo=1&bar=2&baz=3&baz=4' -H \"Content-Type: application/json\""));
        assertThat(lines.size(), is(1));
    }

    @Test
    public void testGetBookWithQueryParameters() throws Exception {
        mockRequest.setContentType("application/json");
        mockRequest.setMethod("GET");
        mockRequest.setRequestURI("/books/1");
        mockRequest.addParameter("foo", "1");
        mockRequest.addParameter("bar", "2");
        mockRequest.addParameter("baz", "3");
        mockRequest.addParameter("baz", "4");

        handler.handle(mockResult);
        List<String> lines = Files.readAllLines(book);
        System.out.println(Joiner.on('\n').join(lines));
        assertThat(lines.get(0), is("curl 'http://localhost:8080/books/1?foo=1&bar=2&baz=3&baz=4' -H \"Content-Type: application/json\""));
        assertThat(lines.size(), is(1));
    }

    @Before
    public void setUp() throws Exception {
        book = Files.createTempFile("book-curl", ".md");
        handler = new CurlResultHandler(book);
        mockResult = Mockito.mock(MvcResult.class);
        mockRequest = new MockHttpServletRequest();
        mockRequest.setServerName("localhost");
        mockRequest.setServerPort(8080);
        when(mockResult.getRequest()).thenReturn(mockRequest);
    }

    @Test
    public void testAddBook() throws Exception {
        mockRequest.setContentType("application/json");
        mockRequest.setMethod("POST");
        mockRequest.setRequestURI("/books");
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("  \"name\": \"DEF\"");
        sb.append("}");
        mockRequest.setContent(sb.toString().getBytes());

        handler.handle(mockResult);
        List<String> lines = Files.readAllLines(book);
        System.out.println(Joiner.on('\n').join(lines));

        assertThat(lines.get(0), is("curl 'http://localhost:8080/books' -X POST -H \"Content-Type: application/json\" -d '{  \"name\": \"DEF\"}'"));
        assertThat(lines.size(), is(1));
    }

    @Test
    public void testUpdateBook() throws Exception {
        mockRequest.setContentType("application/json");
        mockRequest.setMethod("PUT");
        mockRequest.setRequestURI("/books/1");
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("  \"id\": 1,");
        sb.append("  \"name\": \"DEF\"");
        sb.append("}");
        mockRequest.setContent(sb.toString().getBytes());

        handler.handle(mockResult);
        List<String> lines = Files.readAllLines(book);
        System.out.println(Joiner.on('\n').join(lines));

        assertThat(lines.get(0), is("curl 'http://localhost:8080/books/1' -X PUT -H \"Content-Type: application/json\" -d '{  \"id\": 1,  \"name\": \"DEF\"}'"));
        assertThat(lines.size(), is(1));
    }


    @Test
    public void testDeleteBook() throws Exception {
        mockRequest.setContentType("application/json");
        mockRequest.setMethod("DELETE");
        mockRequest.setRequestURI("/books/1");

        handler.handle(mockResult);
        List<String> lines = Files.readAllLines(book);
        System.out.println(Joiner.on('\n').join(lines));

        assertThat(lines.get(0), is("curl 'http://localhost:8080/books/1' -X DELETE -H \"Content-Type: application/json\""));
        assertThat(lines.size(), is(1));
    }
}