package coza.opencollab.meetings.exception;

import coza.opencollab.meetings.model.MeetingVendor;

public class NoSuchProviderException extends RuntimeException {


    public NoSuchProviderException(MeetingVendor meetingVendor) {
        super("There is no provider with key " + meetingVendor.toString());
    }
}