package coza.opencollab.meetings.service.impl;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import coza.opencollab.meetings.model.Meeting;
import coza.opencollab.meetings.repository.MeetingRepository;
import coza.opencollab.meetings.service.BasicMeetingService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public abstract class BasicMeetingServiceImpl implements BasicMeetingService {


    protected final MeetingRepository meetingRepository;


    @Override
    public List<Meeting> getMeetingsForSite(@NonNull String siteId) {
        return meetingRepository.findAll(Example.of(Meeting.builder().siteId(siteId).build()));
    }

    @Override
    public Optional<Meeting> getMeeting(@NonNull String meetingId) {
        return meetingRepository.findById(meetingId);
    }

    protected boolean meetingExists(String meetingId) {
        return meetingId == null ? false : meetingRepository.existsById(meetingId);
    }
}
