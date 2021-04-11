package bsep.tim4.hospitalApp.users.config;

import bsep.tim4.hospitalApp.users.dto.UserLoginDTO;
import bsep.tim4.hospitalApp.users.model.User;
import bsep.tim4.hospitalApp.users.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;

@Component
public class CustomLoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    int MAX_FAILED_ATTEMPTS = 3;

    @Autowired
    private UserService userService;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {

        UserLoginDTO userLoginDTO = new ObjectMapper().readValue(request.getInputStream(), UserLoginDTO.class);
        User user = userService.findByEmail(userLoginDTO.getUsername());

        if (user != null) {
            if (user.isEnabled() && user.isAccountNonLocked()) {
                if(user.getFailedAttempts() < MAX_FAILED_ATTEMPTS - 1) {
                    userService.increaseFailedAttempts(user);
                }
                //ako je dosao do max-a zakljucava se nalog
                else {
                    userService.lock(user);
                    exception = new LockedException("Your account has been locked due to 3 failed attempts."
                            + " It will be unlocked after 24 hours.");
                }
            } else if (!user.isAccountNonLocked()) {
                if(userService.unlockWhenTimeExpired(user)) {
                    exception = new LockedException("Your account has been unlocked. Please try to login again.");
                }
            }
        }

        super.onAuthenticationFailure(request, response, exception);
    }

}
