package tech.hongjian.oa.service;

import tech.hongjian.oa.entity.User;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xiahongjian
 * @since 2020-03-17
 */
public interface UserService extends IService<User>, UserDetailsService {

}
