package coza.opencollab.meetings.provider;

import coza.opencollab.meetings.model.Meeting;
import coza.opencollab.meetings.model.MeetingVendor;

public interface MeetingProvider {


    public MeetingVendor getKey();

    public Meeting createMeeting(Meeting meeting);

    public Meeting updateMeeting(Meeting meeting);

    public Meeting deleteMeeting(Meeting meeting);
}