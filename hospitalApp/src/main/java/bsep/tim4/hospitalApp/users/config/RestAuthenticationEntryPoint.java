package bsep.tim4.hospitalApp.users.config;

import bsep.tim4.hospitalApp.users.dto.UserLoginDTO;
import bsep.tim4.hospitalApp.users.model.User;
import bsep.tim4.hospitalApp.users.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

	int MAX_FAILED_ATTEMPTS = 3;

	@Autowired
	private UserService userService;

	@Override
    public void commence(HttpServletRequest httpServletRequest,
                         HttpServletResponse httpServletResponse,
                         AuthenticationException authException) 
                        		 throws IOException, ServletException {
		// authentication failed
		UserLoginDTO userLoginDTO = new ObjectMapper().readValue(httpServletRequest.getInputStream(), UserLoginDTO.class);
		User user = userService.findByEmail(userLoginDTO.getUsername());

		if (user != null) {
			if (user.isEnabled() && user.isAccountNonLocked()) {
				if(user.getFailedAttempts() < MAX_FAILED_ATTEMPTS - 1) {
					userService.increaseFailedAttempts(user);
				}
				//ako je dosao do max-a zakljucava se nalog
				else {
					userService.lock(user);
				}
			} else if (!user.isAccountNonLocked()) {
				if(userService.unlockWhenTimeExpired(user)) {
					System.out.println("Unlocked account.");
				}
			}
		}

    	httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
    }
}
