package bsep.tim4.hospitalApp.users.config;

import bsep.tim4.hospitalApp.users.model.User;
import bsep.tim4.hospitalApp.users.service.SecureUserDetailsService;
import bsep.tim4.hospitalApp.users.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomLoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    private UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        User user = (User) authentication.getPrincipal();
        if(user.getFailedAttempts() > 0) {
            userService.resetFailedAttempts(user);
        }

        super.onAuthenticationSuccess(request, response, authentication);

    }
}
