package coza.opencollab.meetings.service.impl;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import coza.opencollab.meetings.model.Function;
import coza.opencollab.meetings.model.Privilege;
import coza.opencollab.meetings.model.Role;
import coza.opencollab.meetings.model.User;
import coza.opencollab.meetings.service.PrivilegeService;
import lombok.NonNull;

@Service
public class PrivilegeServiceImpl implements PrivilegeService {


    private Set<Privilege> defaultPrivileges = Set.of(
        Privilege.builder().role(Role.INSTRUCTOR).function(Function.EDIT_MEETINGS).build()
    );


    @Override
    public boolean check(@NonNull Role role, @NonNull Function function) {
        return isAdmin(role) || checkPrivilege(Privilege.builder().role(role).function(function).build());
    }

    @Override
    public boolean check(@NonNull User user, @NonNull Function function) {
        return check(user.getRole(), function);
    }

    public boolean checkPrivilege(@NonNull Privilege privilege) {
        return defaultPrivileges.contains(privilege);
    }

    public Map<String, Boolean> getPermissionMap(@NonNull Role role) {
        return Arrays.stream(Function.values())
            .collect(Collectors.toMap(Function::getLabel, function -> check(role, function)));
    }

    private boolean isAdmin(Role role) {
        return Role.ADMIN.equals(role);
    }
}
