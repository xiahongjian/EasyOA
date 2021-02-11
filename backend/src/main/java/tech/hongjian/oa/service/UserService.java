package tech.hongjian.oa.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.baomidou.mybatisplus.extension.service.IService;

import tech.hongjian.oa.entity.User;
import tech.hongjian.oa.entity.enums.Status;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xiahongjian
 * @since 2021-01-12
 */
public interface UserService extends IService<User>, UserDetailsService {

    IPage<User> listUser(String keyword, Status status, Integer dept, Integer page, Integer limit);
}
