package tech.hongjian.oa.config;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import com.baomidou.mybatisplus.extension.plugins.OptimisticLockerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;

import tech.hongjian.oa.util.JSONUtil;

/**
 * @author xiahongjian
 * @since 2020-03-16 21:56:16
 */
@Configuration
@MapperScan(basePackages = "tech.hongjian.oa.mapper")
public class MybatisConfig {

    @Bean
    @Primary
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return builder -> builder
                .serializerByType(LocalDateTime.class,
                        new LocalDateTimeSerializer(
                                DateTimeFormatter.ofPattern(JSONUtil.DATE_TIME_FORMAT)))
                .serializerByType(LocalDate.class,
                        new LocalDateSerializer(DateTimeFormatter.ofPattern(JSONUtil.DATE_FORMAT)))
                .serializerByType(LocalTime.class,
                        new LocalTimeSerializer(DateTimeFormatter.ofPattern(JSONUtil.TIME_FORMAT)))
                .deserializerByType(LocalDateTime.class,
                        new LocalDateTimeDeserializer(
                                DateTimeFormatter.ofPattern(JSONUtil.DATE_TIME_FORMAT)))
                .deserializerByType(LocalDate.class,
                        new LocalDateDeserializer(
                                DateTimeFormatter.ofPattern(JSONUtil.DATE_FORMAT)))
                .deserializerByType(LocalTime.class, new LocalTimeDeserializer(
                        DateTimeFormatter.ofPattern(JSONUtil.TIME_FORMAT)));
    }

    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }

    @Bean
    public OptimisticLockerInterceptor optimisticLockerInterceptor() {
        return  new OptimisticLockerInterceptor();
    }
}
