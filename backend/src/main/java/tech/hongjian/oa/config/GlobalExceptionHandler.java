package tech.hongjian.oa.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import tech.hongjian.oa.exception.CommonServiceException;
import tech.hongjian.oa.model.R;

import javax.servlet.http.HttpServletResponse;

/**
 * @author xiahongjian
 * @since 2021-01-17 00:05:52
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public R globalException(Exception e) {
        log.info(e.getMessage(), e);
        return R.error(e.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(CommonServiceException.class)
    public R serviceException(CommonServiceException e) {
        log.info(e.getMessage(), e);
        return R.error(e.getCode(), e.getMessage());
    }
}
