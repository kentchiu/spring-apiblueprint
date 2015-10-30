package com.kentchiu.spring.domain;

import com.kentchiu.spring.attribute.AttributeInfo;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "test_publisher_tbl")
public class Publisher extends MaintainObject {

    private String name;

    @NotBlank
    @AttributeInfo(path = "name",description = "出版商名稱")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
