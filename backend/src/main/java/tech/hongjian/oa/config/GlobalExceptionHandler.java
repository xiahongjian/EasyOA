package tech.hongjian.oa.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
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

    @ExceptionHandler(Exception.class)
    public R globalException(HttpServletResponse response, Exception e) {
        log.info(e.getMessage(), e);
        return R.error(e.getMessage());
    }

    @ExceptionHandler(CommonServiceException.class)
    public R serviceException(HttpServletResponse response, CommonServiceException e) {
        log.info(e.getMessage(), e);
        return R.error(e.getCode(), e.getMessage());
    }
}
