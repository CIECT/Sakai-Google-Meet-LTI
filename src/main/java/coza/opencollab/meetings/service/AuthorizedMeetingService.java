package coza.opencollab.meetings.service;

import coza.opencollab.meetings.model.Meeting;

public interface AuthorizedMeetingService extends BasicMeetingService {


    public Meeting createMeeting(Meeting meeting);

    public Meeting updateMeeting(Meeting meeting);

    public Meeting saveMeeting(Meeting meeting);

    public void deleteMeeting(String meetingId);
}
