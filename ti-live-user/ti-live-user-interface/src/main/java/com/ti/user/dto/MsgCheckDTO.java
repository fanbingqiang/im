package com.ti.user.dto;

import java.io.Serializable;

public class MsgCheckDTO implements Serializable {

    private boolean checkStatus;

    private String message;

    public MsgCheckDTO() {
    }

    public MsgCheckDTO(boolean checkStatus, String message) {
        this.checkStatus = checkStatus;
        this.message = message;
    }

    public boolean isCheckStatus() {
        return checkStatus;
    }

    public void setCheckStatus(boolean checkStatus) {
        this.checkStatus = checkStatus;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "MsgCheckDTO{" +
                "checkStatus=" + checkStatus +
                ", message='" + message + '\'' +
                '}';
    }
}
