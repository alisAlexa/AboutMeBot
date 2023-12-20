package org.pack.telegram.enums;

public enum MeetingEnum {
    MEETING,
    DATE,
    TIME,
    PREV_WEEK,
    NEXT_WEEK,
    APPROVE;

    public static boolean isMeeting(String field) {
        return field.contains(MEETING.name()) ||
                field.contains(DATE.name()) ||
                field.contains(TIME.name()) ||
                field.contains(PREV_WEEK.name()) ||
                field.contains(NEXT_WEEK.name()) ||
                field.contains(APPROVE.name());
    }


}
