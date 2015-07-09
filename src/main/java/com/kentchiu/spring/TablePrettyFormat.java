package com.kentchiu.spring;

import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

class TablePrettyFormat {

    private List<String> lines;
    private List<Integer> cellLengths = new ArrayList<>();

    public TablePrettyFormat(List<String> lines) {
        this.lines = lines;
    }

    protected List<Integer> getCellLengths() {
        return cellLengths;
    }

    public List<String> prettyOutput() {
        scanTable();

        return lines.stream().map(line -> {
            Iterable<String> split = Splitter.on('|').trimResults().split(line);
            ArrayList<String> strings = Lists.newArrayList(split);
            StringBuilder sb = new StringBuilder();
            int i = 0;
            for (String cell : strings) {
                boolean isDivider = StringUtils.startsWith(cell, "--");
                if (isDivider) {
                    // divider (a.k.a ---------|------|--------)
                    sb.append(StringUtils.rightPad(cell, cellLengths.get(i++), "-"));
                    if (i < cellLengths.size()) {
                        sb.append("|");
                    }
                } else {
                    // header and row
                    sb.append(" ").append(StringUtils.rightPad(cell, cellLengths.get(i++) - 2)).append(" ");
                    if (i < cellLengths.size()) {
                        sb.append("|");
                    }
                }
            }
            return sb.toString();
        }).collect(Collectors.toList());
    }

    private void scanTable() {
        lines.stream().map(new LineToLengthsFunction()).forEach(lens -> {
            if (cellLengths.isEmpty()) {
                cellLengths = lens;
            } else {
                Preconditions.checkState(cellLengths.size() == lens.size(), "cell count should be " + cellLengths.size() + " but was " + lens);
                // using max length
                for (int i = 0; i < cellLengths.size(); i++) {
                    Integer value = cellLengths.get(i);
                    Integer newValue = lens.get(i);
                    if (newValue > value) {
                        cellLengths.set(i, newValue);
                    }
                }
            }

        });
    }


    /**
     * convert cell content to length
     */
    static class LineToLengthsFunction implements Function<String, List<Integer>> {
        @Override
        public List<Integer> apply(String line) {
            Iterable<String> split = Splitter.on('|').trimResults().split(line);
            List<String> cellContents = Lists.newArrayList(split);
            return cellContents.stream().map(c -> {
                boolean isDivider = StringUtils.startsWith(c, "----");
                if (isDivider || StringUtils.isBlank(c)) {
                    return StringUtils.length(c);
                } else {
                    return StringUtils.length(c) + 2;
                }
            }).collect(Collectors.toList());
        }
    }
}
