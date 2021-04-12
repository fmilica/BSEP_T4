package bsep.tim4.hospitalApp.users.service;

import bsep.tim4.hospitalApp.users.model.User;
import bsep.tim4.hospitalApp.users.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.LockedException;
import org.springframework.stereotype.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.Date;

@Service
public class UserService {

    public static final int MAX_FAILED_ATTEMPTS = 5;
    private static final long LOCK_TIME_DURATION = 24 * 60 * 60 * 1000; // 24 hours

    @Autowired
    UserRepository userRepository;

    public User findByEmail(String email) {
        return userRepository.findOneByEmail(email);
    }

    public void increaseFailedAttempts(User user) {
        user.setFailedAttempts(user.getFailedAttempts() + 1);
        userRepository.save(user);
    }

    public void resetFailedAttempts(User user) {
        user.setFailedAttempts(0);
        userRepository.save(user);
    }

    public void lock(User user) {
        user.setAccountNonLocked(false);
        Timestamp now = new Timestamp(new Date().getTime());
        user.setLastAccountLockDate(now);
        userRepository.save(user);
    }

    public boolean unlockWhenTimeExpired(User user) {
        long lockTimeInMillis = user.getLastAccountLockDate().getTime();
        long currentTimeInMillis = System.currentTimeMillis();

        if (lockTimeInMillis + LOCK_TIME_DURATION < currentTimeInMillis) {
            user.setAccountNonLocked(true);
            user.setLastAccountLockDate(null);
            user.setFailedAttempts(0);
            userRepository.save(user);

            return true;
        }

        return false;
    }

    //Login fail
    public String onAuthenticationFailure(String email) {
        String exception ="Incorrect email or password";

        User user = this.findByEmail(email);
        if (user != null) {
            if (user.isEnabled() && user.isAccountNonLocked()) {
                if (user.getFailedAttempts() < MAX_FAILED_ATTEMPTS - 1) {
                    this.increaseFailedAttempts(user);
                } else {
                    this.lock(user);
                    exception = "Your account has been locked due to 5 failed login attempts."
                            + " It will be unlocked after 24 hours.";
                }
            } else if (!user.isAccountNonLocked()) {
                if (this.unlockWhenTimeExpired(user)) {
                    exception = "Your account has been unlocked. Please try to login again.";
                } else {
                    exception = "Your account is locked due to multiple failed login attempts.";
                }
            }
        }
        return exception;
    }

    //Successfull login
    public void onAuthenticationSuccess(User user) {
        if (user.getFailedAttempts() > 0) {
            this.resetFailedAttempts(user);
        }
    }

}
