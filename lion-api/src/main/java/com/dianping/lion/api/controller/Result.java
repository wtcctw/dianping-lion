package com.dianping.lion.api.controller;

public class Result {

    private static final String STATUS_ERROR = "error";
    private static final String STATUS_SUCCESS = "success";
    
    private String status;
    private String message;
    private Object result;

    public Result(String status) {
        this.status = status;
    }
    
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
    
    public static Result createSuccessResult(Object result) {
        Result rr = new Result(STATUS_SUCCESS);
        rr.setResult(result);
        return rr;
    }
    
    public static Result createErrorResult(String message) {
        Result rr = new Result(STATUS_ERROR);
        rr.setMessage(message);
        return rr;
    }
}
