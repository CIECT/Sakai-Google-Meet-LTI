package coza.opencollab.meetings.service;

import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;

public interface UnauthorizedMeetingService extends BasicMeetingService {


    public AuthorizedMeetingService authorized(OAuth2AuthorizedClient authorizedClient);
}
