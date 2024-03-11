package coza.opencollab.meetings.service.impl;

import javax.persistence.EntityManager;

import coza.opencollab.meetings.model.Meeting;
import coza.opencollab.meetings.provider.MeetingProvider;
import coza.opencollab.meetings.repository.MeetingRepository;
import coza.opencollab.meetings.service.AuthorizedMeetingService;
import lombok.NonNull;

public class AuthorizedMeetingServiceImpl extends BasicMeetingServiceImpl implements AuthorizedMeetingService {

    private final MeetingProvider meetingProvider;

    public AuthorizedMeetingServiceImpl(
            EntityManager entityManager,
            MeetingRepository meetingRepository,
            MeetingProvider meetingProvider) {

        super(entityManager, meetingRepository);
        this.meetingProvider = meetingProvider;
    }

    @Override
    public Meeting createMeeting(Meeting meeting) {
        Meeting createdMeeting = meetingProvider.createMeeting(meeting);
        return meetingRepository.save(createdMeeting);
    }

    @Override
    public Meeting updateMeeting(Meeting meeting) {
        Meeting updatedMeeting = meetingProvider.updateMeeting(meeting);
        return meetingRepository.save(updatedMeeting);
    }

    @Override
    public Meeting saveMeeting(@NonNull Meeting meeting) {
        String meetingId = meeting.getId();

        if (meetingExists(meetingId)) {
            return updateMeeting(meeting);
        }

        // Set id to null, just to be sure
        meeting.setId(null);
        return createMeeting(meeting);
    }

    @Override
    public void deleteMeeting(@NonNull String meetingId) {
        if (!meetingExists(meetingId)) {
            throw new IllegalArgumentException("No meeting with meetingId [" + meetingId + "] exists");
        }

        meetingRepository.deleteById(meetingId);
    }
}
