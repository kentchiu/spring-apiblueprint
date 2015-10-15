package com.kentchiu.spring.attribute;

import org.apache.commons.lang3.StringUtils;

public class Attribute {

    private String path;
    private String type = "";
    private boolean required;
    private String description = "";
    private String format = "";
    private String defaultValue = "";
    private boolean ignore;
    private String column = "";

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public boolean isIgnore() {
        return ignore;
    }

    public void setIgnore(boolean ignore) {
        this.ignore = ignore;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(path);
        if (!required) {
            sb.append("*");
        }
        if (StringUtils.isNoneBlank(defaultValue)) {
            sb.append(" = ").append(defaultValue);
        }
        sb.append(" (").append(type);
        if (StringUtils.isNotBlank(format)) {
            sb.append(" ").append(format);
        }
        sb.append(")");
        sb.append(" : ").append(description);
        return sb.toString();
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public Attribute setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }

    public String getFormat() {
        return format;
    }

    public Attribute setFormat(String format) {
        this.format = format;
        return this;
    }

    public String getPath() {
        return path;
    }

    public Attribute setPath(String path) {
        this.path = path;
        return this;
    }

    public String getType() {
        return type;
    }

    public Attribute setType(String type) {
        this.type = type;
        return this;
    }

    public boolean isRequired() {
        return required;
    }

    public Attribute setRequired(boolean required) {
        this.required = required;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
