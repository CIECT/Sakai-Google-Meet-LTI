package coza.opencollab.meetings.provider.impl;

import java.io.IOException;
import java.util.Date;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.OAuth2AccessToken;

import com.google.api.gax.core.CredentialsProvider;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.apps.meet.v2.Space;
import com.google.apps.meet.v2.SpacesServiceClient;
import com.google.apps.meet.v2.SpacesServiceSettings;
import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.GoogleCredentials;

import coza.opencollab.meetings.model.Meeting;
import coza.opencollab.meetings.model.MeetingVendor;
import coza.opencollab.meetings.provider.MeetingProvider;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GoogleMeetingProvider implements MeetingProvider {


    private final OAuth2AuthorizedClient authorizedClient;

    private SpacesServiceClient spacesServiceClient;


    public GoogleMeetingProvider(OAuth2AuthorizedClient authorizedClient) {
        this.authorizedClient = authorizedClient;
    }


    @PostConstruct
    public void init() {
        OAuth2AccessToken springAccessToken = authorizedClient.getAccessToken();

        AccessToken googleAccessToken = AccessToken.newBuilder()
                .setTokenValue(springAccessToken.getTokenValue())
                .setExpirationTime(Date.from(springAccessToken.getExpiresAt()))
                .setScopes(authorizedClient.getAccessToken().getScopes().stream().collect(Collectors.toList()))
                .build();

        CredentialsProvider credentialsProvider = FixedCredentialsProvider.create(GoogleCredentials.create(googleAccessToken));

        try {
            this.spacesServiceClient = SpacesServiceClient.create(
                    SpacesServiceSettings.newBuilder().setCredentialsProvider(credentialsProvider).build());
        } catch (IOException e) {
            log.error("Could not create google services due to {}: {}", e, ExceptionUtils.getStackTrace(e));
        }
    }

    public static GoogleMeetingProvider using(OAuth2AuthorizedClient authorizedClient) {
        GoogleMeetingProvider googleMeetingProvider = new GoogleMeetingProvider(authorizedClient);
        googleMeetingProvider.init();
        return googleMeetingProvider;
    }

    @Override
    public MeetingVendor getKey() {
        return MeetingVendor.GOOGLE;
    }

    @Override
    public Meeting createMeeting(Meeting meeting) {
        Space createdSpace = spacesServiceClient.createSpace(Space.newBuilder().build());

        meeting.setExternalId(createdSpace.getName());
        meeting.setUrl(createdSpace.getMeetingUri());

        return meeting;
    }

    @Override
    public Meeting updateMeeting(Meeting meeting) {
        // NOOP; No space fields will change by updating the meeting
        return meeting;
    }

    @Override
    public Meeting deleteMeeting(Meeting meeting) {
        // NOOP; Spaces can not be deleted
        return meeting;
    }
}
