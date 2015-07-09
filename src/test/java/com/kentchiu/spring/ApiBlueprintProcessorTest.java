package com.kentchiu.spring;


import com.google.common.collect.ImmutableMap;
import org.junit.Before;
import org.junit.Test;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class ApiBlueprintProcessorTest {

    private Path documentHome;
    private Path output;
    private ApiBlueprintProcessor processor;
    private Path snippetHome;

    @Before
    public void setUp() throws Exception {
        URI uri = this.getClass().getResource("/").toURI();
        Path targetDir = Paths.get(uri);
        documentHome = targetDir.resolve("document");
        output = targetDir.resolve("api.md");
        snippetHome = targetDir.resolve("snippet");
        processor = new ApiBlueprintProcessor(snippetHome);
    }

    @Test
    public void testGenerate() throws Exception {
        processor.appendDocument(documentHome.resolve("preface.md"));
        processor.appendDocument(documentHome.resolve("brand.md"));

        processor.generate(output);

        assertThat(Files.exists(output), is(true));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAppendDocument_append_folder_should_throw_exception() throws Exception {
        processor.appendDocument(documentHome);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAppendDocument_append_not_exist_field_should_throw_exception() throws Exception {
        processor.appendDocument(Paths.get("a_not_exist_file"));
    }

    @Test
    public void testMap() throws Exception {
        Map<String, String> map = processor.loadSnippetHomeToMap(snippetHome);
        assertThat(map, hasKey("BrandControllerTest/Brand.md"));
        assertThat(map.get("BrandControllerTest/Brand.md"), containsString("Field"));
    }

    @Test
    public void testSubstitution() throws Exception {
        Path testFile = documentHome.resolve("for_substitution_test.text");
        List<String> lines = processor.substitution(testFile, ImmutableMap.of("foo", "bar"));
        assertThat(lines, hasItem("bar"));
    }
}