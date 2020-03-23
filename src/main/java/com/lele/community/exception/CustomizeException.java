package com.lele.community.exception;

/**
 * @author xuan
 * 自定义异常.
 */
public class CustomizeException extends RuntimeException {
    private String message;

    public CustomizeException(String message) {
        this.message = message;
    }

    /**
     * 直接可以传进来ErrorCode
     * @param errorCode 错误值
     */
    public CustomizeException(ICustomizeErrorCode errorCode) {
        this.message = errorCode.getMessage();
    }

    @Override
    public String getMessage(){
        return message;
    }
}
