package coza.opencollab.meetings.controller;

import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import coza.opencollab.meetings.constant.Action;
import coza.opencollab.meetings.constant.Layout;
import coza.opencollab.meetings.constant.Template;
import coza.opencollab.meetings.constant.View;
import coza.opencollab.meetings.model.Meeting;
import coza.opencollab.meetings.service.UnauthorizedMeetingService;
import coza.opencollab.meetings.utils.ContextUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@SessionAttributes({
    "view",
    "layout",
})
public class IndexController extends BaseController {


    private static final String ATTRIBUTE_VIEW = "view";
    private static final String ATTRIBUTE_LAYOUT = "layout";
    private static final String ATTRIBUTE_MEETINGS = "meetings";
    private static final String ATTRIBUTE_LOCALE = "locale";

    @Autowired
    UnauthorizedMeetingService meetingService;

    @Value("${meetings.max-count: 200}")
    private Integer meetingsMaxCount;


    @GetMapping({"/", "/index"})
    public String index(Model model, @RequestParam Optional<Action> action) {
        addDefaults(model);
        addLocale(model);

        action.ifPresent(a -> performAction(model, a));

        addMeetings(model);

        return Template.INDEX;
    }

    private void addDefaults(Model model) {
        if (StringUtils.isBlank((String) model.getAttribute(ATTRIBUTE_VIEW))) {
            model.addAttribute(ATTRIBUTE_VIEW, View.FUTURE);
        }

        if (StringUtils.isBlank((String) model.getAttribute(ATTRIBUTE_LAYOUT))) {
            model.addAttribute(ATTRIBUTE_LAYOUT, Layout.LIST);
        }
    }

    private void addLocale(Model model) {
        Locale locale = ContextUtil.getLocale().orElse(Locale.getDefault());

        model.addAttribute(ATTRIBUTE_LOCALE, locale.toString());
    }

    private void addMeetings(Model model) {
        String siteId = ContextUtil.getCurrentSiteId().orElseThrow(IllegalStateException::new);

        Stream<Meeting> meetingStream;

        switch ((String) model.getAttribute(ATTRIBUTE_VIEW)) {
            case View.FUTURE:
                meetingStream = meetingService.streamPendingMeetingsForSite(siteId);
                break;
            case View.PAST:
                meetingStream = meetingService.streamPastMeetingsForSite(siteId);
                break;
            default:
                meetingStream = Stream.empty();
                break;
        }

        Set<Meeting> meetings = meetingStream
                .sorted(Meeting.BY_START_DATE_ASC.thenComparing(Meeting.BY_END_DATE_ASC))
                .limit(meetingsMaxCount)
                .collect(Collectors.toCollection(LinkedHashSet::new));

        model.addAttribute(ATTRIBUTE_MEETINGS, meetings);
    }

    private void performAction(Model model, Action action) {
        switch (action) {
            case SET_VIEW_PAST:
                model.addAttribute(ATTRIBUTE_VIEW, View.PAST);
                break;
            case SET_VIEW_FUTURE:
                model.addAttribute(ATTRIBUTE_VIEW, View.FUTURE);
                break;
            case SET_LAYOUT_LIST:
                model.addAttribute(ATTRIBUTE_LAYOUT, Layout.LIST);
                break;
            case SET_LAYOUT_GRID:
                model.addAttribute(ATTRIBUTE_LAYOUT, Layout.GRID);
                break;
            default:
                log.debug("No-op action {}", action);
                break;
        }
    }
}
