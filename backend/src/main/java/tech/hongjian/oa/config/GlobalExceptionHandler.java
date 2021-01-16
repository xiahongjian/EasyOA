package tech.hongjian.oa.config;

import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.extern.slf4j.Slf4j;
import tech.hongjian.oa.model.R;

/**
 * @author xiahongjian
 * @since 2021-01-17 00:05:52
 */
@Slf4j
@Configuration
public class GlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public R globalException(HttpServletResponse response, Exception e) {
        log.info("Exception: ", e);
        return R.error(e.getMessage());
    }
}
