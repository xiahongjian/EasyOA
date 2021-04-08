package tech.hongjian.oa.config.flowable;

import lombok.Setter;
import org.flowable.common.engine.api.FlowableException;
import org.flowable.idm.api.*;
import org.flowable.idm.engine.IdmEngineConfiguration;
import org.flowable.idm.engine.impl.IdmIdentityServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tech.hongjian.oa.config.flowable.query.CustomGroupQueryImpl;
import tech.hongjian.oa.config.flowable.query.CustomUserQueryImpl;

import java.util.Collections;
import java.util.List;

/**
 * Created by xiahongjian on 2021/4/8.
 */
@Setter(onMethod_ = {@Autowired})
@Component
public class CustomIdentityServiceImpl extends IdmIdentityServiceImpl {

    private CustomUserQueryImpl userQuery;
    private CustomGroupQueryImpl groupQuery;

    public CustomIdentityServiceImpl(IdmEngineConfiguration idmEngineConfiguration) {
        super(idmEngineConfiguration);
    }

    @Override
    public UserQuery createUserQuery() {
        return userQuery;
    }

    @Override
    public GroupQuery createGroupQuery() {
        return groupQuery;
    }

    @Override
    public boolean checkPassword(String userId, String password) {
        return true;
    }

    @Override
    public List<Group> getGroupsWithPrivilege(String name) {
        return Collections.emptyList();
    }

    @Override
    public List<User> getUsersWithPrivilege(String name) {
        return Collections.emptyList();
    }

    @Override
    public User newUser(String userId) {
        throw new FlowableException("Custom identity service doesn't support creating a new user");
    }

    @Override
    public void saveUser(User user) {
        throw new FlowableException("Custom identity service doesn't support saving an user");
    }

    @Override
    public NativeUserQuery createNativeUserQuery() {
        throw new FlowableException("Custom identity service doesn't support native querying");
    }

    @Override
    public void deleteUser(String userId) {
        throw new FlowableException("Custom identity service doesn't support deleting an user");
    }

    @Override
    public Group newGroup(String groupId) {
        throw new FlowableException("Custom identity service doesn't support creating a new group");
    }

    @Override
    public NativeGroupQuery createNativeGroupQuery() {
        throw new FlowableException("Custom identity service doesn't support native querying");
    }

    @Override
    public void saveGroup(Group group) {
        throw new FlowableException("Custom identity service doesn't support saving a group");
    }

    @Override
    public void deleteGroup(String groupId) {
        throw new FlowableException("Custom identity service doesn't support deleting a group");
    }
}
