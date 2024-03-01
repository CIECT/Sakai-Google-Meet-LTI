package coza.opencollab.meetings.service.impl;

import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.stereotype.Service;

import coza.opencollab.meetings.provider.impl.GoogleMeetingProvider;
import coza.opencollab.meetings.repository.MeetingRepository;
import coza.opencollab.meetings.service.AuthorizedMeetingService;
import coza.opencollab.meetings.service.UnauthorizedMeetingService;

@Service
public class UnauthorizedMeetingServiceImpl extends BasicMeetingServiceImpl implements UnauthorizedMeetingService {


    public UnauthorizedMeetingServiceImpl(MeetingRepository meetingRepository) {
        super(meetingRepository);
    }


    @Override
    public AuthorizedMeetingService authorized(OAuth2AuthorizedClient authorizedClient) {
        return new AuthorizedMeetingServiceImpl(
                meetingRepository,
                GoogleMeetingProvider.using(authorizedClient));
    }
}
