package com.kentchiu.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultHandler;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Enumeration;
import java.util.Map;

public class CurlResultHandler implements ResultHandler {

    private Logger logger = LoggerFactory.getLogger(CurlResultHandler.class);
    private Path document;
    private int port;

    public CurlResultHandler(Path document, int port) {
        this.document = document;
        this.port = port;
    }

    public CurlResultHandler(Path document) {
        this(document, 8080);
    }

    private static String toQueryString(Map<String, String[]> map) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String[]> entry : map.entrySet()) {
            for (String value : entry.getValue()) {
                if (sb.length() > 0) {
                    sb.append("&");
                }
                sb.append(urlEncodeUTF8(entry.getKey())).append('=')
                        .append(urlEncodeUTF8(value));
            }
        }
        return sb.toString();
    }

    private static String urlEncodeUTF8(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            throw new IllegalStateException("Unable to URL encode " + s
                    + " using UTF-8", ex);
        }
    }

    @Override
    public void handle(MvcResult result) throws Exception {
        MockHttpServletRequest request = result.getRequest();

        StringBuilder sb = new StringBuilder();
        sb.append(String.format("curl '%s://%s", request.getScheme(),
                request.getServerName()));


        sb.append(String.format(":%d", port));

        sb.append(getRequestUriWithQueryString(request));

        sb.append("'");
        if (!isGetRequest(request)) {
            sb.append(String.format(" -X %s", request.getMethod()));
        }


        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            Enumeration<String> headers = request.getHeaders(headerName);
            while (headers.hasMoreElements()) {
                String header = headers.nextElement();
                sb.append(String.format(" -H \"%s: %s\"", headerName, header));
            }

        }

        if (request.getContentLengthLong() > 0) {
            sb.append(String.format(" -d '%s'", getContent(request)));
        } else if (isPostRequest(request)) {
            Map<String, String[]> parameters = request.getParameterMap();
            if (!parameters.isEmpty()) {
                sb.append(String.format(" -d '%s'", toQueryString(parameters)));
            }
        }

        Files.write(document, sb.toString().getBytes());
        logger.info("snippet file: {}", document);
    }

    private boolean isGetRequest(HttpServletRequest request) {
        return RequestMethod.GET == RequestMethod.valueOf(request.getMethod());
    }

    private boolean isPostRequest(HttpServletRequest request) {
        return RequestMethod.POST == RequestMethod.valueOf(request.getMethod());
    }

    private String getRequestUriWithQueryString(HttpServletRequest request) {
        StringBuilder sb = new StringBuilder();
        sb.append(request.getRequestURI());
        String queryString = getQueryString(request);
        if (StringUtils.hasText(queryString)) {
            sb.append('?').append(queryString);
        }
        return sb.toString();
    }

    private String getQueryString(HttpServletRequest request) {
        if (request.getQueryString() != null) {
            return request.getQueryString();
        }
        if (isGetRequest(request)) {
            return toQueryString(request.getParameterMap());
        }
        return null;
    }

    private String getContent(MockHttpServletRequest request) throws IOException {
        StringWriter bodyWriter = new StringWriter();
        FileCopyUtils.copy(request.getReader(), bodyWriter);
        return bodyWriter.toString();
    }
}
