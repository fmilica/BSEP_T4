package bsep.tim4.adminApp.config;

import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.keycloak.adapters.springsecurity.KeycloakSecurityComponents;
import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider;
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.*;

//@KeycloakConfiguration
//@Configuration i @EnableWebSecurity i ComponentScan se zamenjuju sa @KeycloakConfiguration
//@ComponentScan(basePackageClasses = KeycloakSecurityComponents.class)
@Configuration
@EnableWebSecurity
@ComponentScan(basePackageClasses = KeycloakSecurityComponents.class)
public class WebSecurityConfiguration extends KeycloakWebSecurityConfigurerAdapter {

    @Autowired
    public void configureGlobal(
            AuthenticationManagerBuilder auth) throws Exception {
        KeycloakAuthenticationProvider keycloakAuthenticationProvider
                = keycloakAuthenticationProvider();
        keycloakAuthenticationProvider.setGrantedAuthoritiesMapper(
                new SimpleAuthorityMapper());
        auth.authenticationProvider(keycloakAuthenticationProvider);
    }

    @Bean
    public KeycloakSpringBootConfigResolver KeycloakConfigResolver() {
        return new KeycloakSpringBootConfigResolver();
    }

    @Bean
    @Override
    protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
        return new RegisterSessionAuthenticationStrategy(
                new SessionRegistryImpl());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);
        http.csrf().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()
                .antMatchers("/api/csr/verification").permitAll()
                .antMatchers("/api/csr/verification/*").permitAll()
                .antMatchers("/api/certificate/validate").permitAll()
                .antMatchers("/api/certificate/validate/*").permitAll()
                .antMatchers("/api/certificate/download").permitAll()
                .antMatchers("/api/certificate/download/*").permitAll()
                .antMatchers("/api/hospital").hasAnyRole("ADMIN")
                .antMatchers("/api/hospital/*").hasAnyRole("ADMIN")
                .antMatchers("/api/hospital/not-in/*").hasAnyRole("ADMIN")
                .antMatchers("/api/hospital/add-simulator/*").hasAnyRole("ADMIN")
                .antMatchers("/api/csr/receive").hasAnyRole("ADMIN")
                .antMatchers("/api/csr/receive/*").hasAnyRole("ADMIN")
                .antMatchers("/api/certificates").hasAnyRole("SUPER_ADMIN")
                .antMatchers("/api/certificates/*").hasAnyRole("SUPER_ADMIN")
                .antMatchers("/api/csr").hasAnyRole("SUPER_ADMIN")
                .antMatchers("/api/csr/*").hasAnyRole("SUPER_ADMIN")
                .antMatchers("/**").hasAnyRole("SUPER_ADMIN")
                .anyRequest().authenticated()
                .and().cors();
        http.headers().xssProtection()
        .and().contentSecurityPolicy("script-src 'self'");;
    }

    @Bean("corsConfigurationSource")
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("https://localhost:4200", "https://localhost:8081"));
        configuration.setAllowedHeaders(Arrays.asList(CorsConfiguration.ALL));
        configuration.setAllowedMethods(Arrays.asList(CorsConfiguration.ALL));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
