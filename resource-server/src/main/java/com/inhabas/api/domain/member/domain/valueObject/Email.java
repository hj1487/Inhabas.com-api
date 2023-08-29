package com.inhabas.api.domain.member.domain.valueObject;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;
import java.util.Objects;
import java.util.regex.Pattern;

@Embeddable
public class Email {

    @Column(name = "EMAIL", length = 150, nullable = false)
    private String value;

    @Transient
    private static final int MAX_LENGTH = 150;

    @Transient
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$");

    public Email() {}

    public Email(String value) {
        if (validate(value))
            this.value = value;
        else
            throw new IllegalArgumentException();
    }

    private boolean validate(Object value) {
        if (Objects.isNull(value)) return false;
        if (!(value instanceof String))  return false;

        String obj = (String) value;
        if (obj.isBlank()) return false;
        return obj.length() < MAX_LENGTH && EMAIL_PATTERN.matcher(obj).matches();
    }

    public String getValue() {
        return value;
    }
}
