
package coza.opencollab.meetings.controller;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import coza.opencollab.meetings.constant.OAuth2Client;
import coza.opencollab.meetings.constant.Template;
import coza.opencollab.meetings.exception.BadRequestException;
import coza.opencollab.meetings.model.Function;
import coza.opencollab.meetings.model.Meeting;
import coza.opencollab.meetings.service.AuthorizedMeetingService;
import coza.opencollab.meetings.service.UnauthorizedMeetingService;
import coza.opencollab.meetings.utils.ContextUtil;
import coza.opencollab.meetings.utils.Redirect;
import lombok.NonNull;

@Controller
public class EditMeetingController extends BaseController {


    public static final String ATTRIBUTE_IS_NEW = "isNew";
    public static final String ATTRIBUTE_MEETING = "meeting";
    public static final String ATTRIBUTE_ERRORS = "errors";

    @Autowired
    private UnauthorizedMeetingService unauthorizedMeetingService;


    @GetMapping({"/edit", "/edit/{meetingId}"})
    public String editMeeting(Model model, @PathVariable(required = false) String meetingId,
            @RegisteredOAuth2AuthorizedClient(OAuth2Client.GOOGLE_CODE) OAuth2AuthorizedClient authorizedClient) {

        checkPriviledge(Function.EDIT_MEETINGS);

        Meeting meeting = StringUtils.isBlank(meetingId)
                ? Meeting.template()
                : unauthorizedMeetingService.getMeeting(meetingId)
                        .orElseThrow(BadRequestException::new);

        addEditAttributes(model, meeting);

        return Template.EDIT;
    }

    @PostMapping("/save")
    public String saveMeeting(Model model,
            @Valid @ModelAttribute(ATTRIBUTE_MEETING) Meeting meeting, BindingResult bindingResult,
            @RegisteredOAuth2AuthorizedClient(OAuth2Client.GOOGLE_CODE) OAuth2AuthorizedClient authorizedClient) {

        checkPriviledge(Function.EDIT_MEETINGS);

        if (bindingResult.hasErrors()) {
            model.addAttribute(ATTRIBUTE_ERRORS, bindingResult.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .toList());

            addEditAttributes(model, meeting);

            return Template.EDIT;
        }

        String meetingId = meeting.getId();
        AuthorizedMeetingService meetingService = unauthorizedMeetingService.authorized(authorizedClient);

        // If the meetingId is defined, it's an existing meeting
        // We need to add the fields that are not part of the form
        if (StringUtils.isNotBlank(meetingId)) {
            Meeting originalMeeting = meetingService.getMeeting(meetingId)
                    .orElseThrow(BadRequestException::new);

            meeting.setExternalId(originalMeeting.getExternalId());
            meeting.setUrl(originalMeeting.getUrl());
        }



        // Add the siteId, it's not part of the form
        meeting.setSiteId(ContextUtil.getCurrentSiteId().orElseThrow(IllegalStateException::new));

        // Trim title and description
        meeting.setTitle(StringUtils.trimToNull(meeting.getTitle()));
        meeting.setDescription(StringUtils.trimToNull(meeting.getDescription()));

        meetingService.saveMeeting(meeting);

        return Redirect.to("/index");
    }

    @GetMapping("/delete/{meetingId}")
    public String deleteMeeting(@ModelAttribute(ATTRIBUTE_MEETING) Meeting meeting, @PathVariable @NonNull String meetingId,
            @RegisteredOAuth2AuthorizedClient(OAuth2Client.GOOGLE_CODE) OAuth2AuthorizedClient authorizedClient) {

        checkPriviledge(Function.EDIT_MEETINGS);

        AuthorizedMeetingService meetingService = unauthorizedMeetingService.authorized(authorizedClient);

        meetingService.deleteMeeting(meetingId);

        return Redirect.to("/index");
    }

    private void addEditAttributes(Model model, Meeting meeting) {
        model.addAttribute(ATTRIBUTE_IS_NEW, meeting.getId() == null);
        model.addAttribute(ATTRIBUTE_MEETING, meeting);
    }
}
