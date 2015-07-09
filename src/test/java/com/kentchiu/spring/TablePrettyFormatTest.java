package com.kentchiu.spring;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class TablePrettyFormatTest {


    @Test
    public void testPrettyOutput() throws Exception {
        TablePrettyFormat fmt = new TablePrettyFormat(input());
        List<String> lines = fmt.prettyOutput();
        int i = 0;
        assertThat(lines.get(i++), is(" Field             | Required | Type   | default | Format            | Description "));
        assertThat(lines.get(i++), is("-------------------|----------|--------|---------|-------------------|-------------"));
        assertThat(lines.get(i++), is(" catalogueTemplate |          | object |         |                   | 模板_uuid     "));
        assertThat(lines.get(i++), is(" children          |          | array  |         |                   |             "));
        assertThat(lines.get(i++), is(" confirm           | *        | string | 1       | 1=未审核/2=已审核/3=已发布 | 审核码         "));
        assertThat(lines.get(i++), is(" createDate        |          | date   |         |                   | 创建日期        "));
        assertThat(lines.get(i++), is(" createUserUuid    |          | string |         |                   | 创建用户 uuid   "));
        assertThat(lines.get(i++), is(" modiDate          |          | date   |         |                   | 修改日期        "));
        assertThat(lines.get(i++), is(" modiUserUuid      |          | string |         |                   | 修改用户 uuid   "));
        assertThat(lines.get(i++), is(" name              | *        | string |         |                   | 目录名称        "));
        assertThat(lines.get(i++), is(" no                | *        | string |         |                   | 目录编号        "));
        assertThat(lines.get(i++), is(" parent            |          | object |         |                   |             "));
        assertThat(lines.get(i++), is(" parentUuid        |          | string |         |                   | 上层目录_uuid   "));
        assertThat(lines.get(i++), is(" status            | *        | string | 1       | 1=有效/2=无效/3=作废    | 状态码         "));
        assertThat(lines.get(i++), is(" terminalIp        |          | string |         |                   | 终端IP        "));
        assertThat(lines.get(i++), is(" terminalMac       |          | string |         |                   | 终端MAC       "));
        assertThat(lines.get(i++), is(" terminalType      |          | string |         |                   | 终端类型        "));
        assertThat(lines.get(i++), is(" uuid              |          | string |         |                   | uuid        "));
    }


    private List<String> input() {
        List<String> lines = new ArrayList<>();
        lines.add(" Field | Required | Type | default | Format | Description ");
        lines.add("-------|----------|------|---------|--------|-------------");
        lines.add("catalogueTemplate||object|||模板_uuid");
        lines.add("children||array|||");
        lines.add("confirm|*|string|1|1=未审核/2=已审核/3=已发布|审核码");
        lines.add("createDate||date|||创建日期");
        lines.add("createUserUuid||string|||创建用户 uuid");
        lines.add("modiDate||date|||修改日期");
        lines.add("modiUserUuid||string|||修改用户 uuid");
        lines.add("name|*|string|||目录名称");
        lines.add("no|*|string|||目录编号");
        lines.add("parent||object|||");
        lines.add("parentUuid||string|||上层目录_uuid");
        lines.add("status|*|string|1|1=有效/2=无效/3=作废|状态码");
        lines.add("terminalIp||string|||终端IP");
        lines.add("terminalMac||string|||终端MAC");
        lines.add("terminalType||string|||终端类型");
        lines.add("uuid||string|||uuid");
        return lines;
    }


    @Test
    public void testLineToLengths() throws Exception {
        TablePrettyFormat.LineToLengthsFunction function = new TablePrettyFormat.LineToLengthsFunction();
        assertThat(function.apply(" Field | Required | Type | default | Format | Description "), hasItems(7, 10, 6, 9, 8, 13));
        assertThat(function.apply("-------|----------|------|---------|--------|-------------"), hasItems(7, 10, 6, 9, 8, 13));
        assertThat(function.apply("catalogueTemplate||object|||模板_uuid"), hasItems(19, 0, 8, 0, 0, 9));
        assertThat(function.apply("confirm|*|string|1|1=未审核/2=已审核/3=已发布|审核码"), hasItems(9, 3, 8, 3, 19, 5));
    }


    @Test
    public void testCellLength() throws Exception {
        TablePrettyFormat fmt = new TablePrettyFormat(input());
        fmt.prettyOutput();
        assertThat(fmt.getCellLengths(), hasItems(19, 10, 8, 9, 19, 13));
    }
}


