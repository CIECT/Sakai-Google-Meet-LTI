package coza.opencollab.meetings.controller;

import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import coza.opencollab.meetings.constant.Template;
import coza.opencollab.meetings.model.Meeting;
import coza.opencollab.meetings.service.UnauthorizedMeetingService;
import coza.opencollab.meetings.utils.ContextUtil;

@Controller
public class IndexController extends BaseController {


    @Autowired
    UnauthorizedMeetingService meetingService;


    @GetMapping({"/", "/index"})
    public String index(Model model) {
        model.addAttribute("locale", ContextUtil.getLocale().orElse(Locale.getDefault()).toString());

        String siteId = ContextUtil.getCurrentSiteId().orElseThrow(IllegalStateException::new);

        Set<Meeting> meetings = meetingService.streamPendingMeetingsForSite(siteId)
                .sorted(Meeting.BY_START_DATE_ASC.thenComparing(Meeting.BY_END_DATE_ASC))
                .collect(Collectors.toCollection(LinkedHashSet::new));

        model.addAttribute("meetings", meetings);

        return Template.INDEX;
    }
}
