package com.lele.community.advice;

import com.lele.community.exception.CustomizeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class CustomizeExceptionHandler {
    @ExceptionHandler(Exception.class)
    ModelAndView handleControllerException(HttpServletRequest request,
                                           Throwable ex,
                                           Model model) {
        if (ex instanceof CustomizeException){
            model.addAttribute("message", ex.getMessage());
        } else {
            model.addAttribute("message","服务器冒烟了,请稍后重试呢.");
        }

        return new ModelAndView("error");
    }
}
