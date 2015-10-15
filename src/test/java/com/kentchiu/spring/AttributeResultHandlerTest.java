package com.kentchiu.spring;

import com.kentchiu.spring.domain.Author;
import com.kentchiu.spring.domain.Book;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class AttributeResultHandlerTest {


    @Test
    public void testBook_showColumn() throws Exception {
        AttributeResultHandler handler = new AttributeResultHandler(null, true);
        List<String> lines = handler.attributeTable(Book.class);

        lines.stream().forEach(System.out::println);

        assertThat(lines.get(0), is(" Field      | Required | Type      | default | Format              | Column                    | Description "));
        assertThat(lines.get(1), is("------------|----------|-----------|---------|---------------------|---------------------------|-------------"));
        assertThat(lines.get(2), is(" isbn       |          | string    |         |                     |                           | ISBN編號      "));
        assertThat(lines.get(3), is(" authors    |          | array     |         |                     | test_book_tbl.AUTHOR_UUID | 作者          "));
        assertThat(lines.get(4), is(" confirm    |          | character |         |                     |                           |             "));
        assertThat(lines.get(5), is(" createDate |          | date      | now     | yyyy/MM/dd HH:mm:ss |                           | 资料创建日期      "));
        assertThat(lines.get(6), is(" createUser |          | string    |         |                     |                           | 资料创建者       "));
        assertThat(lines.get(7), is(" modiDate   |          | date      |         |                     |                           |             "));
        assertThat(lines.get(8), is(" modiUser   |          | string    |         |                     |                           |             "));
        assertThat(lines.get(9), is(" bookName   | *        | string    |         |                     | test_book_tbl.BOOK_NAME   |             "));
        assertThat(lines.get(10), is(" price      | *        | number    |         |                     |                           |             "));
        assertThat(lines.get(11), is(" status     |          | character |         |                     |                           |             "));
        assertThat(lines.get(12), is(" uuid       |          | string    |         |                     |                           |             "));

    }


    @Test
    public void testBook() throws Exception {
        AttributeResultHandler handler = new AttributeResultHandler(null);
        List<String> lines = handler.attributeTable(Book.class);

        assertThat(lines.get(0), is(" Field      | Required | Type      | default | Format              | Description "));
        assertThat(lines.get(1), is("------------|----------|-----------|---------|---------------------|-------------"));
        assertThat(lines.get(2), is(" isbn       |          | string    |         |                     | ISBN編號      "));
        assertThat(lines.get(3), is(" authors    |          | array     |         |                     | 作者          "));
        assertThat(lines.get(4), is(" confirm    |          | character |         |                     |             "));
        assertThat(lines.get(5), is(" createDate |          | date      | now     | yyyy/MM/dd HH:mm:ss | 资料创建日期      "));
        assertThat(lines.get(6), is(" createUser |          | string    |         |                     | 资料创建者       "));
        assertThat(lines.get(7), is(" modiDate   |          | date      |         |                     |             "));
        assertThat(lines.get(8), is(" modiUser   |          | string    |         |                     |             "));
        assertThat(lines.get(9), is(" bookName   | *        | string    |         |                     |             "));
        assertThat(lines.get(10), is(" price      | *        | number    |         |                     |             "));
        assertThat(lines.get(11), is(" status     |          | character |         |                     |             "));
        assertThat(lines.get(12), is(" uuid       |          | string    |         |                     |             "));
    }


    @Test
    public void testAuthor() throws Exception {
        AttributeResultHandler handler = new AttributeResultHandler(null);
        List<String> lines = handler.attributeTable(Author.class);
        assertThat(lines.get(0), is(" Field      | Required | Type      | default | Format                | Description "));
        assertThat(lines.get(1), is("------------|----------|-----------|---------|-----------------------|-------------"));
        assertThat(lines.get(2), is(" confirm    |          | character |         |                       |             "));
        assertThat(lines.get(3), is(" createDate |          | date      | now     | yyyy/MM/dd HH:mm:ss   | 资料创建日期      "));
        assertThat(lines.get(4), is(" createUser |          | string    |         |                       | 资料创建者       "));
        assertThat(lines.get(5), is(" gender     |          | enum      |         | MALE / FEMALE / OTHER | 性別          "));
        assertThat(lines.get(6), is(" modiDate   |          | date      |         |                       |             "));
        assertThat(lines.get(7), is(" modiUser   |          | string    |         |                       |             "));
        assertThat(lines.get(8), is(" name       |          | string    |         |                       |             "));
        assertThat(lines.get(9), is(" status     |          | character |         |                       |             "));
        assertThat(lines.get(10), is(" uuid       |          | string    |         |                       |             "));
    }

}