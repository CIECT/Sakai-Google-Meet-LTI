package coza.opencollab.meetings.model;

import lombok.Getter;

public enum Function {
    EDIT_MEETINGS("canEditMeetings");


    @Getter
    private String label;


    private Function(String label) {
        this.label = label;
    }
}
