package tech.hongjian.oa.config.flowable.query;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;
import org.flowable.common.engine.api.query.QueryProperty;
import org.flowable.idm.api.Group;
import org.flowable.idm.api.GroupQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.hongjian.oa.entity.Role;
import tech.hongjian.oa.service.RoleService;
import tech.hongjian.oa.util.CommonUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 自定义GroupQuery，用于映射Flowable的group到自己的Role
 * Created by xiahongjian on 2021/4/8.
 */
@Service
public class CustomGroupQueryImpl implements GroupQuery {
    @Setter(onMethod_ = {@Autowired})
    private RoleService roleService;

    private Integer id;
    private List<Integer> ids;
    private String name;
    private String nameLike;
    private String orderBy;
    private String direction;



    @Override
    public GroupQuery groupId(String groupId) {
        id = Integer.valueOf(groupId);
        return this;
    }

    @Override
    public GroupQuery groupIds(List<String> groupIds) {
        if (CollectionUtils.isNotEmpty(groupIds)) {
            ids = groupIds.stream().map(Integer::valueOf).collect(Collectors.toList());
        }
        return this;
    }

    @Override
    public GroupQuery groupName(String groupName) {
        name = groupName;
        return this;
    }

    @Override
    public GroupQuery groupNameLike(String groupNameLike) {
        nameLike = groupNameLike;
        return this;
    }

    @Override
    public GroupQuery groupNameLikeIgnoreCase(String groupNameLikeIgnoreCase) {
        // 不支持此参数
        return this;
    }

    @Override
    public GroupQuery groupType(String groupType) {
        // 不支持此参数
        return this;
    }

    @Override
    public GroupQuery groupMember(String groupMemberUserId) {
        // 不支持此参数
        return this;
    }

    @Override
    public GroupQuery groupMembers(List<String> groupMemberUserIds) {
        // 不支持此参数
        return this;
    }

    @Override
    public GroupQuery orderByGroupId() {
        orderBy = "id";
        return this;
    }

    @Override
    public GroupQuery orderByGroupName() {
        orderBy = "name";
        return this;
    }

    @Override
    public GroupQuery orderByGroupType() {
        // 不支持此参数
        return this;
    }

    @Override
    public GroupQuery asc() {
        direction = "asc";
        return this;
    }

    @Override
    public GroupQuery desc() {
        direction = "desc";
        return this;
    }

    @Override
    public GroupQuery orderBy(QueryProperty property) {
        // 不支持此参数
        return this;
    }

    @Override
    public GroupQuery orderBy(QueryProperty property, NullHandlingOnOrder nullHandlingOnOrder) {
        // 不支持此参数
        return this;
    }

    @Override
    public long count() {
        return roleService.countByParamMap(buildQueryMap());
    }

    @Override
    public Group singleResult() {

        return null;
    }

    @Override
    public List<Group> list() {
        roleService.findByParamMap(buildQueryMap());
        return null;
    }

    @Override
    public List<Group> listPage(int firstResult, int maxResults) {
        roleService.findByParamMapPage(new Page<Role>(firstResult, maxResults), buildQueryMap());
        return null;
    }

    private Map<String, Object> buildQueryMap() {
        Map<String, Object> map = new HashMap<>(6);
        map.put("id", id);
        map.put("ids", ids);
        map.put("name", name);
        map.put("nameLike", nameLike == null ? null : CommonUtil.wrapWithPercent(nameLike));
        map.put("orderBy", orderBy);
        map.put("direction", direction);
        return map;
    }
}
