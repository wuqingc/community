package com.lele.community.dto;

import com.lele.community.exception.CustomizeErrorCode;
import com.lele.community.exception.CustomizeException;
import lombok.Data;

@Data
public class ResultDTO<T> {
    private Integer code;
    private String message;
    private T data;

    /**
     * 创建一个静态方法,便于传递.
     * @param code
     * @param message
     * @return
     */
    public static ResultDTO errorOf(Integer code,String message){
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setCode(code);
        resultDTO.setMessage(message);
        return resultDTO;
    }

    public static ResultDTO errorOf(CustomizeErrorCode noLogin) {
        return  errorOf(noLogin.getCode(),noLogin.getMessage());
    }
    public static ResultDTO errorOf(CustomizeException ex) {
        return  errorOf(ex.getCode(),ex.getMessage());
    }

    public static ResultDTO okOf(){
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setCode(200);
        resultDTO.setMessage("请求成功");
        return resultDTO;
    }

    public static <T> ResultDTO okOf(T t){
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setCode(200);
        resultDTO.setMessage("请求成功");
        resultDTO.setData(t);
        return resultDTO;
    }


}
