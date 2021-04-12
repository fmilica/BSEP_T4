package bsep.tim4.hospitalApp.users.jwt;

import bsep.tim4.hospitalApp.users.dto.UserLoginDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtUsernameAndPasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    private final TokenUtils tokenUtils;

    public JwtUsernameAndPasswordAuthenticationFilter(AuthenticationManager authenticationManager, TokenUtils tokenUtils) {
        this.authenticationManager = authenticationManager;
        this.tokenUtils = tokenUtils;
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {

        try {
            UserLoginDTO userLoginDTO = new ObjectMapper().readValue(((ContentCachingRequestWrapper)request).getInputStream(), UserLoginDTO.class);

            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(
                        userLoginDTO.getUsername(),
                        userLoginDTO.getPassword()
                    ));

            return authentication;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult)
                                            throws IOException, ServletException {
        String jwtToken = tokenUtils.generateToken(authResult.getName(), authResult.getAuthorities());

        response.addHeader(tokenUtils.getAuthHeader(), "Bearer " + jwtToken);
        response.addHeader(tokenUtils.getExpHeader(), String.valueOf(tokenUtils.getExpiresIn()));
        response.addHeader("Access-Control-Allow-Headers",  "Origin, X-Requested-With, Content-Type, Accept, Authorization, Expires-In");
        response.addHeader("Access-Control-Expose-Headers", "Authorization, Expires-In");
    }
}
