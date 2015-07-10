package com.kentchiu.spring;

import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultHandler;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;

public class ApiBlueprintResultHandler implements ResultHandler {


    private Path document;
    private String identifier = "";
    private Logger logger = LoggerFactory.getLogger(ApiBlueprintResultHandler.class);

    public ApiBlueprintResultHandler(Path document, String identifier) {
        this.document = document;
        this.identifier = identifier;
    }

    public ApiBlueprintResultHandler(Path document) {
        this.document = document;
    }

    public void handle(MvcResult result) throws Exception {
        StringBuilder sb = new StringBuilder();
        String request = getRequest(result);
        sb.append(request);
        if (!StringUtils.isBlank(request)) {
            sb.append("\n");
        }
        sb.append(getResponse(result));
        Files.write(document, sb.toString().getBytes());
        logger.info("snippet file: {}", document);
    }

    private String getRequest(MvcResult result) throws IOException {
        MockHttpServletRequest req = result.getRequest();
        StringBuilder sb = new StringBuilder();
        if (!StringUtils.equals("DELETE", req.getMethod()) && !StringUtils.equals("GET", req.getMethod()) || StringUtils.isNotBlank(identifier)) {
            sb.append("+ Request ");
            if (StringUtils.isNotBlank(identifier)) {
                sb.append(identifier).append(" ");
            }
            sb.append("(").append(req.getContentType()).append(")").append("\n");
            sb.append("\n");
            sb.append(indentJson(IOUtils.toString(req.getInputStream()))).append("\n");
        }
        return sb.toString();
    }

    private String getResponse(MvcResult result) throws UnsupportedEncodingException {
        StringBuilder sb = new StringBuilder();
        MockHttpServletResponse response = result.getResponse();
        response.setCharacterEncoding("UTF-8");
        String content = response.getContentAsString();
        sb.append("+ Response ").append(response.getStatus());
        if (HttpStatus.NO_CONTENT.value() != response.getStatus()) {
            sb.append(" (").append(response.getContentType()).append(")").append("\n");
            sb.append("\n");
            sb.append(indentJson(content));
        }
        return sb.toString();
    }

    private String indentJson(String content) {
        Iterable<String> split = Splitter.on('\n').split(content);
        return ImmutableList.copyOf(split).stream().map(s -> "\t\t\t" + s).collect(Collectors.joining("\n"));
    }

}
