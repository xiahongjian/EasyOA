package tech.hongjian.oa.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.baomidou.mybatisplus.extension.service.IService;

import tech.hongjian.oa.entity.User;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xiahongjian
 * @since 2021-01-12
 */
public interface UserService extends IService<User>, UserDetailsService {

}
