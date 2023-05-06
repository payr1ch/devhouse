package com.example.devhouse.user_things.registration;

import com.example.devhouse.user_things.event.RegistrationCompleteEvent;
import com.example.devhouse.user_things.registration.token.VerificationToken;
import com.example.devhouse.user_things.registration.token.VerificationTokenRepo;
import com.example.devhouse.user_things.user.User;
import com.example.devhouse.user_things.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/register")
public class RegistrationController {

    private final UserService userService;
    private final ApplicationEventPublisher publisher;

    private final VerificationTokenRepo verificationTokenRepo;
    @PostMapping
    public String registerUser(@RequestBody RegistrationRequest request, final HttpServletRequest httpServletRequest){
        User user = userService.registerUser(request);
        publisher.publishEvent(new RegistrationCompleteEvent(user, applicationUrl(httpServletRequest)));
        return "Success! Please check your email to verify it!";
    }

    @GetMapping("/verifyEmail")
    public String verifyEmail(@RequestParam("token") String token){
        VerificationToken theToken = verificationTokenRepo.findByToken(token);
        if(theToken.getUser().getIsEnabled()){
            return "This account has already been verified)";
        }
        String verificationResult = userService.validateToken(token);
        if(verificationResult.equalsIgnoreCase("valid")){
            return "Email verified successfully!";
        }
        return "Incalid verification token";
    }


    private String applicationUrl(HttpServletRequest httpServletRequest) {
        return "http://" + httpServletRequest.getServerName() + ":" + httpServletRequest.getServerPort() + httpServletRequest.getContextPath();
    }

}
