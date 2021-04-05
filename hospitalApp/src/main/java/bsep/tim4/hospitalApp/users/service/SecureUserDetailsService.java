package bsep.tim4.hospitalApp.users.service;

import bsep.tim4.hospitalApp.users.model.User;
import bsep.tim4.hospitalApp.users.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class SecureUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws  UsernameNotFoundException {
        User user = userRepository.findOneByEmail(username);

        if (user == null) {
            throw new UsernameNotFoundException(String.format("No user found with email %s.", username));
        }

        return user;
    }
}
