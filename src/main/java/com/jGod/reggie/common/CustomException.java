package com.jGod.reggie.common;

public class CustomException extends RuntimeException{
    private String message;
    public CustomException(String message){
        this.message = message;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
