package bsep.tim4.hospitalApp.users.config;

import bsep.tim4.hospitalApp.users.jwt.JwtAuthenticationFilter;
import bsep.tim4.hospitalApp.users.jwt.JwtUsernameAndPasswordAuthenticationFilter;
import bsep.tim4.hospitalApp.users.jwt.TokenUtils;
import bsep.tim4.hospitalApp.users.service.SecureUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
// Ukljucivanje podrske za anotacije "@Pre*" i "@Post*" koje ce aktivirati autorizacione provere za svaki pristup metodi
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    TokenUtils tokenUtils;

    @Autowired
    private SecureUserDetailsService secureUserDetailsService;

    // Handler za vracanje 401 UNAUTHORIZED
    @Autowired
    private RestAuthenticationEntryPoint restAuthenticationEntryPoint;
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(secureUserDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
        		.cors().and()
                .csrf().disable()
                // REST ima stateless komunikaciju servera i korisnika
                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()
                // rukovalac pokusajem neautentifikovanog pristupa
                .exceptionHandling()
                	.authenticationEntryPoint(restAuthenticationEntryPoint)
                	.and()
                // dozvola pristupa zahteva
                .authorizeRequests()
                	.antMatchers(HttpMethod.OPTIONS).permitAll()
                    .antMatchers("/**").permitAll()
                	// navoditi u redosledu: more specific first!
                    // dok su u
                    //.anyRequest().authenticated()
                    .and()
                // dozvola odjave
                .logout()
                	.logoutUrl("/logout-user")
                	.logoutSuccessUrl("/successful-logout.html")
                    .and()
                // dodavanje custom filtera
                .addFilter(new JwtUsernameAndPasswordAuthenticationFilter(authenticationManager(), tokenUtils))
                .addFilterAfter(new JwtAuthenticationFilter(tokenUtils, secureUserDetailsService),
                				JwtUsernameAndPasswordAuthenticationFilter.class);
    }

}
