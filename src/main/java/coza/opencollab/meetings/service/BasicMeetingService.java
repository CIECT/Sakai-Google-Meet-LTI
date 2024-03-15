package coza.opencollab.meetings.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import coza.opencollab.meetings.model.Meeting;

public interface BasicMeetingService {


    public List<Meeting> getMeetingsForSite(String siteId);

    public Stream<Meeting> streamPendingMeetingsForSite(String siteId);

    public Stream<Meeting> streamPastMeetingsForSite(String siteId);

    public Optional<Meeting> getMeeting(String meetingId);
}
