package coza.opencollab.meetings.service;

import java.util.List;
import java.util.Optional;

import coza.opencollab.meetings.model.Meeting;

public interface BasicMeetingService {


    public List<Meeting> getMeetingsForSite(String siteId);

    public Optional<Meeting> getMeeting(String meetingId);
}
