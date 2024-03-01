package coza.opencollab.meetings.model;

import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import coza.opencollab.meetings.constant.Lti;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
public class User {


    private String fullName;
    private Role role;


    public static User of(@NonNull OidcUser oidcUser) {
        return User.builder()
            .fullName(oidcUser.getClaimAsString(Lti.CLAIM_FULL_NAME))
            .role(Role.getInstance(oidcUser.getClaimAsStringList(Lti.CLAIM_ROLES)))
        .build();
    }
}
