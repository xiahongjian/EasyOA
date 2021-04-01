package tech.hongjian.oa.config;

import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.FilterInvocation;
import org.springframework.stereotype.Component;
import tech.hongjian.oa.config.ConfigConsts.URLs;
import tech.hongjian.oa.entity.Menu;
import tech.hongjian.oa.entity.Role;
import tech.hongjian.oa.service.MenuService;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author xiahongjian
 * @since 2020-03-18 20:09:42
 */
@Setter(onMethod_ = {@Autowired})
@Component
public class ResourceBasedDecisionManager implements AccessDecisionManager {
    private MenuService menuService;

    @Override
    public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes)
            throws AccessDeniedException, InsufficientAuthenticationException {
        HttpServletRequest request = ((FilterInvocation) object).getRequest();
        String path = getRequestPath(request);
        // 当请求的是登录和登出url时忽略
        if (URLs.HOME.equals(path) || URLs.LOGIN.equals(path) || URLs.LOGOUT.equals(path)) {
            return;
        }
        // TODO FOR TEST
        if (path.startsWith("/test")) {
            return;
        }
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        if (!authorities.isEmpty()) {
            if (authorities.iterator().next() instanceof Role) {
                String method = request.getMethod();
                List<Integer> roleIds = authorities.stream().map(a -> ((Role)a).getId()).collect(Collectors.toList());
                List<Menu> interfaceMenus = menuService.getRoleInterfaceMenus(roleIds);
                for (Menu menu : interfaceMenus) {
                    if (checkPermission(path, method, menu)) {
                        return;
                    }
                }
            }
        }
        throw new AccessDeniedException("您无权限执行此操作，请联系管理员。");
    }

    protected String getRequestPath(HttpServletRequest req) {
        String uri = req.getRequestURI();
        String contextPath = req.getContextPath();
        if (contextPath != null && !"".equals(contextPath) && !"/".equals(contextPath)) {
            uri = StringUtils.removeStart(uri, contextPath);
        }
        if ("".equals(uri)) {
            uri = "/";
        }
        if (uri.contains("?")) {
            uri = uri.substring(0, uri.indexOf("?"));
        }
        return uri;
    }

    private boolean checkPermission(String uri, String method, Menu menu) {
        if (!method.equalsIgnoreCase(menu.getMethod())) {
            return false;
        }
        String path = menu.getRoutePath();
        path = path.replaceAll("\\{[0-9a-zA-Z\\-_]+\\}", "[^/?&=#]+");
        uri = trimQueryParameterAndAnchor(uri);
        return uri.matches(path);
    }


    private String trimQueryParameterAndAnchor(String uri) {
        int index = -1;
        if ((index = uri.indexOf("?")) != -1 ) {
            uri = uri.substring(0, index);
        }
        if ((index = uri.indexOf("#")) != -1) {
            uri = uri.substring(0, index);
        }
        return uri;
    }

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }

}
