package coza.opencollab.meetings.controller;

import java.util.Collections;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.ModelAttribute;

import coza.opencollab.meetings.model.User;
import coza.opencollab.meetings.service.PrivilegeService;
import coza.opencollab.meetings.utils.ContextUtil;

public class BaseController {


    @Autowired
    private PrivilegeService privilegeService;

    @Autowired
    private MessageSource messageSource;

    @ModelAttribute("privilege")
    public Map<String, Boolean> privilege() {
        return ContextUtil.getCurrentUser()
                .map(User::getRole)
                .map(privilegeService::getPermissionMap)
                .orElse(Collections.emptyMap());
    }

    @ModelAttribute("title")
    public String title() {
        return ContextUtil.getToolTitle()
                .orElseGet(() -> messageSource.getMessage("title.tool.fallback", null,
                        ContextUtil.getLocale().orElse(Locale.getDefault())));
    }
}
