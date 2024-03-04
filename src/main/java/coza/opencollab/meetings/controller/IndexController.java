package coza.opencollab.meetings.controller;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import coza.opencollab.meetings.constant.Lti;
import coza.opencollab.meetings.constant.Template;
import coza.opencollab.meetings.service.UnauthorizedMeetingService;
import coza.opencollab.meetings.utils.ContextUtil;

@Controller
public class IndexController extends BaseController {


    @Autowired
    UnauthorizedMeetingService meetingService;


    @GetMapping({"/", "/index"})
    public String index(Model model, OAuth2AuthenticationToken token) {
        model.addAttribute("locale", token.getPrincipal().getAttribute(Lti.FIELD_LOCALE));

        model.addAttribute("meetings", ContextUtil.getCurrentSiteId()
                .map(meetingService::getMeetingsForSite)
                .orElse(Collections.emptyList()));

        return Template.INDEX;
    }
}
