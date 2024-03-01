package coza.opencollab.meetings.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import coza.opencollab.meetings.model.Meeting;

@Repository
public interface MeetingRepository extends JpaRepository<Meeting, String> { }
