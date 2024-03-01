package coza.opencollab.meetings.service;

import java.util.List;
import java.util.Optional;

import coza.opencollab.meetings.model.Meeting;

public interface MeetingService {


    public List<Meeting> getMeetingsForSite(String siteId);

    public Meeting createMeeting(Meeting meeting);

    public Optional<Meeting> getMeeting(String meetingId);

    public Meeting updateMeeting(Meeting meeting);

    public Meeting saveMeeting(Meeting meeting);

    public void deleteMeeting(Meeting meeting);
}
