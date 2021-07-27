package com.siemens.ct.task.exception;

public class GeneralException extends  RuntimeException{
    private int code;

    public int getCode() {
        return code;
    }

    public GeneralException setCode(int code) {
        this.code = code;
        return this;
    }

    public GeneralException(String message, int code) {
        super(message);
        this.code = code;
    }

}
