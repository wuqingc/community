package com.lele.community.exception;

/**
 * 将异常的通用形式抽离出来,不同类型有不同的ErrorCode类.
 */
public enum CustomizeErrorCode implements ICustomizeErrorCode{

    QUESTION_NOT_FOUND(2001,"你找的问题不在了,要不换个试试?"),
    TARGET_PARAM_NOT_PARAM(2002,"未选择任何问题或评论进行回复"),
    NO_LOGIN(2003,"当前操作需要登录,请登录后重试."),
    SYSTEM_ERROR(2004,"服务端异常,请稍后重试."),
    TYPE_PARAM_NOT_WRONG(2005,"评论类型错误或不存在."),
    COMMENT_NOT_FOUND(2006,"你操作的评论不存在"),
    COMMENT_IS_EMPTY(2007,"你操作的评论不存在")
    ;

    private Integer code;
    private String message;


    CustomizeErrorCode(Integer code,String message) {
        this.message = message;
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public Integer getCode() {
        return code;
    }
}
