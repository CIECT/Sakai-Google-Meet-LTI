package coza.opencollab.meetings.utils;

import java.util.Locale;
import java.util.Optional;

import org.apache.commons.lang3.LocaleUtils;
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

    public static Optional<Locale> getLocale() {
        return getPrincipalOidcUser()
                .map(oidcUser -> oidcUser.getClaimAsString(Lti.FIELD_LOCALE))
                .map(LocaleUtils::toLocale);
    }

    public static Optional<String> getToolTitle() {
        return getPrincipalOidcUser()
                .map(oidcUser -> oidcUser.getClaimAsMap(Lti.CLAIM_RESOURCE_LINK))
                .map(resourceLink -> resourceLink.get(Lti.FIELD_RESOURCE_LINK_TITLE))
                .map(String.class::cast);
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
