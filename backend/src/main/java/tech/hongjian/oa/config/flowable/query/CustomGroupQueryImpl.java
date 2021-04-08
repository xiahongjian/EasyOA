package tech.hongjian.oa.config.flowable.query;

import org.flowable.common.engine.api.query.QueryProperty;
import org.flowable.idm.api.Group;
import org.flowable.idm.api.GroupQuery;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by xiahongjian on 2021/4/8.
 */
@Service
public class CustomGroupQueryImpl implements GroupQuery {
    @Override
    public GroupQuery groupId(String groupId) {
        return null;
    }

    @Override
    public GroupQuery groupIds(List<String> groupIds) {
        return null;
    }

    @Override
    public GroupQuery groupName(String groupName) {
        return null;
    }

    @Override
    public GroupQuery groupNameLike(String groupNameLike) {
        return null;
    }

    @Override
    public GroupQuery groupNameLikeIgnoreCase(String groupNameLikeIgnoreCase) {
        return null;
    }

    @Override
    public GroupQuery groupType(String groupType) {
        return null;
    }

    @Override
    public GroupQuery groupMember(String groupMemberUserId) {
        return null;
    }

    @Override
    public GroupQuery groupMembers(List<String> groupMemberUserIds) {
        return null;
    }

    @Override
    public GroupQuery orderByGroupId() {
        return null;
    }

    @Override
    public GroupQuery orderByGroupName() {
        return null;
    }

    @Override
    public GroupQuery orderByGroupType() {
        return null;
    }

    @Override
    public GroupQuery asc() {
        return null;
    }

    @Override
    public GroupQuery desc() {
        return null;
    }

    @Override
    public GroupQuery orderBy(QueryProperty property) {
        return null;
    }

    @Override
    public GroupQuery orderBy(QueryProperty property, NullHandlingOnOrder nullHandlingOnOrder) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public Group singleResult() {
        return null;
    }

    @Override
    public List<Group> list() {
        return null;
    }

    @Override
    public List<Group> listPage(int firstResult, int maxResults) {
        return null;
    }
}
