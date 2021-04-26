package bsep.tim4.adminApp.users.config;

import bsep.tim4.adminApp.users.jwt.JwtAuthenticationFilter;
import bsep.tim4.adminApp.users.jwt.JwtUsernameAndPasswordAuthenticationFilter;
import bsep.tim4.adminApp.users.jwt.TokenUtils;
import bsep.tim4.adminApp.users.service.SecureUserDetailsService;
import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.keycloak.adapters.springsecurity.KeycloakConfiguration;
import org.keycloak.adapters.springsecurity.KeycloakSecurityComponents;
import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider;
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.*;

//@Configuration
//@EnableWebSecurity
//// Ukljucivanje podrske za anotacije "@Pre*" i "@Post*" koje ce aktivirati autorizacione provere za svaki pristup metodi
//@EnableGlobalMethodSecurity(prePostEnabled = true)
//@KeycloakConfiguration
//@Configuration i @EnableWebSecurity i ComponentScan se zamenjuju sa @KeycloakConfiguration
//@ComponentScan(basePackageClasses = KeycloakSecurityComponents.class)
@Configuration
@EnableWebSecurity
@ComponentScan(basePackageClasses = KeycloakSecurityComponents.class)
public class WebSecurityConfiguration extends KeycloakWebSecurityConfigurerAdapter {

//    @Autowired
//    TokenUtils tokenUtils;
//
//    @Autowired
//    private SecureUserDetailsService secureUserDetailsService;
//
//    // Handler za vracanje 401 UNAUTHORIZED
//    @Autowired
//    private RestAuthenticationEntryPoint restAuthenticationEntryPoint;
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        // BCryptPasswordEncoder automatski implementira 16-bitni salt
//        // 16-bitni je i preporucen
//        // preporucena minimalna vrednost
//        return new BCryptPasswordEncoder(12);
//    }
//
//    @Bean
//    @Override
//    public AuthenticationManager authenticationManagerBean() throws Exception {
//        return super.authenticationManagerBean();
//    }
//
//    @Autowired
//    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(secureUserDetailsService).passwordEncoder(passwordEncoder());
//    }
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http
//        		.cors().and()
//                .csrf().disable()
//                // REST ima stateless komunikaciju servera i korisnika
//                .sessionManagement()
//                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                    .and()
//                // rukovalac pokusajem neautentifikovanog pristupa
//                .exceptionHandling()
//                	.authenticationEntryPoint(restAuthenticationEntryPoint)
//                	.and()
//                // dozvola pristupa zahteva
//                .authorizeRequests()
//                	.antMatchers(HttpMethod.OPTIONS).permitAll()
//                    .antMatchers("/**").permitAll()
//                	// navoditi u redosledu: more specific first!
//                    // dok su u
//                    //.anyRequest().authenticated()
//                    .and()
//                // dozvola odjave
//                .logout()
//                	.logoutUrl("/logout-user")
//                	.logoutSuccessUrl("/successful-logout.html")
//                    .and()
//                // dodavanje custom filtera
//                .addFilterBefore(new JwtAuthenticationFilter(tokenUtils, secureUserDetailsService),
//                        BasicAuthenticationFilter.class);
//    }

//    @Autowired
//    public void configureGlobal(AuthenticationManagerBuilder auth) {
//        SimpleAuthorityMapper grantedAuthorityMapper = new SimpleAuthorityMapper();
//        grantedAuthorityMapper.setPrefix("ROLE_");
//
//        KeycloakAuthenticationProvider keycloakAuthenticationProvider = keycloakAuthenticationProvider();
//        keycloakAuthenticationProvider.setGrantedAuthoritiesMapper(grantedAuthorityMapper);
//        auth.authenticationProvider(keycloakAuthenticationProvider);
//    }
//
//    @Bean
//    public KeycloakSpringBootConfigResolver KeycloakConfigResolver() {
//        return new KeycloakSpringBootConfigResolver();
//    }
//
//    @Bean
//    @Override
//    protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
//        return new RegisterSessionAuthenticationStrategy(
//                new SessionRegistryImpl());
//    }
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        super.configure(http);
//        http.authorizeRequests()
//                .antMatchers("/api/certificate")
//                .hasRole("user")
//                .antMatchers("/api/certificate/*")
//                .hasRole("user")
//                .anyRequest()
//                .permitAll();
//    }
//
//    @Bean("corsConfigurationSource")
//    CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration configuration = new CorsConfiguration();
//        configuration.setAllowedOrigins(Arrays.asList("https://localhost:4200", "https://localhost:8081"));
//        configuration.setAllowedHeaders(Arrays.asList(CorsConfiguration.ALL));
//        configuration.setAllowedMethods(Arrays.asList(CorsConfiguration.ALL));
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", configuration);
//        return source;
//    }

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
                .antMatchers("/api/certificates").hasAnyRole("user")
                .antMatchers("/api/certificates/*").hasAnyRole("user")
                .antMatchers("/api/csr").hasAnyRole("user")
                .antMatchers("/api/csr/*").hasAnyRole("user")
                .anyRequest().authenticated()
                .and().cors();
        http.headers().xssProtection();
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
