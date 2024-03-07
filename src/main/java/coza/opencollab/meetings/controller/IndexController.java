package coza.opencollab.meetings.controller;

import java.util.Collections;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import coza.opencollab.meetings.constant.Template;
import coza.opencollab.meetings.service.UnauthorizedMeetingService;
import coza.opencollab.meetings.utils.ContextUtil;

@Controller
public class IndexController extends BaseController {


    @Autowired
    UnauthorizedMeetingService meetingService;


    @GetMapping({"/", "/index"})
    public String index(Model model) {
        model.addAttribute("locale", ContextUtil.getLocale().orElse(Locale.getDefault()).toString());

        model.addAttribute("meetings", ContextUtil.getCurrentSiteId()
                .map(meetingService::getMeetingsForSite)
                .orElse(Collections.emptyList()));

        return Template.INDEX;
    }
}
