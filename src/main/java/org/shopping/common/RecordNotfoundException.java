package org.shopping.common;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class RecordNotfoundException extends RuntimeException{
    public RecordNotfoundException(String exception){
        super(exception + " not found!!!");
    }
}
