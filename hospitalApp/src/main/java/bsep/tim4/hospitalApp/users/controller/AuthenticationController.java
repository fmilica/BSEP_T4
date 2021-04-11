package bsep.tim4.hospitalApp.users.controller;

import bsep.tim4.hospitalApp.users.dto.UserLoginDTO;
import bsep.tim4.hospitalApp.users.jwt.TokenUtils;
import bsep.tim4.hospitalApp.users.model.User;
import bsep.tim4.hospitalApp.users.service.SecureUserDetailsService;
import bsep.tim4.hospitalApp.users.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

//Kontroler zaduzen za autentifikaciju korisnika
@RestController
@RequestMapping(value = "/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthenticationController {

    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private SecureUserDetailsService userDetailsService;

    @Autowired
    private UserService userService;

    // Prvi endpoint koji pogadja korisnik kada se loguje.
    // Tada zna samo svoje korisnicko ime i lozinku i to prosledjuje na backend.
    @PostMapping("/login")
    public ResponseEntity<Void> createAuthenticationToken(@RequestBody UserLoginDTO authenticationRequest,
                                                                    HttpServletResponse response) {
        try{
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(),
                            authenticationRequest.getPassword()));

            // Ubaci korisnika u trenutni security kontekst
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Kreiraj token za tog korisnika
            User user = (User) authentication.getPrincipal();

            //Nakon uspesne autentifikacije korisnika na sistem anulirati neuspesne pokusaje
            userService.onAuthenticationSuccess(user);

            String jwt = tokenUtils.generateToken(user.getEmail(), user.getAuthorities());

            // Vrati token kao odgovor na uspesnu autentifikaciju
            response.addHeader(tokenUtils.getAuthHeader(), "Bearer " + jwt);
            response.addHeader(tokenUtils.getExpHeader(), String.valueOf(tokenUtils.getExpiresIn()));
            response.addHeader("Access-Control-Allow-Headers",  "Origin, X-Requested-With, Content-Type, Accept, Authorization, Expires-In");
            response.addHeader("Access-Control-Expose-Headers", "Authorization, Expires-In");
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            String exception = userService.onAuthenticationFailure(authenticationRequest.getUsername());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, exception);
        }
    }
/*
    // Endpoint za registraciju novog korisnika
    @PostMapping("/signup")
    public ResponseEntity<User> addUser(@RequestBody UserRequest userRequest, UriComponentsBuilder ucBuilder) {

        User existUser = this.userService.findByUsername(userRequest.getUsername());
        if (existUser != null) {
            throw new ResourceConflictException(userRequest.getId(), "Username already exists");
        }

        User user = this.userService.save(userRequest);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/api/user/{userId}").buildAndExpand(user.getId()).toUri());
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    // U slucaju isteka vazenja JWT tokena, endpoint koji se poziva da se token osvezi
    @PostMapping(value = "/refresh")
    public ResponseEntity<UserTokenState> refreshAuthenticationToken(HttpServletRequest request) {

        String token = tokenUtils.getToken(request);
        String username = this.tokenUtils.getUsernameFromToken(token);
        User user = (User) this.userDetailsService.loadUserByUsername(username);

        if (this.tokenUtils.canTokenBeRefreshed(token, user.getLastPasswordResetDate())) {
            String refreshedToken = tokenUtils.refreshToken(token);
            int expiresIn = tokenUtils.getExpiredIn();

            return ResponseEntity.ok(new UserTokenState(refreshedToken, expiresIn));
        } else {
            UserTokenState userTokenState = new UserTokenState();
            return ResponseEntity.badRequest().body(userTokenState);
        }
    }

    @RequestMapping(value = "/change-password", method = RequestMethod.POST)
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> changePassword(@RequestBody PasswordChanger passwordChanger) {
        userDetailsService.changePassword(passwordChanger.oldPassword, passwordChanger.newPassword);

        Map<String, String> result = new HashMap<>();
        result.put("result", "success");
        return ResponseEntity.accepted().body(result);
    }

    static class PasswordChanger {
        public String oldPassword;
        public String newPassword;
    }*/
}
