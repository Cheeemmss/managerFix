package com.wl.Exception;

import com.wl.common.Result;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.net.PortUnreachableException;

import static com.wl.common.Constants.CODE_403;

@ControllerAdvice
public class GlExceptionHandler {

    @ExceptionHandler(ServiceException.class)
    @ResponseBody
    public Result handle(ServiceException ex){
        return  Result.error(ex.getCode(), ex.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseBody
    public Result handleAccessDeniedException(AccessDeniedException e){
        return Result.error(CODE_403,"您无此操作权限");
    }

}
