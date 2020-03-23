package com.lele.community.exception;

/**
 * 将异常的通用形式抽离出来,不同类型有不同的ErrorCode类.
 */
public enum CustomizeErrorCode implements ICustomizeErrorCode{

    QUESTION_NOT_FOUND("你找的问题不在了,要不换个试试?");

    private String message;

    CustomizeErrorCode(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
