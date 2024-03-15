package coza.opencollab.meetings.controller;

import java.net.URI;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriComponentsBuilder;

import coza.opencollab.meetings.constant.OAuth2Client;
import coza.opencollab.meetings.constant.Template;

@Controller
public class OAuth2Controller {


    @GetMapping("/intercept/oauth2/code/google")
    public String intercept(Model model, @RequestParam MultiValueMap<String,String> params) {
        URI uri = UriComponentsBuilder.fromHttpUrl(OAuth2Client.GOOGLE_AUTH_URL)
                .queryParams(params)
                .build()
                .toUri();

        model.addAttribute("redirect", uri);
        model.addAttribute("returnUrl", "/index");

        return Template.OAUTH2_LOAD;
    }

    @GetMapping("/intercept/oauth2/access-denied")
    public String authError(Model model, @RequestParam String from) {
        model.addAttribute("returnUrl", "/index");

        return Template.OAUTH2_ACCESS_DENIED;
    }
}
