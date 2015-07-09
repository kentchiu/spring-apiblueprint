package com.kentchiu.spring;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class ApiBlueprintProcessor {

    private Logger logger = LoggerFactory.getLogger(ApiBlueprintProcessor.class);
    private Path snippetHome;
    private List<Path> documents;

    public ApiBlueprintProcessor(Path snippetHome) {
        this.snippetHome = snippetHome;
        documents = Lists.newArrayList();
    }

    private void substitution(Path snippetHome, Path input, Path output) throws IOException {
        Map<String, String> map = loadSnippetHomeToMap(snippetHome);
        List<String> lines2 = substitution(input, map);
        String join = Joiner.on('\n').join(lines2);
        logger.info("process file: {}", output);
        Files.write(output, join.getBytes());
    }

    List<String> substitution(Path input, Map<String, String> map) throws IOException {
        List<String> lines = Files.readAllLines(input);
        int i = 1;
        List<String> lines2 = Lists.newArrayList();
        for (String line : lines) {
            String[] tokens = StringUtils.substringsBetween(line, "{{", "}}");
            if (tokens != null && tokens.length > 0) {
                String str = new String();
                for (String token : tokens) {
                    if (map.containsKey(token)) {
                        logger.trace("replace token @ {}:{} : {} ", input, i, token);
                        str = line.replace("{{" + token + "}}", map.getOrDefault(token, "{{" + token + "}}"));
                    } else {
                        logger.warn("token don't mapping @ {}:{} : {} ", input, i, token);
                        str = line;
                    }
                }
                lines2.add(str);
            } else {
                lines2.add(line);
            }
            i++;
        }
        return lines2;
    }

    Map<String, String> loadSnippetHomeToMap(Path snippetHome) throws IOException {
        Preconditions.checkArgument(Files.isDirectory(snippetHome), "snippet home is not a directory : " + snippetHome.toString());
        Map<String, String> results = Maps.newLinkedHashMap();
        Collection<File> files = FileUtils.listFiles(snippetHome.toFile(), new String[]{"md"}, true);
        for (File file : files) {
            Path relativize = snippetHome.relativize(file.toPath());
            results.put(relativize.toString(), FileUtils.readFileToString(file));
        }
        return results;
    }

    public void appendDocument(Path document) {
        Preconditions.checkArgument(Files.exists(document), "document is not exist : " + document);
        Preconditions.checkArgument(Files.isRegularFile(document), "document is not a regular file : " + document);
        documents.add(document);
    }

    public void generate(Path output) throws Exception {
        Path outputHome = output.getParent();
        Files.createDirectories(outputHome);
        substitution(outputHome);
        List<String> all = append(outputHome);
        logger.info("output file : {}", output);
        Files.write(output, all);
    }

    private List<String> append(Path outputHome) throws IOException {
        List<String> all = Lists.newArrayList();
        for (Path doc : documents) {
            List<String> lines = Files.readAllLines(outputHome.resolve(doc.getFileName()));
            all.addAll(lines);
            all.add("");
        }
        return all;
    }

    private void substitution(Path outputHome) throws IOException {
        for (Path doc : documents) {
            substitution(snippetHome, doc, outputHome.resolve(doc.getFileName()));
        }
    }

}


