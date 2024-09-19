package org.shopping.common;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NameAlreadyExistsException extends RuntimeException {
    public NameAlreadyExistsException(String field) {
        super(field + " is already existed!");
    }
}