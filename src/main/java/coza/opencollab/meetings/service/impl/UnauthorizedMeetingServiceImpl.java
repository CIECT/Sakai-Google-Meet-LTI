package coza.opencollab.meetings.service.impl;

import javax.persistence.EntityManager;

import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.stereotype.Service;

import coza.opencollab.meetings.provider.impl.GoogleMeetingProvider;
import coza.opencollab.meetings.repository.MeetingRepository;
import coza.opencollab.meetings.service.AuthorizedMeetingService;
import coza.opencollab.meetings.service.UnauthorizedMeetingService;

@Service
public class UnauthorizedMeetingServiceImpl extends BasicMeetingServiceImpl implements UnauthorizedMeetingService {


    public UnauthorizedMeetingServiceImpl(
            EntityManager entityManager,
            MeetingRepository meetingRepository) {
        super(entityManager, meetingRepository);
    }


    @Override
    public AuthorizedMeetingService authorized(OAuth2AuthorizedClient authorizedClient) {
        return new AuthorizedMeetingServiceImpl(
                entityManager,
                meetingRepository,
                GoogleMeetingProvider.using(authorizedClient));
    }
}
