package coza.opencollab.meetings.utils;

import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import coza.opencollab.meetings.constant.Lti;
import coza.opencollab.meetings.model.User;

public class ContextUtil {


    public static Optional<User> getCurrentUser() {
        return getPrincipalOidcUser().map(User::of);
    }

    public static Optional<String> getCurrentSiteId() {
        return getPrincipalOidcUser()
                .flatMap(oidcUser -> Optional.ofNullable(oidcUser.getClaimAsMap(Lti.CLAIM_LIS))
                            .map(lis -> (String) lis.get(Lti.FIELD_LIS_COURSE_SECTION_SOURCE_ID)));
    }

    private static Optional<OidcUser> getPrincipalOidcUser() {
        return getAuthentification()
                .map(Authentication::getPrincipal)
                .filter(OidcUser.class::isInstance)
                .map(OidcUser.class::cast);
    }

    private static Optional<Authentication> getAuthentification() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication());
    }
}
