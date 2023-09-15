package com.lsl.gulimall.product.exception;

import com.lsl.common.exception.BizCodeEnum;
import com.lsl.common.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * 异常集中处理
 */
@Slf4j
@RestControllerAdvice(basePackages = "com.lsl.gulimall.product.controller")
public class GulimallExceptionControllerAdvice {

    /**
     * 校验异常集中处理
     * @param validException
     * @return R
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public R handleValidException(MethodArgumentNotValidException validException) {
        BindingResult result = validException.getBindingResult();
        Map<String,String> errorMap = new HashMap<>();
        //获取校验的错误结果
        result.getFieldErrors().forEach((item) -> {
            errorMap.put(item.getField(),item.getDefaultMessage());
        });
        return R.error(BizCodeEnum.VAILD_EXCEPTION.getCode(), BizCodeEnum.VAILD_EXCEPTION.getMsg()).put("data", errorMap);
    }
}
