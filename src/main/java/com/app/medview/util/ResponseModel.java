package com.app.medview.util;

public class ResponseModel {
    private boolean success;
    private Object data;
    private String message;
    private int status;

    public ResponseModel() {
    }

    public ResponseModel(boolean success, Object data, String message, int status) {
        this.success = success;
        this.data = data;
        this.message = message;
        this.status = status;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
