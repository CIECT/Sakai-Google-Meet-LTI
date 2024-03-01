package coza.opencollab.meetings.validation.validator;

import java.time.Instant;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang3.ObjectUtils;

import coza.opencollab.meetings.model.Meeting;
import coza.opencollab.meetings.validation.constrain.StartDateBeforeEndDate;;


public class StartBeforeEndValidator implements ConstraintValidator<StartDateBeforeEndDate, Meeting> {


    @Override
    public boolean isValid(Meeting meeting, ConstraintValidatorContext context) {
        Instant startDate = meeting.getStartDate();
        Instant endDate = meeting.getEndDate();

        return ObjectUtils.allNotNull(startDate, endDate) && !startDate.isAfter(endDate);
    }
}
