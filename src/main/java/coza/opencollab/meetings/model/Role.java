package coza.opencollab.meetings.model;

import java.util.Collection;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import coza.opencollab.meetings.constant.Lti;
import lombok.NonNull;

public enum Role {
    ADMIN,
    INSTRUCTOR,
    STUDENT;


    private static final Set<String> adminLtiRoles = Set.of(
            Lti.VOCAB_ROLE_INSTITUTION_ADMIN,
            Lti.VOCAB_ROLE_MEMBERSHIP_ADMIN,
            Lti.VOCAB_ROLE_SYSTEM_ADMIN
    );

    private static final Set<String> instructorLtiRoles = Set.of(Lti.VOCAB_ROLE_MEMBERSHIP_INSTRUCTOR);
    private static final Set<String> studentLtiRoles = Set.of(Lti.VOCAB_ROLE_MEMBERSHIP_LEARNER);


    public static Role getInstance(@NonNull Collection<String> ltiRoles) {
        if(CollectionUtils.containsAny(ltiRoles, adminLtiRoles)) {
            return ADMIN;
        } else if(CollectionUtils.containsAny(ltiRoles, instructorLtiRoles)) {
            return INSTRUCTOR;
        } else if(CollectionUtils.containsAny(ltiRoles, studentLtiRoles)) {
            return STUDENT;
        }

        throw new IllegalArgumentException("No matching roles for lti roles [" + StringUtils.join(ltiRoles, ", ") + "]");
    }
}
