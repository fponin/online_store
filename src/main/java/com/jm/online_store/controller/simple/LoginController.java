package com.jm.online_store.controller.simple;

import com.jm.online_store.config.security.Twitter.TwitterAuth;
import com.jm.online_store.config.security.odnoklassniki.OAuth2Odnoklassniki;
import com.jm.online_store.model.User;
import com.jm.online_store.service.interf.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@AllArgsConstructor
@Controller
@RequestMapping("/")
public class LoginController {

    private final OAuth2Odnoklassniki oAuth2Odnoklassniki;
    private final TwitterAuth twitterAuth;
    private final UserService userService;

    @GetMapping("/oauth")
    public String oAuthOdnoklassniki(@RequestParam String code) {
        oAuth2Odnoklassniki.UserAuth(code);
        return "redirect:/";
    }

    @GetMapping("/oauthTwitter")
    public String oAuthTwitter(@RequestParam String oauth_verifier,  Model model) throws InterruptedException, ExecutionException, IOException {
        twitterAuth.getAccessToken(oauth_verifier);
        Optional<User> user = userService.findByFirstName("Yuri7864328");
        System.out.println(user);

        return "TwitterRegistrationPage";
    }

    @GetMapping(value = "/login")
    public String loginPage(Model model) throws InterruptedException, ExecutionException, IOException {
        String authUrlOK = oAuth2Odnoklassniki.getAuthorizationUrl();
        model.addAttribute("authUrlOK", authUrlOK);

        String twitterUrl = twitterAuth.twitterAuth();
        model.addAttribute("twitterUrl", twitterUrl);
        return "login";
    }

    @GetMapping("/denied")
    public String deniedPage() {
        return "denied";
    }
}
