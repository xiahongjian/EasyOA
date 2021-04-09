package tech.hongjian.oa.config.flowable.query;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;
import org.flowable.common.engine.api.query.QueryProperty;
import org.flowable.idm.api.User;
import org.flowable.idm.api.UserQuery;
import org.flowable.idm.engine.impl.persistence.entity.UserEntityImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.hongjian.oa.service.UserService;
import tech.hongjian.oa.util.CommonUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by xiahongjian on 2021/4/8.
 */
@Service
public class CustomUserQueryImpl implements UserQuery {
    @Setter(onMethod_ = {@Autowired})
    private UserService userService;

    private Integer id;
    private List<Integer> ids = new ArrayList<>();
    private String email;
    private String emailLike;
    private Integer roleId;
    private List<Integer> roleIds = new ArrayList<>();
    private String orderBy;
    private String direction;

    @Override
    public UserQuery userId(String id) {
        this.id = Integer.valueOf(id);
        return this;
    }

    @Override
    public UserQuery userIds(List<String> ids) {
        if (CollectionUtils.isNotEmpty(ids)) {
            this.ids = ids.stream().map(Integer::valueOf).collect(Collectors.toList());
        }
        return this;
    }

    @Override
    public UserQuery userIdIgnoreCase(String id) {
        // 不支持此参数
        return this;
    }

    @Override
    public UserQuery userFirstName(String firstName) {
        // 不支持此参数
        return this;
    }

    @Override
    public UserQuery userFirstNameLike(String firstNameLike) {
        // 不支持此参数
        return this;
    }

    @Override
    public UserQuery userFirstNameLikeIgnoreCase(String firstNameLikeIgnoreCase) {
        // 不支持此参数
        return this;
    }

    @Override
    public UserQuery userLastName(String lastName) {
        // 不支持此参数
        return this;
    }

    @Override
    public UserQuery userLastNameLike(String lastNameLike) {
        // 不支持此参数
        return this;
    }

    @Override
    public UserQuery userLastNameLikeIgnoreCase(String lastNameLikeIgnoreCase) {
        // 不支持此参数
        return this;
    }

    @Override
    public UserQuery userFullNameLike(String fullNameLike) {
        // 不支持此参数
        return this;
    }

    @Override
    public UserQuery userFullNameLikeIgnoreCase(String fullNameLikeIgnoreCase) {
        // 不支持此参数
        return this;
    }

    @Override
    public UserQuery userDisplayName(String displayName) {
        // 不支持此参数
        return this;
    }

    @Override
    public UserQuery userDisplayNameLike(String displayNameLike) {
        // 不支持此参数
        return this;
    }

    @Override
    public UserQuery userDisplayNameLikeIgnoreCase(String displayNameLikeIgnoreCase) {
        // 不支持此参数
        return this;
    }

    @Override
    public UserQuery userEmail(String email) {
        this.email = email;
        return this;
    }

    @Override
    public UserQuery userEmailLike(String emailLike) {
        if (emailLike != null) {
            this.emailLike = CommonUtil.wrapWithPercent(emailLike);
        }
        return this;
    }

    @Override
    public UserQuery memberOfGroup(String groupId) {
        if (groupId != null) {
            this.roleId = Integer.valueOf(groupId);
        }
        return this;
    }

    @Override
    public UserQuery memberOfGroups(List<String> groupIds) {
        if (CollectionUtils.isNotEmpty(groupIds)) {
            this.roleIds = groupIds.stream().map(Integer::valueOf).collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public UserQuery tenantId(String tenantId) {
        // 不支持此参数
        return this;
    }

    @Override
    public UserQuery orderByUserId() {
        this.orderBy = "id";
        return this;
    }

    @Override
    public UserQuery orderByUserFirstName() {
        // 不支持此参数
        return this;
    }

    @Override
    public UserQuery orderByUserLastName() {
        // 不支持此参数
        return this;
    }

    @Override
    public UserQuery orderByUserEmail() {
        this.orderBy = "email";
        return null;
    }

    @Override
    public UserQuery asc() {
        this.direction = "asc";
        return this;
    }

    @Override
    public UserQuery desc() {
        this.direction = "desc";
        return this;
    }

    @Override
    public UserQuery orderBy(QueryProperty property) {
        // 不支持此参数
        return this;
    }

    @Override
    public UserQuery orderBy(QueryProperty property, NullHandlingOnOrder nullHandlingOnOrder) {
        // 不支持此参数
        return this;
    }

    @Override
    public long count() {
        return userService.countByParamMap(buildParamMap());
    }


    @Override
    public User singleResult() {
        List<User> list = list();
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public List<User> list() {
        return userService.findByParamMap(buildParamMap()).stream().map(this::convertTo).collect(Collectors.toList());
    }

    @Override
    public List<User> listPage(int firstResult, int maxResults) {
        IPage<tech.hongjian.oa.entity.User> page = new Page<>(firstResult, maxResults);
        return userService.findByParamMapPage(page, buildParamMap()).getRecords().stream().map(this::convertTo).collect(Collectors.toList());
    }

    private User convertTo(tech.hongjian.oa.entity.User user) {
        UserEntityImpl o = new UserEntityImpl();
        o.setId(String.valueOf(user.getId()));
        o.setEmail(user.getEmail());
        o.setDisplayName(user.getName());
        return o;
    }


    private Map<String, Object> buildParamMap() {
        Map<String, Object> params = new HashMap<>(8);
        params.put("id", id);
        params.put("ids", ids);
        params.put("email", email);
        params.put("emailLike", emailLike == null ? null :  CommonUtil.wrapWithPercent(emailLike));
        params.put("roleId", roleId);
        params.put("roleIds", roleIds);
        params.put("orderBy", orderBy);
        params.put("direction", direction);
        return params;
    }
}
