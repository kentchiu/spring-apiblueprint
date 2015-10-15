package com.kentchiu.spring.domain;

import com.kentchiu.spring.attribute.AttributeInfo;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name = "test_book_tbl")
public class Book extends MaintainObject {

    private List<Author> authors;
    private String ISBN;
    private Double price;
    private String name;


    @JoinColumn(name = "AUTHOR_UUID")
    @AttributeInfo(description = "作者")
    public List<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(List<Author> authors) {
        this.authors = authors;
    }

    @AttributeInfo(path = "isbn", description = "ISBN編號")
    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    @NotNull
    @AttributeInfo
    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @NotBlank
    @Column(name = "BOOK_NAME")
    @AttributeInfo(path = "bookName")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
