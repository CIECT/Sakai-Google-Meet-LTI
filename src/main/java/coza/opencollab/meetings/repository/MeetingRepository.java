package coza.opencollab.meetings.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import coza.opencollab.meetings.model.Meeting;
import java.util.stream.Stream;


@Repository
public interface MeetingRepository extends JpaRepository<Meeting, String> {


    Stream<Meeting> findBySiteId(String siteId);
}
