package coza.opencollab.meetings.service.impl;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import javax.persistence.EntityManager;

import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import coza.opencollab.meetings.model.Meeting;
import coza.opencollab.meetings.repository.MeetingRepository;
import coza.opencollab.meetings.service.BasicMeetingService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class BasicMeetingServiceImpl implements BasicMeetingService {


    protected final EntityManager entityManager;
    protected final MeetingRepository meetingRepository;


    @Override
    public List<Meeting> getMeetingsForSite(@NonNull String siteId) {
        return meetingRepository.findAll(Example.of(Meeting.builder().siteId(siteId).build()));
    }

    @Override
    @Transactional(readOnly = true)
    public Stream<Meeting> streamPendingMeetingsForSite(String siteId) {
        Instant now = Instant.now();

        return meetingRepository.findBySiteId(siteId)
                .peek(entityManager::detach)
                .filter(meeting -> !isPastMeeting(now, meeting));
    }

    @Override
    @Transactional(readOnly = true)
    public Stream<Meeting> streamPastMeetingsForSite(String siteId) {
        Instant now = Instant.now();

        return meetingRepository.findBySiteId(siteId)
                .peek(entityManager::detach)
                .filter(meeting -> isPastMeeting(now, meeting));
    }

    @Override
    public Optional<Meeting> getMeeting(@NonNull String meetingId) {
        return meetingRepository.findById(meetingId);
    }

    protected boolean meetingExists(String meetingId) {
        return meetingId == null ? false : meetingRepository.existsById(meetingId);
    }

    protected boolean isPastMeeting(Instant now, Meeting meeting) {
        return meeting.getEndDate().isBefore(now);
    }
}
