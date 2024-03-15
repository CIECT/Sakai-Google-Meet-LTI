package coza.opencollab.meetings.model;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.Size;

import org.hibernate.annotations.GenericGenerator;

import coza.opencollab.meetings.validation.constrain.StartDateBeforeEndDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@StartDateBeforeEndDate(message = "{error.startdate-before-enddate}")
public class Meeting {


    public static final Comparator<Meeting> BY_START_DATE_ASC = Comparator.comparing(Meeting::getStartDate);
    public static final Comparator<Meeting> BY_START_DATE_DESC = BY_START_DATE_ASC.reversed();
    public static final Comparator<Meeting> BY_END_DATE_ASC = Comparator.comparing(Meeting::getEndDate);
    public static final Comparator<Meeting> BY_END_DATE_DESC = BY_END_DATE_ASC.reversed();


    @Id
    @Column(nullable = false, length = 36)
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @Column(nullable = false, length = 36)
    private String siteId;

    @Column(nullable = false, length = 255)
    private String externalId;

    @Column(nullable = false, length = 255)
    @Size(min = 1, max = 255)
    private String title;

    @Column(length = 2047)
    @Size(max = 2047)
    private String description;

    @Column(nullable = false, length = 255)
    private String url;

    @Column
    private Instant startDate;

    @Column
    @FutureOrPresent(message = "{error.enddate-in-past}")
    private Instant endDate;


    public Boolean getActive() {
        Instant now = Instant.now();

        // If start date is null, it must have started already
        boolean started = startDate == null || startDate.isBefore(now);

        // If end date is null, it's an never ending meeting
        boolean ended = endDate != null && endDate.isBefore(now);

        return started && !ended;
    }

    public static Meeting template() {
        return Meeting.builder()
                .startDate(Instant.now())
                .endDate(Instant.now().plus(1, ChronoUnit.HOURS))
                .build();
    }
}
