package coza.opencollab.meetings.service;

import java.util.Map;

import coza.opencollab.meetings.model.Function;
import coza.opencollab.meetings.model.Role;
import coza.opencollab.meetings.model.User;

public interface PrivilegeService {


    public boolean check(Role role, Function function);

    public boolean check(User user, Function function);

    public Map<String, Boolean> getPermissionMap(Role role);
}
