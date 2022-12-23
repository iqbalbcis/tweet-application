package com.main.security.config;

import com.main.security.exception.JwtUnAuthorizedResponseAuthenticationEntryPoint;
import com.main.security.filter.JwtRequestFilter;
import com.main.security.logout.JWTLogoutSuccessHandler;
import com.main.security.service.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.main.constants.CommonConstant.*;

@Configuration
@EnableWebSecurity
public class SpringSecurityWithJWTConfiguration {

    private MyUserDetailsService myUserDetailsService;
    private JwtRequestFilter jwtRequestFilter;
    private JwtUnAuthorizedResponseAuthenticationEntryPoint entryPoint;
    private JWTLogoutSuccessHandler jwtLogoutSuccessHandler;

    @Autowired
    public SpringSecurityWithJWTConfiguration(final MyUserDetailsService myUserDetailsService,
                                              final JwtRequestFilter jwtRequestFilter,
                                              final JwtUnAuthorizedResponseAuthenticationEntryPoint entryPoint,
                                              final JWTLogoutSuccessHandler jwtLogoutSuccessHandler) {

        this.myUserDetailsService = myUserDetailsService;
        this.jwtRequestFilter = jwtRequestFilter;
        this.entryPoint = entryPoint;
        this.jwtLogoutSuccessHandler = jwtLogoutSuccessHandler;
    }

    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(myUserDetailsService) // without no encode password
        .passwordEncoder(encodePWD()); // with encode password
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .exceptionHandling().authenticationEntryPoint(entryPoint).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()
                .requestMatchers(AUTHENTICATIONPATH).permitAll() // need to the path in constant class
                .requestMatchers(ACTUATOR, H2CONSOLE).permitAll()
                .requestMatchers("/tweets/register").permitAll()
                .anyRequest().authenticated();

        // http.headers().frameOptions().disable();
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        // disable page caching
        http.headers()
                .frameOptions().sameOrigin()  // required to set for H2 else H2 Console will be blank.
                .cacheControl();

        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .requestMatchers(HttpMethod.OPTIONS, "/**")
                .requestMatchers("/v3/api-docs",
                        "/configuration/ui",
                        "/v3/api-docs/swagger-config",
                        "/swagger-resources/**",
                        "/configuration/security",
                        "/swagger-ui/index.html",
                        "/swagger-ui/**",
                        "/webjars/**");
    }

//    @Bean
//    public PasswordEncoder encodePWD() {
//        return new BCryptPasswordEncoder();
//    }

    @Bean
    public PasswordEncoder encodePWD() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();

    }
}