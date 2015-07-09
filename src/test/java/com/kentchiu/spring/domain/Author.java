package com.kentchiu.spring.domain;

import com.kentchiu.spring.attribute.AttributeInfo;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

enum Gender {
    MALE, FEMALE, OTHER
}

@Entity
@Table(name = "test_author_tbl")
public class Author extends MaintainObject {

    private String name;
    private Gender gender;

    @AttributeInfo
    public String getName() {
        return name;
    }

    public Author setName(String name) {
        this.name = name;
        return this;
    }

    @Enumerated(EnumType.STRING)
    @AttributeInfo(description = "性別", format = "MALE / FEMALE / OTHER")
    public Gender getGender() {
        return gender;
    }

    public Author setGender(Gender gender) {
        this.gender = gender;
        return this;
    }
}