package com.kentchiu.spring;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.kentchiu.spring.attribute.Attribute;
import com.kentchiu.spring.attribute.AttributeInfo;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.NotBlank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.core.MethodParameter;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultHandler;
import org.springframework.web.method.HandlerMethod;

import javax.validation.constraints.NotNull;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class AttributeResultHandler implements ResultHandler {

    private Path documentHome;
    private Logger logger = LoggerFactory.getLogger(AttributeResultHandler.class);

    public AttributeResultHandler(Path documentHome) {
        this.documentHome = documentHome;
    }

    @Override
    public void handle(MvcResult result) throws Exception {
        HandlerMethod handler = (HandlerMethod) result.getHandler();
        if (handler == null) {
            throw new RuntimeException("no handler method exist! please check the end point of this resource");
        }
        Class<?> responseClass = handler.getReturnType().getMethod().getReturnType();
        writeToFile(responseClass);
        MethodParameter[] methodParameters = handler.getMethodParameters();
        for (MethodParameter methodParameter : methodParameters) {
            writeToFile(methodParameter.getParameterType());
        }
    }

    public void writeToFile(Class<?> responseClass) throws IOException {
        List<String> strings = attributeTable(responseClass);
        Path responseDocument = documentHome.resolve(responseClass.getSimpleName() + ".md");
        if (!strings.isEmpty()) {
            String join = Joiner.on('\n').join(strings);
            Files.write(responseDocument, join.getBytes());
        }
        logger.info("snippet file: {}", responseDocument);
    }

    public List<String> attributeTable(Class<?> responseClass) {
        if (ClassUtils.isPrimitiveOrWrapper(responseClass) || Date.class.isAssignableFrom(responseClass) || String.class.isAssignableFrom(responseClass)) {
            return Lists.newArrayList();
        }

        PropertyDescriptor[] pds = BeanUtils.getPropertyDescriptors(responseClass);
        List<Attribute> attributes = Lists.newArrayList();

        for (PropertyDescriptor pd : pds) {
            if (pd.getReadMethod() != null && pd.getReadMethod().isAnnotationPresent(AttributeInfo.class)) {
                Attribute attribute = createAttribute(responseClass, pd);
                attributes.add(attribute);
            } else {
                logger.info("AttributeInfo is not present, ignore the property : {}", pd.getName());
            }
        }

        List<Attribute> collect = attributes.stream()
                .filter(a -> !a.isIgnore())
                .filter(a -> !ArrayUtils.contains(new String[]{"class"}, a.getPath()))
                .collect(Collectors.toList());

        return toTable(collect);
    }

    private Attribute createAttribute(Class<?> responseClass, PropertyDescriptor pd) {
        String propertyName = pd.getName();

        AttributeInfo.Type type = AttributeInfo.Type.valueOf(pd.getPropertyType());
        Attribute attribute = new Attribute();
        attribute.setPath(propertyName);
        attribute.setType(type.name());

        try {
            boolean required = isRequired(responseClass, pd, propertyName);
            attribute.setRequired(required);
        } catch (Exception e) {
            attribute.setRequired(false);
        }

        Optional<AttributeInfo> infoOptional = findAttributeInfo(responseClass, pd, propertyName);

        if (infoOptional.isPresent()) {
            AttributeInfo info = infoOptional.get();
            attribute.setDefaultValue(info.defaultValue());
            attribute.setFormat(info.format());
            attribute.setDescription(info.description());
            attribute.setIgnore(info.ignore());

            if (StringUtils.isNotBlank(info.path())) {
                attribute.setPath(info.path());
            }
            if (AttributeInfo.Type.UNKOWN != info.type()) {
                attribute.setPath(info.type().name());
            }
        }
        return attribute;
    }

    private List<String> toTable(List<Attribute> collect) {
        List<String> results = Lists.newArrayList();
        results.add(" Field | Required | Type | default | Format | Description ");
        results.add("-------|----------|------|---------|--------|-------------");
        collect.forEach(a -> {
            StringBuffer sb = new StringBuffer();
            sb.append(a.getPath()).append("|");
            sb.append(a.isRequired() ? "*" : "").append("|");
            sb.append(a.getType().toLowerCase()).append("|");
            sb.append(a.getDefaultValue()).append("|");
            sb.append(a.getFormat()).append("|");
            sb.append(a.getDescription());
            results.add(sb.toString());
        });
        return new TablePrettyFormat(results).prettyOutput();
    }

    private Optional<AttributeInfo> findAttributeInfo(Class<?> responseClass, PropertyDescriptor pd, String propertyName) {
        Optional<AttributeInfo> infoOptional = Optional.empty();
        try {
            Field field = responseClass.getField(propertyName);
            infoOptional = Optional.ofNullable(field.getAnnotation(AttributeInfo.class));
        } catch (NoSuchFieldException e) {

        }

        if (!infoOptional.isPresent()) {
            try {
                Field declaredField = responseClass.getDeclaredField(propertyName);
                infoOptional = Optional.ofNullable(declaredField.getAnnotation(AttributeInfo.class));
            } catch (NoSuchFieldException e) {

            }
        }
        if (!infoOptional.isPresent()) {
            infoOptional = Optional.ofNullable(pd.getReadMethod().getAnnotation(AttributeInfo.class));
        }
        return infoOptional;
    }

    private boolean isRequired(Class<?> responseClass, PropertyDescriptor pd, String propertyName) {
        boolean required = false;
        try {
            Field field = responseClass.getField(propertyName);
            if (field.isAnnotationPresent(NotNull.class)) {
                required = true;
            }
            if (field.isAnnotationPresent(NotBlank.class)) {
                required = true;
            }
        } catch (NoSuchFieldException e) {

        }

        try {
            Field declaredField = responseClass.getDeclaredField(propertyName);
            if (declaredField.isAnnotationPresent(NotNull.class)) {
                required = true;
            }
            if (declaredField.isAnnotationPresent(NotBlank.class)) {
                required = true;
            }
        } catch (NoSuchFieldException e) {

        }

        if (pd.getReadMethod().isAnnotationPresent(NotNull.class)) {
            required = true;
        }
        if (pd.getReadMethod().isAnnotationPresent(NotBlank.class)) {
            required = true;
        }

        return required;
    }
}

