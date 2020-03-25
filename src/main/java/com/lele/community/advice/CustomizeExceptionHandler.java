package com.lele.community.advice;

import com.alibaba.fastjson.JSON;
import com.lele.community.dto.ResultDTO;
import com.lele.community.exception.CustomizeErrorCode;
import com.lele.community.exception.CustomizeException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author xuan
 * 处理异常的统一操作.
 */
@ControllerAdvice
public class CustomizeExceptionHandler {
    @ExceptionHandler(Exception.class)
    Object handleControllerException(HttpServletRequest request,
                                     HttpServletResponse response,
                                           Throwable ex,
                                           Model model) throws IOException {
        /*
         * 需要区分请求的格式.
         */
        String contentType = request.getContentType();
        if ("application/json".equals(contentType)) {
            ResultDTO resultDTO;
            if (ex instanceof CustomizeException){
                resultDTO =  ResultDTO.errorOf((CustomizeException) ex);
            } else {
                resultDTO = ResultDTO.errorOf(CustomizeErrorCode.SYSTEM_ERROR);
            }
            /*
             * 手写功能,经典.
             */
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.setStatus(200);
            PrintWriter printWriter = response.getWriter();
            printWriter.write(JSON.toJSONString(resultDTO));
            return null;
        } else {
            if (ex instanceof CustomizeException){
                model.addAttribute("message", ex.getMessage());
            } else {
                model.addAttribute("message",CustomizeErrorCode.SYSTEM_ERROR.getMessage());
            }
            return new ModelAndView("error");
        }

    }
}
