package com.musigma.engineering.paymentservice;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RootRedirectController {

    // Intercepts direct hits to the root domain index URL
    @GetMapping("/")
    public String redirectToH2Console() {
        // Automatically redirects the browser context right into our H2 Web Console dashboard
        return "redirect:/h2-console";
    }
}
